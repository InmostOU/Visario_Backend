package pro.inmost.amazon.chime.model.dto;

import com.amazonaws.services.chime.model.Attendee;
import com.amazonaws.services.chime.model.Meeting;
import lombok.Data;

@Data
public class MeetingDTO {
    private Meeting meetingObject;
    private Attendee attendeeObject;
}