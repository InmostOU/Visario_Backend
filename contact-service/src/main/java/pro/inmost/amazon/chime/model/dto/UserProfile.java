package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfile {
    private Long id;
    private String userArn;
    private String firstName;
    private String lastName;
    private String username;
    private Long birthday;
    private String email;
    private String phoneNumber;
    private String image;
    private String about;
    private String showEmailTo;
    private String showPhoneNumberTo;
}
