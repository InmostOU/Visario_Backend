package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactDetail {
    private Long id;
    private String userArn;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private String image;
    private String about;
    private Boolean online;
    private Boolean favorite;
    private Boolean muted;
    private Boolean inMyContacts;
}
