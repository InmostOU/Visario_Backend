package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;



@Data

@Builder
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private long birthdayInMilliseconds;
    private String username;
    private String password;
    private String matchingPassword;
    private String email;
    private boolean isActive;
}
