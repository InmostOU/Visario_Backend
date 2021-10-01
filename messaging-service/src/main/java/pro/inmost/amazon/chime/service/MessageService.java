package pro.inmost.amazon.chime.service;

import org.springframework.web.multipart.MultipartFile;
import pro.inmost.amazon.chime.enums.MessagePersistance;
import pro.inmost.amazon.chime.enums.MessageType;
import pro.inmost.amazon.chime.model.dto.*;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.model.entity.Attachment;
import pro.inmost.amazon.chime.model.entity.Message;
import software.amazon.awssdk.services.chime.model.MessagingSessionEndpoint;

public interface MessageService {
    /**
     *
     *
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
    Message sendMessage(String channelArn, String content, MessageType type, MessagePersistance persistence, Metadata metadata,
                        String clientRequestToken, String chimeBearer, Boolean withAttachment);

    /**
     *
     * @param channelArn         The ARN of the channel.
     * @return                   An instance of {@link ChannelMessages} class, that consist a list of {@link MessageDetail}.
     */
    ChannelMessages fetchChannelMessages(String channelArn);

    /**
     * The details of the endpoint for the messaging session.
     *
     * @return  url for connection to the messaging session.
     */
    String getMessagingSessionEndpoint();

    /**
     * Edit a message's content. Set 'redacted' flag to 'true'
     *
     * @param messageEditRequest
     * @return
     */
     Message editMessage(MessageEditRequest messageEditRequest);

     BaseResponse deleteChannelMessage(MessageDeleteRequest request);

     BaseResponse sendMessageWithAttachment(MultipartFile attachment, String channelArn, String content, MessageType type, MessagePersistance persistence, Metadata metadata,
                                            String clientRequestToken, String chimeBearer);
}
