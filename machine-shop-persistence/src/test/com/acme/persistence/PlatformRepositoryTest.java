package com.acme.persistence;

import com.acme.domain.platform.Platform;
import com.acme.domain.platform.PlatformConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class PlatformRepositoryTest {

    private static final String A_PLATFORM = "a platform";
    private PlatformRepository platformRepository;

    @Before
    public void setUp() {
        platformRepository = PlatformRepositoryImpl.getInstance();
    }

    @After
    public void tearDown() {
        platformRepository.deleteAllPlatforms();
    }

    @Test
    public void givenOnePlatform_whenGettingAllPlatforms_thenOnlyOneReturned() {
        createPlatform(A_PLATFORM);

        assertThat(platformRepository.allPlatforms()).isNotNull();
        assertThat(platformRepository.allPlatforms()).isNotEmpty();
        assertThat(platformRepository.allPlatforms()).hasSize(1);
    }

    @Test
    public void givenNoPlatforms_whenGettingAllPlatforms_thenEmptyListReturned() {
        assertThat(platformRepository.allPlatforms()).isNotNull();
        assertThat(platformRepository.allPlatforms()).isEmpty();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void givenOnePlatform_whenGettingIt_thenGetsReturned() {
        createPlatform(A_PLATFORM);

        assertThat(platformRepository.platform(A_PLATFORM)).isNotEmpty();
        assertThat(platformRepository.platform(A_PLATFORM)).hasValueSatisfying(platform -> platform.getName().equals(A_PLATFORM));
    }

    @Test
    public void givenNoPlatforms_whenGettingSpecificPlatform_thenNothingReturned() {
        assertThat(platformRepository.platform(A_PLATFORM)).isEmpty();
    }

    @Test
    public void whenSavingAPlatform_thenCorrectlySaved() {
        PlatformConfiguration platformConfiguration = new PlatformConfiguration(Collections.emptyList(), Collections.emptyList());
        String savedPlatformName = platformRepository.savePlatform(new Platform(A_PLATFORM, platformConfiguration));

        assertThat(savedPlatformName).isEqualTo(A_PLATFORM);
        assertThat(platformRepository.allPlatforms()).isNotEmpty();
        assertThat(platformRepository.allPlatforms()).hasSize(1);
    }

    @Test
    public void whenSavingAPlatformWhichAlreadyExists_thenItGetsReplaced() {
        PlatformConfiguration platformConfiguration = createPlatform(A_PLATFORM);
        String savedPlatformName = platformRepository.savePlatform(new Platform(A_PLATFORM, platformConfiguration));

        assertThat(savedPlatformName).isEqualTo(A_PLATFORM);
        assertThat(platformRepository.allPlatforms()).isNotEmpty();
        assertThat(platformRepository.allPlatforms()).hasSize(1);
    }

    @Test
    public void givenNoPlatform_whenDeletingSpecificPlatform_thenNothingHappens() {
        assertThat(platformRepository.allPlatforms()).isEmpty();
        platformRepository.deletePlatform("not existing platform");
        assertThat(platformRepository.allPlatforms()).isEmpty();
    }

    @Test
    public void givenOnePlatform_whenDeletingIt_thenNoPlatformsAreMorePresent() {
        createPlatform(A_PLATFORM);

        platformRepository.deletePlatform(A_PLATFORM);
        assertThat(platformRepository.allPlatforms()).isEmpty();
    }

    @Test
    public void givenTwoPlatforms_whenDeletingOneOfThem_thenOnlyOnePlatformIsPresent() {
        createPlatform(A_PLATFORM);
        createPlatform("another platform");

        platformRepository.deletePlatform(A_PLATFORM);
        assertThat(platformRepository.allPlatforms()).isNotEmpty();
        assertThat(platformRepository.allPlatforms()).hasSize(1);
    }

    @Test
    public void givenTwoPlatforms_whenDeletingAllOfThem_thenNoPlatformsAreMorePresent() {
        createPlatform(A_PLATFORM);
        createPlatform("another platform");

        platformRepository.deleteAllPlatforms();
        assertThat(platformRepository.allPlatforms()).isEmpty();
    }

    @Test
    public void givenNoPlatforms_whenDeletingAllOfThem_thenNothingHappens() {
        platformRepository.deleteAllPlatforms();
        assertThat(platformRepository.allPlatforms()).isEmpty();
    }

    private PlatformConfiguration createPlatform(String platformName) {
        PlatformConfiguration platformConfiguration = new PlatformConfiguration(Collections.emptyList(), Collections.emptyList());
        platformRepository.savePlatform(new Platform(platformName, platformConfiguration));
        return platformConfiguration;
    }

}
