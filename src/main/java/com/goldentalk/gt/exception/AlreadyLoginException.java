package com.goldentalk.gt.exception;

public class AlreadyLoginException extends RuntimeException {

    public AlreadyLoginException(String message) {
        super(message);
    }
}
