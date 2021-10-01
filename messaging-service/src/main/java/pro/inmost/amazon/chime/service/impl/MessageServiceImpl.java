package pro.inmost.amazon.chime.service.impl;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.inmost.amazon.chime.enums.MessagePersistance;
import pro.inmost.amazon.chime.enums.MessageType;
import pro.inmost.amazon.chime.exception.MessageNotFoundException;
import pro.inmost.amazon.chime.model.dto.*;
import pro.inmost.amazon.chime.model.entity.*;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.Channel;
import pro.inmost.amazon.chime.model.entity.User;
import pro.inmost.amazon.chime.repository.AppInstanceUserRepository;
import pro.inmost.amazon.chime.repository.ChannelRepository;
import pro.inmost.amazon.chime.repository.MessageRepository;
import pro.inmost.amazon.chime.service.*;
import software.amazon.awssdk.services.chime.model.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@AllArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    private static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final UserService userService;
    private final AppInstanceUserRepository appInstanceUserRepository;
    private final ChimeClientService chimeClientService;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final AttachmentService attachmentService;
    private final FileStorage fileStorage;

    /**
     * @param channelArn         The ARN of the channel. Required.
     * @param content            The content of the message. Required.
     * @param type               The type of message, STANDARD or CONTROL. Required.
     * @param persistence        Boolean that controls whether the message is persisted on the back end. Required.
     *
     *                           Possible values:
     *                           - PERSISTENT
     *                           - NON_PERSISTENT
     * @param metadata           The optional metadata for each message.
     * @param clientRequestToken The Idempotency token for each client request. Required.
     * @param chimeBearer        The AppInstanceUserArn of the {@link AppInstanceUser} that makes the API call.
     * @return
     */
    @Override
    public Message sendMessage(String channelArn, String content, MessageType type, MessagePersistance persistence, Metadata metadata,
                               String clientRequestToken, String chimeBearer, Boolean withAttachment) {

        AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());
        Channel channel = channelRepository.findByChannelArn(channelArn);

        SendChannelMessageResponse sendChannelMessageResponse = chimeClientService.getAmazonChimeClient()
                .sendChannelMessage(
                        SendChannelMessageRequest.builder()
                        .channelArn(channelArn)
                        .clientRequestToken(clientRequestToken)
                        .persistence(persistence.toString())
                        .type(type.toString())
                        .content(content)
                        .metadata(metadata.toString())
                        .chimeBearer(chimeBearer)
                        .build()
                );

        Message message = Message.builder()
                .appInstanceUser(currentUser)
                .content(content)
                .channel(channel)
                .createdTimestamp(new Date())
                .messageId(sendChannelMessageResponse.messageId())
                .metadata(metadata.toString())
                .userToUser(false)
                .redacted(false)
                .withAttachment(withAttachment)
                .delivered(sendChannelMessageResponse.messageId() != null ? true : false)
                .build();

        messageRepository.save(message);

        return message;
    }

    @Override
    @SneakyThrows
    public BaseResponse sendMessageWithAttachment(MultipartFile file, String channelArn, String content, MessageType type,
                                                  MessagePersistance persistence, Metadata metadata, String clientRequestToken,
                                                  String chimeBearer) {

        User current = userService.getCurrentUser();
        Attachment attach = attachmentService.uploadAttachment(file, current.getUsername());

        metadata.setFileName(attachmentService.getFileName(file.getOriginalFilename()).orElse(UUID.randomUUID().toString()));
        metadata.setFileType(attachmentService.getFileType(file.getOriginalFilename()).orElse("unknown"));
        metadata.setUrl(fileStorage.getDownloadUrl(attach.getPrefix() + "/" + attach.getFileName() + "." + attach.getFileType()));

        Message message = sendMessage(channelArn, content, type, persistence, metadata, clientRequestToken, chimeBearer, true);

        message.setAttachment(attach);
        messageRepository.save(message);

        return BaseResponse.standard();
    }
    /**
     * @param channelArn The ARN of the channel.
     * @return An instance of {@link ChannelMessages} class, that consist a list of {@link MessageDetail}.
     */
    @Override
    public ChannelMessages fetchChannelMessages(String channelArn) {
        AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());

        User currentMessageSender ;
        Channel channel = channelRepository.findByChannelArn(channelArn);
        List<Message> messages = messageRepository.findByChannel(channel);
        List<MessageDetail> details = new ArrayList<>();

        for (Message message : messages) {
            currentMessageSender = message.getAppInstanceUser().getUser();

            if (message.getWithAttachment() != null && message.getWithAttachment()) {
                Metadata metadata = new Gson().fromJson(message.getMetadata(), Metadata.class);
                metadata.setUrl(attachmentService.getAttachmentUrl(message.getAttachment()));
                message.setMetadata(metadata.toString());
            }

            details.add(MessageDetail.builder()
                    .id(message.getId())
                    .createdTimestamp(message.getCreatedTimestamp().getTime())
                    .metadata(message.getMetadata())
                    .senderName(currentMessageSender.getFirstName() + " " + currentMessageSender.getLastName())
                    .type(MessageType.STANDARD.toString())
                    .messageId(message.getMessageId())
                    .lastEditedTimestamp(message.getLastEditedTimestamp() != null ? message.getLastEditedTimestamp().getTime() : 0)
                    .senderArn(message.getAppInstanceUser().getAppInstanceUserArn())
                    .content(message.getContent())
                    .redacted(message.getLastEditedTimestamp() == null ? false : true)
                    .isFromCurrentUser(currentUser.getAppInstanceUserArn() == message.getAppInstanceUser().getAppInstanceUserArn() ? true : false)
                    .channelArn(channelArn)
                    .delivered(message.getDelivered())
                    .build()
            );
        }

        return ChannelMessages.builder()
                    .status(200)
                    .message("Messages were fetched successfully")
                    .data(details)
                .build();
    }

    /**
     * Edit a message's content. Set 'redacted' flag to 'true'
     *
     * @param messageEditRequest
     * @return
     */
    @Override
    public Message editMessage(MessageEditRequest messageEditRequest) {
        Message message = messageRepository.findByMessageId(messageEditRequest.getMessageId()).get();
        AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());

        if (haveNoRights(message, currentUser))
            throw ForbiddenException.builder().message("You have no rights for this action").build();

        logger.info(String.format("EDIT MESSAGE::MESSAGE ID '%s' %n", message.getMessageId()));
        logger.info(String.format("EDIT MESSAGE::CONTENT '%s' %n", messageEditRequest.getContent()));

        updateChannelMessage(message, message.getChannel().getChannelArn(), currentUser.getAppInstanceUserArn(), messageEditRequest.getContent());

        message.setLastEditedTimestamp(new Date());
        message.setContent(messageEditRequest.getContent());
        messageRepository.save(message);

        return message;
    }

    @Override
    public BaseResponse deleteChannelMessage(MessageDeleteRequest request) {
        Message message = messageRepository.findByMessageId(request.getMessageId())
                .orElseThrow(MessageNotFoundException::new);
        AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());

        if (haveNoRights(message, currentUser))
            throw ForbiddenException.builder().message("You have no rights for this action").build();

        chimeClientService.getAmazonChimeClient()
                .deleteChannelMessage(DeleteChannelMessageRequest.builder()
                    .channelArn(message.getChannel().getChannelArn())
                    .messageId(request.getMessageId())
                    .chimeBearer("arn:aws:chime:us-east-1:277431928707:app-instance/c9f0aa1c-74c7-49af-8b75-b650f128511c/user/777")
                    .build()
                );

        logger.info(String.format("MESSAGE DELETE::MESSAGE ID '%s' %n DELETED", message.getMessageId()));
        logger.info(String.format("MESSAGE DELETE::MESSAGE CONTENT '%s' %n DELETED", message.getContent()));

        messageRepository.delete(message);

        return BaseResponse.builder()
                .path("/messages/edit")
                .message("Message was deleted successfully")
                .timestamp(new Date().getTime())
                .status(200)
                .build();
    }

    private boolean haveNoRights(Message message, AppInstanceUser currentUser) {

        if (message.getAppInstanceUser() == currentUser)
            return false;
        if (message.getChannel().getModerators().contains(currentUser))
            return false;


        return true;
    }

    private String updateChannelMessage(Message message, String channelArn, String appInstanceUserArn, String content) {
        UpdateChannelMessageResponse response = chimeClientService.getAmazonChimeClient()
                .updateChannelMessage(UpdateChannelMessageRequest.builder()
                        .content(content)
                        .messageId(message.getMessageId())
                        .chimeBearer(appInstanceUserArn)
                        .channelArn(channelArn)
                        .build());
        return response.messageId();
    }

    public String getMessagingSessionEndpoint() {
        return chimeClientService.getAmazonChimeClient().getMessagingSessionEndpoint(
                GetMessagingSessionEndpointRequest.builder().build()
        ).endpoint().toString();
    }
}
