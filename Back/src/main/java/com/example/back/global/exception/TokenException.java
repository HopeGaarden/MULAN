package com.example.back.global.exception;

public class TokenException extends BlueRoseException {

    public TokenException(ExceptionMessage message) {
        super(message.getText());
    }
}
