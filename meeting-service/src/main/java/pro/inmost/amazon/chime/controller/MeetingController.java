package pro.inmost.amazon.chime.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.inmost.amazon.chime.exceptions.MeetingNotFoundException;
import pro.inmost.amazon.chime.model.dto.MeetingAndUserIdDTO;
import pro.inmost.amazon.chime.model.dto.MeetingDTO;
import pro.inmost.amazon.chime.model.dto.UserInfoForMeeting;
import pro.inmost.amazon.chime.service.MeetingService;
import software.amazon.awssdk.services.chime.model.NotFoundException;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/meeting")
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping("/create")
    public ResponseEntity<MeetingDTO> createMeeting() {
        return ResponseEntity.ok(meetingService.createNewMeetingInfo());
    }

    @GetMapping("/getMeeting")
    public ResponseEntity<MeetingDTO> getMeeting(@RequestParam String meetingId) throws MeetingNotFoundException {
        return ResponseEntity.ok(meetingService.getMeeting(meetingId));
    }

    @GetMapping("/deleteMeeting")
    public ResponseEntity<Void> deleteMeeting(@RequestParam String meetingId) {
        meetingService.deleteMeeting(meetingId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deleteAttendee")
    public ResponseEntity<Void> deleteAttendee(@RequestBody MeetingAndUserIdDTO meetingAndUserIdDTO) throws NotFoundException, MeetingNotFoundException {
        meetingService.deleteAttendee(meetingAndUserIdDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/getUserInfoByUserId")
    public UserInfoForMeeting getUserInfoForMeetingByUserId(@RequestBody MeetingAndUserIdDTO meetingAndUserIdDTO) throws MeetingNotFoundException {
        return meetingService.getUserInfoForMeetingByUserId(meetingAndUserIdDTO);
    }
}

