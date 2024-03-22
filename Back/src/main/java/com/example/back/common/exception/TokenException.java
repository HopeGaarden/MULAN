package com.example.back.common.exception;

public class TokenException extends BlueRoseException {

    public TokenException(ExceptionMessage message) {
        super(message.getText());
    }
}
