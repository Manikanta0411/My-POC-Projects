package com.record.exception;

public class RecordException extends RuntimeException {

    private String message;

    public RecordException(String message) {
        this.message = message;
    }
}
