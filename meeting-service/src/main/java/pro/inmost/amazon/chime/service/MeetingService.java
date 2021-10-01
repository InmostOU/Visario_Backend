package pro.inmost.amazon.chime.service;

import pro.inmost.amazon.chime.exceptions.MeetingNotFoundException;
import pro.inmost.amazon.chime.model.dto.MeetingAndUserIdDTO;
import pro.inmost.amazon.chime.model.dto.MeetingDTO;
import pro.inmost.amazon.chime.model.dto.UserInfoForMeeting;
import software.amazon.awssdk.services.chime.model.NotFoundException;

public interface MeetingService {

    MeetingDTO getMeeting(String meetingId) throws MeetingNotFoundException;

    void deleteMeeting(String meetingId);

    void deleteAttendee(MeetingAndUserIdDTO meetingAndUserIdDTO) throws NotFoundException, MeetingNotFoundException;

    UserInfoForMeeting getUserInfoForMeetingByUserId(MeetingAndUserIdDTO meetingAndUserIdDTO) throws MeetingNotFoundException;

    MeetingDTO createNewMeetingInfo();
}

