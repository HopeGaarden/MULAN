package com.example.back.global.exception;

public class TagInfoException extends BlueRoseException{
    public TagInfoException(ExceptionMessage message) { super(message.getText()); }
}
