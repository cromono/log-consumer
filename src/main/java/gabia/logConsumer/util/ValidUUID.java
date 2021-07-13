package gabia.logConsumer.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Constraint(validatedBy = gabia.logConsumer.util.UuidValidator.class)
public @interface ValidUUID {

    String message() default "UUID has wrong format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
