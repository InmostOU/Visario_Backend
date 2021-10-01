package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberDetail {
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
    private Boolean inMyContacts;
    private Boolean isAdmin;
    private Boolean isMod;
}
