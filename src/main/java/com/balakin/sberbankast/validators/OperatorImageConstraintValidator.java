package com.balakin.sberbankast.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OperatorImageConstraintValidator implements ConstraintValidator<OperatorImageConstraint,Byte[]> {
    @Override
    public boolean isValid(Byte[] file, ConstraintValidatorContext constraintValidatorContext) {
        return file!=null ;
    }
}