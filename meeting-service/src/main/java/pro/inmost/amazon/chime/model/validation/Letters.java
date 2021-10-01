package pro.inmost.amazon.chime.model.validation;

import pro.inmost.amazon.chime.model.validation.validator.LettersValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = LettersValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Letters {
    String message() default "Only letters are allowed here.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
