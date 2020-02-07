package com.balakin.sberbankast.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=OperatorImageConstraintValidator.class)
public @interface OperatorImageConstraint {
    String message() default "{image}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
