package com.example.demo33.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<isNoRussianPhones, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
        value = value.replaceAll("[^\\d ]", " ");
        value = value.replaceAll( " {2,}", " ");
        String[] msg = value.split(" ");
        for(String val: msg) {
            Matcher matcher = pattern.matcher(val);
            try {
                if (matcher.matches()) {
                    return false;
                }
            } catch (Exception e) {
                return true;
            }
        }
        return true;
    }
}