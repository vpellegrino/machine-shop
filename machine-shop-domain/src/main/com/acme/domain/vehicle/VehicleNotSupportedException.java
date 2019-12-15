package com.acme.domain.vehicle;

public class VehicleNotSupportedException extends RuntimeException {

    public VehicleNotSupportedException(String errorMessage) {
        super(errorMessage);
    }

    public VehicleNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
