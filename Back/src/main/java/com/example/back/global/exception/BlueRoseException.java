package com.example.back.global.exception;

public abstract class BlueRoseException extends RuntimeException {
    public BlueRoseException(String message) {
        super(message);
    }
}
