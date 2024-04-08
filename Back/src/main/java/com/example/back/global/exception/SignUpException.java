package com.example.back.global.exception;

public class SignUpException extends BlueRoseException {
    public SignUpException(ExceptionMessage message) {
        super(message.getText());
    }
}