package com.example.back.global.exception;

public class FeedInfoException extends BlueRoseException{
    public FeedInfoException(ExceptionMessage message) {
        super(message.getText());
    }
}
