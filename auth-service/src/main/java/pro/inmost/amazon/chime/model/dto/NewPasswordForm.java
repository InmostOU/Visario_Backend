package pro.inmost.amazon.chime.model.dto;

import lombok.Data;
import pro.inmost.amazon.chime.model.validation.PasswordMatches;
import javax.validation.constraints.NotEmpty;

@Data
@PasswordMatches
public class NewPasswordForm {
    @NotEmpty
    private String password;

    @NotEmpty
    private String matchingPassword;
}
