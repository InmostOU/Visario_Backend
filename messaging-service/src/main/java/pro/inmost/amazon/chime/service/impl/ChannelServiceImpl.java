package pro.inmost.amazon.chime.service.impl;

import com.amazonaws.AmazonClientException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.inmost.amazon.chime.enums.ChannelMode;
import pro.inmost.amazon.chime.enums.ChannelPrivacy;
import pro.inmost.amazon.chime.exception.ModsCannotLeaveChannelException;
import pro.inmost.amazon.chime.exception.ChannelNotFoundException;
import pro.inmost.amazon.chime.model.dto.*;
import pro.inmost.amazon.chime.model.dto.ChannelSummary;
import pro.inmost.amazon.chime.model.dto.ChannelCreateResponse;
import pro.inmost.amazon.chime.model.entity.*;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.Channel;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.repository.*;
import pro.inmost.amazon.chime.service.ChannelService;
import pro.inmost.amazon.chime.service.ChimeClientService;
import pro.inmost.amazon.chime.service.Prediction;
import pro.inmost.amazon.chime.service.UserService;
import software.amazon.awssdk.services.chime.model.*;
import software.amazon.awssdk.services.chime.model.CreateChannelRequest;
import java.util.*;

@Service
@AllArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChimeClientService clientService;
    private final UserService userService;
    private final AppInstanceUserRepository appInstanceUserRepository;
    private final ChannelRepository channelRepository;
    private final Channel2AppInstanceUserRepository channel2AppInstanceUserRepository;
    private final ModeratorRepository moderatorRepository;
    private final ContactsRepository contactsRepository;

    /**
     * Creates a channel to which you can add users and send messages.
     *
     * @param appInstanceArn     The ARN of the channel request. Required.
     * @param chimeBearer        The AppInstanceUserArn of the {@link AppInstanceUser} that makes the API call. Required.
     * @param clientRequestToken The client token for the request. An Idempotency token.
     * @param metaData           The metadata of the creation request. Limited to 1KB and UTF-8.
     * @param mode               The channel mode: UNRESTRICTED or RESTRICTED. Administrators, moderators, and channel
     *                           members can add themselves and other members to unrestricted channels. Only administrators
     *                           and moderators can add members to restricted channels.
     * @param name               The name of the channel. Required.
     * @param privacy            The channel's privacy level: PUBLIC or PRIVATE. Private channels aren't discoverable
     *                           by users outside the channel. Public channels are discoverable by anyone in the AppInstance .
     * @param tags               The tags for the creation request. Describes a tag applied to a resource.
     * @return
     */
    @Override
    @Transactional
    public ChannelCreateResponse createChannel(boolean isConversation, String metaData, ChannelMode mode, String name, ChannelPrivacy privacy, String description, Tag... tags)
            throws BadRequestException, ForbiddenException, UnauthorizedClientException, ConflictException, ResourceLimitExceededException,
                    ThrottledClientException, ServiceUnavailableException, ServiceFailureException {

        User currentUser = userService.getCurrentUser();
        AppInstanceUser currentAppInstanceUser = appInstanceUserRepository.findByUser(currentUser);

        CreateChannelResponse response = clientService.getAmazonChimeClient()
                .createChannel(CreateChannelRequest.builder()
                        .appInstanceArn(currentAppInstanceUser.getAppInstanceArn())
                        .clientRequestToken(UUID.randomUUID().toString())
                        .metadata(metaData)
                        .mode(mode.toString())
                        .chimeBearer(currentAppInstanceUser.getAppInstanceUserArn())
                        .name(name)
                        .privacy(privacy.toString())
                        .tags(tags)
                        .build()
                );

        if (!isConversation) {
            Channel channel = channelRepository.save(
                    Channel.builder()
                            .channelArn(response.channelArn())
                            .metadata(metaData != null ? metaData : "metadata")
                            .privacy(privacy.toString())
                            .name(name)
                            .mode(mode.toString())
                            .createdBy(currentAppInstanceUser)
                            .description((description.isEmpty() || description == null) ? "" : description)
                            .build()
            );

            createModerator(currentAppInstanceUser, channel);
            createChannelMemberShip(false, response.channelArn(), currentAppInstanceUser.getAppInstanceUserArn());
        }

        return ChannelCreateResponse.builder()
                 .status(200)
                 .timestamp(new Date().getTime())
                 .message("Channel created successfully.")
                 .channelArn(response.channelArn())
                 .path("/channels/create")
                 .build();
    }

    /**
     * Adds a user to a channel. The InvitedBy response field is derived from the request header. A channel member can:
     * <p>
     * - List messages
     * - Send messages
     * - Receive messages
     * - Edit their own messages
     * - Leave the channel
     * - Privacy settings impact this action as follows:
     * <p>
     * Public Channels: You do not need to be a member to list messages, but you must be a member to send messages.
     * Private Channels: You must be a member to list or send messages.
     *
     * @param channelArn  The ARN of the channel to which you're adding users.
     * @param memberArn   The ARN of the member you want to add to the channel.
     * @param type        The membership type of a user, DEFAULT or HIDDEN. Default members are always returned as part
     *                    of ListChannelMemberships. Hidden members are only returned if the type filter in
     *                    ListChannelMemberships equals HIDDEN.
     *                    Otherwise hidden members are not returned. This is only supported by moderators.
     *                    <p>
     *                    Possible values:
     *                    - DEFAULT
     *                    - HIDDEN
     * @param chimeBearer The AppInstanceUserArn of the {@link AppInstanceUser} that makes the API call.
     * @return
     */
    @Override
    public BaseResponse createChannelMemberShip(boolean isConversation, String channelArn, String memberArn)
                         throws BadRequestException, ForbiddenException, UnauthorizedClientException, ConflictException,
                         ResourceLimitExceededException, ThrottledClientException, ServiceUnavailableException {

        Channel channel = channelRepository.findByChannelArn(channelArn);
        AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());
        AppInstanceUser invitedUser = appInstanceUserRepository.findByAppInstanceUserArn(memberArn);
        Channel2AppInstanceUser relation = channel2AppInstanceUserRepository.findByAppInstanceUserIdAndChannelId(
                invitedUser.getId(), channel.getId()
        );

        if (channel != null && relation != null) {
            return BaseResponse.builder()
                    .message("User " + invitedUser.getUser().getUsername() + " is already in the channel.")
                    .status(226)
                    .path("/channels/addMemberToChannel")
                    .build();
        }

        if (channel == null || currentUser == null)
            throw new RuntimeException("There's something wrong with fetching channel or app instance user");

        CreateChannelMembershipResponse createChannelMembershipResponse = clientService.getAmazonChimeClient()
                .createChannelMembership(
                        CreateChannelMembershipRequest.builder()
                        .channelArn(channelArn)
                        .memberArn(memberArn)
                        .chimeBearer(currentUser.getAppInstanceUserArn())
                        .type(ChannelMembershipType.DEFAULT)
                        .build()
                );
        if (createChannelMembershipResponse == null)
            throw new AmazonClientException("Something wrong with sending request to Amazon");

        relation = Channel2AppInstanceUser.builder()
                .channelId(channel.getId())
                .appInstanceUserId(invitedUser.getId())
                .build();
        channel2AppInstanceUserRepository.save(relation);

        return BaseResponse.builder()
                .message("User " + invitedUser.getUser().getUsername() + " has been added to the channel.")
                .status(200)
                .path("/channels/addMemberToChannel")
                .build();
    }

    @Override
    public ChannelSummaries listChannelMembershipsForLoggedAppInstanceUser() {
        List<ChannelSummary> summaries = new ArrayList<>();
        AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());

        for (Channel channel : currentUser.getChannels()) {

            summaries.add(ChannelSummary.builder()
                    .channelArn(channel.getChannelArn())
                    .metadata(channel.getMetadata())
                    .privacy(channel.getPrivacy())
                    .name(channel.getName())
                    .mode(channel.getMode())
                    .isMember(true)
                    .isAdmin(false)
                    .isModerator(channel.getModerators().contains(currentUser) ? true : false)
                    .description(channel.getDescription())
                    .membersCount(channel.getUsers().size())
                    .build());
        }

        return ChannelSummaries.builder()
                .status(200)
                .message("OK")
                .data(summaries)
                .build();
    }

    @Override
    public ChannelSummaries findChannelsByName(String channelName) {

        AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());
        List<Channel> channels = channelRepository.findByName(channelName);
        List<ChannelSummary> summaries = new ArrayList<>();
        List<Channel2AppInstanceUser> userMemberships = channel2AppInstanceUserRepository.findByAppInstanceUserId(
                appInstanceUserRepository.findByUser(userService.getCurrentUser()).getId()
        );

        for (Channel channel : channels) {
            if (channel.getPrivacy().equals(ChannelPrivacy.PUBLIC.toString())) {
                summaries.add(
                        ChannelSummary.builder()
                                .channelArn(channel.getChannelArn())
                                .metadata(channel.getMetadata())
                                .privacy(channel.getPrivacy())
                                .name(channel.getName())
                                .mode(channel.getMode())
                                .isMember(userMemberships.contains(Channel2AppInstanceUser.builder().appInstanceUserId(currentUser.getId()).channelId(channel.getId()).build()) ? true : false)
                                .isModerator(channel.getModerators().contains(currentUser))
                                .membersCount(channel.getUsers().size())
                                .build()
                );
            }
        }


        return ChannelSummaries.builder()
                .message("Found channels: ")
                .status(200)
                .data(summaries)
                .build();
    }

    @Override
    public BaseResponse leaveChannel(String channelArn) throws BadRequestException, ForbiddenException, UnauthorizedClientException, ConflictException,
            ThrottledClientException, ServiceUnavailableException, ServiceFailureException, ChannelNotFoundException, ModsCannotLeaveChannelException {

        AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());
        Channel channel = channelRepository.findByChannelArn(channelArn);

        if (currentUser.getId().longValue() == channel.getCreatedBy().getId().longValue()
            && channel.getMode().equals("PRIVATE") && channel.getUsers().size() > 1)
            throw new ModsCannotLeaveChannelException("You cannot leave a private channel until there's members.");

        Channel2AppInstanceUser relation = channel2AppInstanceUserRepository.findByAppInstanceUserIdAndChannelId(
            currentUser.getId(), channel.getId()
        );

        if (relation != null) {
            clientService.getAmazonChimeClient().deleteChannelMembership(
                    DeleteChannelMembershipRequest.builder()
                            .channelArn(channel.getChannelArn())
                            .chimeBearer(currentUser.getAppInstanceUserArn())
                            .memberArn(currentUser.getAppInstanceUserArn())
                            .build()
            );
            channel2AppInstanceUserRepository.delete(relation);
        } else {
            throw new ChannelNotFoundException("Channel with ARN " + channelArn + " not found.");
        }

        return BaseResponse.builder()
                .timestamp(new Date().getTime())
                .message("You successfully left the channel.")
                .status(200)
                .build();
    }

    @Override
    public MemberDetails fetchUserListFromChannel(String channelArn) {

        Channel channel = channelRepository.findByChannelArn(channelArn);
        List<MemberDetail> data = new ArrayList<>();
        User current = userService.getCurrentUser();
        Set<Contacts> contacts = contactsRepository.findAllByOwnerId(current.getId());

        Prediction<Contacts, Set<Contacts>, String> prediction = (contact, userContacts, value) -> {

            if (value.equals("EVERYONE"))
                return true;
            else if (value.equals("CONTACTS") && contacts.contains(contact))
                return true;
            else
                return false;

        };

        List<AppInstanceUser> users = new ArrayList<>(channel.getUsers());

        for (AppInstanceUser member : users) {

            Contacts contact = contactsRepository.findByOwnerIdAndUserId(current.getId(), member.getUser().getId());

            data.add(MemberDetail.builder()
                    .id(member.getUser().getId())
                    .userArn(member.getAppInstanceUserArn())
                    .firstName(member.getUser().getFirstName())
                    .lastName(member.getUser().getLastName())
                    .username(member.getUser().getUsername())
                    .email(prediction.make(contact, contacts, member.getUser().getShowEmailTo()) ? member.getUser().getEmail() : "")
                    // TODO: Add phone number
                    .phoneNumber(prediction.make(contact, contacts, member.getUser().getShowPhoneNumberTo()) ? "" : "")
                    // TODO: Implement logic for obtaining avatar url
                    .image("")
                    .about(Optional.of(member.getUser().getAbout()).orElse(""))
                    .online(false)
                    .inMyContacts(contacts.contains(contact))
                    // TODO: Implement logic for defining if member is an admin
                    .isAdmin(false)
                    .isMod(channel.getModerators().contains(member))
                    .build()
            );
        }

        return MemberDetails.builder()
                .status(200)
                .message("Members are fetched successfully.")
                .data(data)
                .build();
    }

    @Override
    public ChannelSummary getChannelByChannelArn(String channelArn) {

        Channel channel = channelRepository.findByChannelArn(channelArn);
        AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());
        Set<AppInstanceUser> channelUsers = channel.getUsers();

        return ChannelSummary.builder()
                .membersCount(channelUsers.size())
                .isModerator(channel.getModerators().contains(currentUser))
                .description(Optional.ofNullable(channel.getDescription()).orElse("No description"))
                // TODO: Define admin
                .isAdmin(false)
                .privacy(channel.getPrivacy())
                .metadata(Optional.ofNullable(channel.getMetadata()).orElse("No metadata"))
                .mode(channel.getMode())
                .name(channel.getName())
                .channelArn(channelArn)
                .build();

    }

    private void deleteChannel(Channel channel, AppInstanceUser user) {
        clientService.getAmazonChimeClient().deleteChannel(
                DeleteChannelRequest.builder()
                        .channelArn(channel.getChannelArn())
                        .chimeBearer(user.getAppInstanceUserArn())
                        .build()
        );
    }
    private void createModerator(AppInstanceUser appInstanceUser, Channel channel) {
        moderatorRepository.save(Moderator.builder()
            .appInstanceUserId(appInstanceUser.getId())
            .channelId(channel.getId())
        .build()
        );
    }
}
