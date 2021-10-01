package pro.inmost.amazon.chime.model.validation;

import pro.inmost.amazon.chime.model.validation.validator.LettersValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LettersValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Letters {
    String message() default "Only letters are allowed here.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
