package pro.inmost.amazon.chime.model.entity;

import com.amazonaws.services.chime.model.Meeting;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
public class MeetingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String meetingId;
    private Meeting meeting;
    private Date createDate;
    private Long userId;
}
