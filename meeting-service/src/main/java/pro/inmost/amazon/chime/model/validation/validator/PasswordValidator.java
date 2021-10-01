package pro.inmost.amazon.chime.model.validation.validator;

import pro.inmost.amazon.chime.model.validation.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9\\\\s]).{6,}";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return (validateEmail(email));
    }

    private boolean validateEmail(String email) {
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
