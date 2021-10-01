package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class UserInfoForMeeting {
    private Long id;
    private String firstName;
    private String lastName;
    private String image;
}
