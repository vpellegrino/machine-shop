package com.acme.domain.platform;

public class PlatformNotFoundException extends RuntimeException {

    public PlatformNotFoundException() {
        super("Platform cannot be found - an issue occurred");
    }
}
