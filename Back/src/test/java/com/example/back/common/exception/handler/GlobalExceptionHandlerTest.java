package com.example.back.common.exception.handler;

import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.MemberException;
import com.example.back.common.response.JsonResult;
import com.example.back.global.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("BindException 발생시 GlobalExceptionHandler에서 처리된다.")
    void bindExceptionTest() {
        // given
        BindException bindException = new BindException(new Object(), "objectName");
        bindException.addError(new FieldError("objectName", "fieldName", "rejectedValue", false, null, null, "error message"));

        String expectedResponseMessage = "fieldName: error message";

        // when
        JsonResult result = globalExceptionHandler.bindException(bindException);

        // then
        assertThat(result.getResCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getResMsg()).isEqualTo(expectedResponseMessage);

    }

    @Test
    @DisplayName("MethodArgumentNotValidException 발생시 GlobalExceptionHandler에서 처리된다.")
    void methodArgumentNotValidExceptionTest() {
        // given
        BindingResult bindingResult = new BindException(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "fieldName", "rejectedValue", false, null, null, "error message"));
        MethodArgumentNotValidException error = new MethodArgumentNotValidException(null, bindingResult);

        String expectedResponseMessage = "fieldName: error message";

        // when
        JsonResult result = globalExceptionHandler.handleMethodArgumentNotValid(error);

        // then
        assertThat(result.getResCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getResMsg()).isEqualTo(expectedResponseMessage);

    }

    @Test
    @DisplayName("Exception 발생시 GlobalExceptionHandler에서 처리된다.")
    void exceptionTest() {
        // given
        MemberException exception = new MemberException(ExceptionMessage.MEMBER_ROLE_NOT_FOUND);

        // when
        JsonResult result = globalExceptionHandler.exception(exception);

        // then
        assertThat(result.getResCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getResMsg()).isEqualTo(ExceptionMessage.MEMBER_ROLE_NOT_FOUND.getText());
    }
}