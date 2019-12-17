package com.acme.persistence;

import com.acme.domain.platform.Platform;

import java.util.List;
import java.util.Optional;

public interface PlatformRepository {

    List<Platform> allPlatforms();

    List<String> allPlatformsNames();

    Optional<Platform> platform(String platformName);

    String savePlatform(Platform platform);

    void deletePlatform(String platformName);

    void deleteAllPlatforms();

}
