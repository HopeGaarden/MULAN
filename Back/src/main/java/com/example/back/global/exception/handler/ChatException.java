package com.example.back.global.exception.handler;

import com.example.back.global.exception.BlueRoseException;
import com.example.back.global.exception.ExceptionMessage;

public class ChatException extends BlueRoseException {
    public ChatException(ExceptionMessage message) {
        super(message.getText());
    }
}