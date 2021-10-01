package pro.inmost.amazon.chime.controllers;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.inmost.amazon.chime.enums.ChannelMode;
import pro.inmost.amazon.chime.enums.ChannelPrivacy;
import pro.inmost.amazon.chime.model.dto.*;
import pro.inmost.amazon.chime.model.dto.ChannelSummary;
import pro.inmost.amazon.chime.model.dto.CreateChannelRequest;
import pro.inmost.amazon.chime.service.ChannelService;
import pro.inmost.amazon.chime.service.ConversationService;
import software.amazon.awssdk.services.chime.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/channels")
public class ChannelController {

    private final ChannelService channelService;
    private final ConversationService conversationService;

    @PostMapping("/create")
    @SneakyThrows
    public ResponseEntity<ChannelCreateResponse> create(@RequestBody CreateChannelRequest request) {
          ChannelPrivacy privacy = request.getPrivacy().equals("PUBLIC") ? ChannelPrivacy.PUBLIC : ChannelPrivacy.PRIVATE;
          ChannelMode mode = request.getMode().equals("UNRESTRICTED") ? ChannelMode.UNRESTRICTED : ChannelMode.RESTRICTED;

          if (privacy == ChannelPrivacy.PUBLIC)
                    mode = ChannelMode.RESTRICTED;

          return ResponseEntity.ok(
                channelService.createChannel(
                        false,
                        request.getMetadata(),
                        mode,
                        request.getName(),
                        privacy,
                        request.getDescription(),
                        Tag.builder().key("key").value("value")
                        .build()
                )
          );
    }

    @GetMapping("/getChannelByChannelArn")
    public ResponseEntity<ChannelSummaries> getChannelByArn(@RequestParam(value = "arn") String channelArn) {

        List<ChannelSummary> summaries = Arrays.asList(channelService.getChannelByChannelArn(channelArn));

        return ResponseEntity.ok(
                ChannelSummaries.builder()
                        .data(summaries)
                        .message("Channel fetched successfully")
                        .status(200)
                        .build()
        );
    }

    @PostMapping("/create/conversation")
    @SneakyThrows
    public ResponseEntity<BaseResponse> createConversation(@RequestBody CreateConversationRequest request) {
        return ResponseEntity.ok(conversationService.createConversation(request));
    }

    @PostMapping("/addMemberToChannel")
    public ResponseEntity<BaseResponse> inviteUserToChannel(@RequestBody InviteUserRequest invite) {
        return ResponseEntity.ok(channelService.createChannelMemberShip(false, invite.getChannelArn(), invite.getMemberArn()));
    }

    @GetMapping("/getChannelsMemberships")
    public ResponseEntity<ChannelSummaries> getChannelsMemberships() {
        return ResponseEntity.ok(channelService.listChannelMembershipsForLoggedAppInstanceUser());
    }

    @GetMapping("/getConversations")
    public ResponseEntity<ConversationSummaries> getConversations() {
        return ResponseEntity.ok(conversationService.fetchConversations());
    }

    @GetMapping("/findByName")
    public ResponseEntity<ChannelSummaries> findChannelsByName(@RequestParam String channelName) {
        return ResponseEntity.ok(channelService.findChannelsByName(channelName));
    }

    @SneakyThrows
    @GetMapping("/leaveChannel")
    public ResponseEntity<BaseResponse> leaveChannel(@RequestParam String channelArn) {
        return ResponseEntity.ok(channelService.leaveChannel(channelArn));
    }

    @GetMapping("/fetchUsersFromChannel")
    public ResponseEntity<MemberDetails> fetchUsersFromChannel(@RequestParam String channelArn) {
        return ResponseEntity.ok(channelService.fetchUserListFromChannel(channelArn));
    }
}
