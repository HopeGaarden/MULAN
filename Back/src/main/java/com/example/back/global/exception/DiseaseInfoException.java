package com.example.back.global.exception;

public class DiseaseInfoException extends BlueRoseException {
    public DiseaseInfoException(ExceptionMessage message) {
        super(message.getText());
    }
}