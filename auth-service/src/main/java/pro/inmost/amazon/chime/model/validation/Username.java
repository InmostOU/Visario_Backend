package pro.inmost.amazon.chime.model.validation;

import pro.inmost.amazon.chime.model.validation.validator.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {
    String message() default "Username must contains only latin letters, \".-\" symbols and digits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
