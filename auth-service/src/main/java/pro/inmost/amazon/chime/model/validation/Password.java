package pro.inmost.amazon.chime.model.validation;

import pro.inmost.amazon.chime.model.validation.validator.PasswordValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Password must contains at least: 6 characters, 1 digit, 1 specific symbol.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
