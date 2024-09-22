package com.goldentalk.gt.exception;

public class LowPaymentException extends RuntimeException {
    public LowPaymentException(String message) {
        super(message);
    }
}
