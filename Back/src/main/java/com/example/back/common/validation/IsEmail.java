package com.example.back.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface IsEmail {
    String message() default "잘못된 형식의 이메일입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
