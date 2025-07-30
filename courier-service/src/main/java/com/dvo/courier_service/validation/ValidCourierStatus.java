package com.dvo.courier_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CourierStatusValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCourierStatus {
    String message() default "Courier status must be either AVAILABLE or UNAVAILABLE";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
