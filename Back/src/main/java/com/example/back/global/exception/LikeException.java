package com.example.back.global.exception;

public class LikeException extends BlueRoseException{
    public LikeException(ExceptionMessage message) {
        super(message.getText());
    }
}
