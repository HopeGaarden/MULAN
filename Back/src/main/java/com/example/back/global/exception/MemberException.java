package com.example.back.global.exception;

public class MemberException extends BlueRoseException {
    public MemberException(ExceptionMessage message) {
        super(message.getText());
    }
}
