package pro.inmost.amazon.chime.controllers;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.inmost.amazon.chime.enums.MessagePersistance;
import pro.inmost.amazon.chime.enums.MessageType;
import pro.inmost.amazon.chime.model.dto.*;
import pro.inmost.amazon.chime.model.entity.AppInstanceUser;
import pro.inmost.amazon.chime.repository.AppInstanceUserRepository;
import pro.inmost.amazon.chime.service.AttachmentService;
import pro.inmost.amazon.chime.service.MessageService;
import pro.inmost.amazon.chime.service.UserService;
import pro.inmost.amazon.chime.service.impl.MessageServiceImpl;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final AppInstanceUserRepository appInstanceUserRepository;
    private final UserService userService;
    private final AttachmentService attachmentService;
    private static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @SneakyThrows
    @PostMapping(value = "/send", consumes = {MediaType.APPLICATION_JSON_VALUE,
                                                               MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MessageSendResponse> sendWithAttachment(@RequestPart(value = "file") MultipartFile file,
                                                                  @RequestPart(value = "message") String message) {
         logger.info(message);
         MessageDto messageDto = new Gson().fromJson(message, MessageDto.class);
         messageDto.getMetadata().setFileName(attachmentService.getFileName(file.getOriginalFilename()).orElse(UUID.randomUUID().toString()));
         messageDto.getMetadata().setFileType(attachmentService.getFileType(file.getOriginalFilename()).orElse(UUID.randomUUID().toString()));

         AppInstanceUser currentUser = appInstanceUserRepository.findByUser(userService.getCurrentUser());

         messageService.sendMessageWithAttachment(
                file,
                messageDto.getChannelArn(),
                messageDto.getContent(),
                MessageType.STANDARD,
                MessagePersistance.NON_PERSISTENT,
                messageDto.getMetadata(),
                UUID.randomUUID().toString(),
                currentUser.getAppInstanceUserArn()
        );

        return ResponseEntity.ok(MessageSendResponse.builder()
                .timestamp(new Date().getTime())
                .status(200)
                .message("Sent successful")
                .path("/messages/send")
                .build()
        );
    }

    @GetMapping("/list")
    public ResponseEntity<ChannelMessages> getMessagesFromChannel(@RequestParam(name = "channelArn")
                                                                  String channelArn) {
        return ResponseEntity.ok(messageService.fetchChannelMessages(channelArn));
    }

    @GetMapping("/messaging-session")
    public ResponseEntity<MessagingSessionEndpoint> getMessagingSessionEndpoint() {
        // endpoint looks like MessagingSessionEndpoint(Url=node001.ue1.ws-messaging.chime.aws)
        // but we need only url, so we need to obtain only url
        String endpoint = messageService.getMessagingSessionEndpoint();

        return ResponseEntity.ok(MessagingSessionEndpoint.builder().url(
                endpoint.substring(29, endpoint.length() - 1)
        ).build());
    }


    @PostMapping("/edit")
    public ResponseEntity<MessageEditResponse> editMessage(@RequestBody MessageEditRequest request) {
        messageService.editMessage(request);

        return ResponseEntity.ok(
                MessageEditResponse.builder()
                .status(200)
                .message("Message edited successfully.")
                .build()
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> deleteMessage(@RequestParam(value = "messageId") String messageId) {
        return ResponseEntity.ok(messageService.deleteChannelMessage(MessageDeleteRequest.builder().messageId(messageId).build()));
    }
}
