package com.acme.domain.platform;

public class PlatformNotFoundException extends RuntimeException {

    public static final String PROVIDED_PLATFORM_DOES_NOT_EXISTS = "Provided platform does not exists";

    public PlatformNotFoundException() {
        super("Platform cannot be found - an issue occurred");
    }
}
