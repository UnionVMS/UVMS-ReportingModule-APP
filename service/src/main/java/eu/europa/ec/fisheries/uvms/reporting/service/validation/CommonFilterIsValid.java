package eu.europa.ec.fisheries.uvms.reporting.service.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = CommonFilterValidator.class)
public @interface CommonFilterIsValid {

    String message() default "CommonFilter is not valid";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
