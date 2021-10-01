package pro.inmost.amazon.chime.model.validation.validator;

import pro.inmost.amazon.chime.model.dto.UserDTO;
import pro.inmost.amazon.chime.model.validation.PasswordMatches;
import pro.inmost.amazon.chime.model.dto.NewPasswordForm;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {

        if (obj instanceof UserDTO) {
           UserDTO user = (UserDTO) obj;
           return user.getPassword().equals(user.getMatchingPassword());
        }

        return false;
    }
}
