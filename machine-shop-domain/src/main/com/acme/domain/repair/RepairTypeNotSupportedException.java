package com.acme.domain.repair;

public class RepairTypeNotSupportedException extends RuntimeException {

    public RepairTypeNotSupportedException(String errorMessage) {
        super(errorMessage);
    }

    public RepairTypeNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
