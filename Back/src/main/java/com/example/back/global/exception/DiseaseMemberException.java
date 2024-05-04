package com.example.back.global.exception;

public class DiseaseMemberException extends BlueRoseException {
    public DiseaseMemberException(ExceptionMessage message) {
        super(message.getText());
    }
}