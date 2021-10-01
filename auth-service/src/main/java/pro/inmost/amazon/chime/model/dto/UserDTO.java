package pro.inmost.amazon.chime.model.dto;

import lombok.Builder;
import lombok.Data;
import pro.inmost.amazon.chime.model.validation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
@Builder
public class UserDTO {

    private Long id;

    @NotEmpty(message = "Please provide first name")
    @Letters
    @Size(max = 100)
    private String firstName;

    @Letters
    @Size(max = 100)
    private String lastName;

    @NotEmpty(message = "Please provide username")
    @Username
    @Size(max = 100)
    private String username;

    @NotEmpty(message = "Please provide password")
    @Password
    @Size(max = 100)
    private String password;

    @NotEmpty(message = "Please provide password match")
    @Password
    @Size(max = 100)
    private String matchingPassword;

    @Size(max = 255)
    @ValidEmail
    private String email;

    private Long birthdayInMilliseconds;

    private boolean isActive;
}
