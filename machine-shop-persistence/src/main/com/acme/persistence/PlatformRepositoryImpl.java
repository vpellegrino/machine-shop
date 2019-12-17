package com.acme.persistence;

import com.acme.domain.platform.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlatformRepositoryImpl implements PlatformRepository {

    private static final PlatformRepositoryImpl singleInstance = new PlatformRepositoryImpl();
    private List<Platform> platforms; // persistence in memory, as long as application runs

    private PlatformRepositoryImpl() {
        platforms = new ArrayList<>();
    }

    public static PlatformRepositoryImpl getInstance() {
        return singleInstance;
    }

    @Override
    public List<Platform> allPlatforms() {
        return new ArrayList<>(platforms);
    }

    @Override
    public List<String> allPlatformsNames() {
        return platforms.stream().map(Platform::getName).collect(Collectors.toList());
    }

    @Override
    public Optional<Platform> platform(String platformName) {
        return platforms.stream()
                .filter(platformByNamePredicate(platformName))
                .findAny();
    }

    @Override
    public String savePlatform(Platform platform) {
        deletePlatform(platform.getName());

        platforms.add(platform);

        return platform.getName();
    }

    @Override
    public void deletePlatform(String platformName) {
        platforms.removeIf(platformByNamePredicate(platformName));
    }

    @Override
    public void deleteAllPlatforms() {
        platforms.clear();
    }

    private Predicate<Platform> platformByNamePredicate(String platformName) {
        return platform -> platformName.equals(platform.getName());
    }

}
