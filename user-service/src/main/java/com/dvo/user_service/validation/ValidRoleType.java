package com.dvo.user_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleTypeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRoleType {
    String message() default "Role type must be either USER or ADMIN";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
