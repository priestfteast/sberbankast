package com.balakin.sberbankast.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OperatorLastNameConstraintValidator implements ConstraintValidator<OperatorLastNameConstraint, String> {
    @Override
    public boolean isValid(String lastName, ConstraintValidatorContext constraintValidatorContext) {
        return lastName.endsWith("n") ;
    }
}