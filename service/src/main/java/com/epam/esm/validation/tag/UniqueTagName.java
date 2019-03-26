package com.epam.esm.validation.tag;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueTagNameValidator.class)
public @interface UniqueTagName {

    String message() default "tag.name.exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
