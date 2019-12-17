package com.acme.domain.vehicle;

public class VehicleNotSupportedException extends RuntimeException {

    public VehicleNotSupportedException() {
        super("Vehicle type cannot be found - an issue occurred");
    }

    public VehicleNotSupportedException(String errorMessage) {
        super(errorMessage);
    }

    public VehicleNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
