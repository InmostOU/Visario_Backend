package pro.inmost.amazon.chime.model.validation.validator;

import pro.inmost.amazon.chime.model.validation.Letters;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LettersValidator implements ConstraintValidator<Letters, String> {

    @Override
    public void initialize(Letters constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext constraintValidatorContext) {

        return contactField != null && containsOnlyLetters(contactField)
                && (contactField.length() > 1) && (contactField.length() < 100);
    }

    private boolean containsOnlyLetters(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isLetter(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
