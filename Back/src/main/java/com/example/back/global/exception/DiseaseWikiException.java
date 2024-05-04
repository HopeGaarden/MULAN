package com.example.back.global.exception;

public class DiseaseWikiException extends BlueRoseException {
    public DiseaseWikiException(ExceptionMessage message) {
        super(message.getText());
    }
}