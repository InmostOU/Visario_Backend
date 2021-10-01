package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.enums.ChannelMode;
import pro.inmost.amazon.chime.enums.ChannelPrivacy;
import pro.inmost.amazon.chime.exception.ModsCannotLeaveChannelException;
import pro.inmost.amazon.chime.exception.ChannelNotFoundException;
import pro.inmost.amazon.chime.model.dto.BaseResponse;
import pro.inmost.amazon.chime.model.dto.ChannelSummaries;
import pro.inmost.amazon.chime.model.dto.ChannelSummary;
import pro.inmost.amazon.chime.model.dto.MemberDetails;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import software.amazon.awssdk.services.chime.model.*;
import pro.inmost.amazon.chime.model.dto.ChannelCreateResponse;

public interface ChannelService {
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
     ChannelCreateResponse createChannel(boolean isConversation, String metaData, ChannelMode mode, String name, ChannelPrivacy privacy, String description, Tag... tags)
            throws BadRequestException, ForbiddenException, UnauthorizedClientException, ConflictException, ResourceLimitExceededException,
            ThrottledClientException, ServiceUnavailableException, ServiceFailureException;

    /**
     * Adds a user to a channel. The InvitedBy response field is derived from the request header. A channel member can:
     *
     * - List messages
     * - Send messages
     * - Receive messages
     * - Edit their own messages
     * - Leave the channel
     * - Privacy settings impact this action as follows:
     *
     * Public Channels: You do not need to be a member to list messages, but you must be a member to send messages.
     * Private Channels: You must be a member to list or send messages.
     *
     *
     * @param channelArn  The ARN of the channel to which you're adding users.
     * @param memberArn   The ARN of the member you want to add to the channel.
     * @param type        The membership type of a user, DEFAULT or HIDDEN. Default members are always returned as part
     *                    of ListChannelMemberships. Hidden members are only returned if the type filter in
     *                    ListChannelMemberships equals HIDDEN.
     *                    Otherwise hidden members are not returned. This is only supported by moderators.
     *
     *                    Possible values:
     *                    - DEFAULT
     *                    - HIDDEN
     * @param chimeBearer The AppInstanceUserArn of the {@link AppInstanceUser} that makes the API call.
     * @return
     */
    BaseResponse createChannelMemberShip(boolean isConversation, String channelArn, String memberArn)
            throws BadRequestException, ForbiddenException, UnauthorizedClientException, ConflictException,
            ResourceLimitExceededException, ThrottledClientException, ServiceUnavailableException, ServiceFailureException;

    <T> T listChannelMembershipsForLoggedAppInstanceUser();

    ChannelSummaries findChannelsByName(String channelName);

    ChannelSummary getChannelByChannelArn(String channelArn);

    BaseResponse leaveChannel(String channelArn) throws BadRequestException, ForbiddenException, UnauthorizedClientException, ConflictException,
            ThrottledClientException, ServiceUnavailableException, ChannelNotFoundException, ModsCannotLeaveChannelException;

    MemberDetails fetchUserListFromChannel(String channelArn);
}
