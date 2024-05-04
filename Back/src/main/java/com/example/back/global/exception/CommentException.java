package com.example.back.global.exception;

public class CommentException extends BlueRoseException {
    public CommentException(ExceptionMessage message) {
        super(message.getText());
    }
}
