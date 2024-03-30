package com.example.back.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<IsEmail, String> {

    private static final Pattern EMAIL_REGEX_PATTERN = Pattern.compile(
            "^([a-zA-Z0-9_\\-\\.]+)@((\\[a-zA-Z0-9\\-\\_\\.]+)|([a-zA-Z0-9\\-\\.]+))\\.([a-zA-Z]{2,5})$"
    );

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email == null || EMAIL_REGEX_PATTERN.matcher(email).matches();
    }
}
