package com.epam.esm.validation.certificate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueCertificateNameValidator.class)
public @interface UniqueCertificateName {
    String message() default "certificate.name.exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
