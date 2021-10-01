package pro.inmost.amazon.chime.model.validation.validator;

import pro.inmost.amazon.chime.model.validation.Username;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<Username, String> {

    @Override
    public void initialize(Username constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext constraintValidatorContext) {

        return contactField != null && containsOnlyLettersAndDigits(contactField)
                && (contactField.length() > 1) && (contactField.length() < 100);
    }

    private boolean containsOnlyLettersAndDigits(String username) {
       return (username != null) && username.matches("[A-Za-z0-9-.]+");
    }


}
