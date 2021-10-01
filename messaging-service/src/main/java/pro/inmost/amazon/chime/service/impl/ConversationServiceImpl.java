package pro.inmost.amazon.chime.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.inmost.amazon.chime.enums.ChannelMode;
import pro.inmost.amazon.chime.enums.ChannelPrivacy;
import pro.inmost.amazon.chime.model.dto.*;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.Conversation;
import pro.inmost.amazon.chime.model.entity.ConversationToAppInstanceUser;
import pro.inmost.amazon.chime.repository.AppInstanceUserRepository;
import pro.inmost.amazon.chime.repository.ChannelRepository;
import pro.inmost.amazon.chime.repository.ConversationRepository;
import pro.inmost.amazon.chime.repository.ConversationToAppInstanceUserRepository;
import pro.inmost.amazon.chime.service.ChannelService;
import pro.inmost.amazon.chime.service.ConversationService;
import pro.inmost.amazon.chime.service.UserService;
import software.amazon.awssdk.services.chime.model.Tag;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class ConversationServiceImpl implements ConversationService {

    private final ChannelService channelService;
    private final ChannelRepository channelRepository;
    private final AppInstanceUserRepository appInstanceUserRepository;
    private final UserService userService;
    private final ConversationRepository conversationRepository;
    private final ConversationToAppInstanceUserRepository conversationToAppInstanceUserRepository;

    @Override
    @Transactional
    public BaseResponse createConversation(CreateConversationRequest request) {

        AppInstanceUser firstParticipant = appInstanceUserRepository.findByUser(userService.getCurrentUser());
        AppInstanceUser secondParticipant = appInstanceUserRepository.findByAppInstanceUserArn(request.getParticipantArn());
        String name = "Conversation " + firstParticipant.getUser().getUsername() + " to " + secondParticipant.getUser().getUsername();
        String description = name;
        // Basically conversation is a simple channel, so first we need to create the channel at Chime side
        ChannelCreateResponse response = channelService.createChannel(
                true,
                request.getMetadata(),
                ChannelMode.RESTRICTED,
                name,
                ChannelPrivacy.PRIVATE,
                description,
                Tag.builder().key("key").value("value").build()
        );
        // Then we save it as a conversation in our database
        conversationRepository.save(Conversation.builder()
                .channelArn(response.getChannelArn())
                .description(description)
                .name(name)
                .privacy("PRIVATE")
                .mode("RESTRICTED")
                .build()
        );

        inviteToConversation(response.getChannelArn(), firstParticipant.getAppInstanceUserArn(), secondParticipant.getAppInstanceUserArn());

        return BaseResponse.builder()
                .status(200)
                .message("Conversation is created successfully")
                .timestamp(new Date().getTime())
                .path("/channels/create/conversation")
                .build();
    }

    @Override
    public ConversationSummaries fetchConversations() {
        AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());
        List<Conversation> conversations = currentUser.getConversations();
        List<ConversationSummary> summaries = new ArrayList<>();

        conversations.forEach(
                conversation -> {
                    List<AppInstanceUser> participants = conversation.getParticipants();
                    summaries.add(
                            ConversationSummary.builder()
                            .name(participants.get(0).equals(currentUser) ? getConversationName(participants.get(1)) : getConversationName(participants.get(0)))
                            .build()
                    );
                }
        );

        return ConversationSummaries.builder()
                .message("Conversations are fetched successfully.")
                .status(200)
                .data(summaries)
                .build();
    }

    @Override
    public BaseResponse inviteToConversation(String channelArn, String firstParticipantArn, String secondParticipantArn) {
        // Basically conversation is a simple channel, so first we need to create memberships for participants
//        channelService.createChannelMemberShip(channelArn, firstParticipantArn);
//        channelService.createChannelMemberShip(channelArn, secondParticipantArn);
        // Then we store relationships into our database for first participants
        conversationToAppInstanceUserRepository.save(
                ConversationToAppInstanceUser.builder()
                .appInstanceUserId(appInstanceUserRepository.findByAppInstanceUserArn(firstParticipantArn).getId())
                .channelId(channelRepository.findByChannelArn(channelArn).getId())
                .build()
        );
        // ..and for the second
        conversationToAppInstanceUserRepository.save(
                ConversationToAppInstanceUser.builder()
                        .appInstanceUserId(appInstanceUserRepository.findByAppInstanceUserArn(secondParticipantArn).getId())
                        .channelId(channelRepository.findByChannelArn(channelArn).getId())
                        .build()
        );

        return null;
    }

    private String getConversationName(AppInstanceUser participant) {
        return participant.getUser().getFirstName() + " " + participant.getUser().getLastName();
    }
}
