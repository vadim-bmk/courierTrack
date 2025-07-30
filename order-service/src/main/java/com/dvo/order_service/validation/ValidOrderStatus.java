package com.dvo.order_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OrderStatusValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrderStatus {
    String message() default "OrderStatus type must be either NEW, PROCESSING, COMPLETED or CANCELED";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
