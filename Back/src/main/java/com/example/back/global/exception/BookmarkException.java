package com.example.back.global.exception;

public class BookmarkException extends BlueRoseException{
    public BookmarkException(ExceptionMessage message) { super(message.getText()); }
}
