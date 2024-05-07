package com.example.back.global.exception.handler;

import com.example.back.common.response.JsonResult;
import com.example.back.global.exception.DiseaseWikiException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
        데이터 바인딩 중 발생하는 에러 BindException 처리
     */
    @ExceptionHandler(BindException.class)
    public JsonResult bindException(BindException e) {
        String errorMessage = getErrorMessage(e.getBindingResult());

        return JsonResult.failOf(errorMessage);
    }

    /*
        @Valid 어노테이션을 사용한 DTO의 유효성 검사에서 예외가 발생한 경우
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String errorMessage = getErrorMessage(e.getBindingResult());

        return JsonResult.failOf(errorMessage);
    }

    // 낙관적 락에 의해 동시성 충돌이 발생해 ObjectOptimisticLockingFailureException 예외가 터졌을 경우
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public JsonResult handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException e) {

        return JsonResult.failOf(e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public JsonResult<Exception> exception(Exception e) {
        return JsonResult.failOf(e.getMessage());
    }

    private static String getErrorMessage(BindingResult e) {
        return e
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}