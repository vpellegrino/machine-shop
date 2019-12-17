package com.acme.menu.operation;

import com.acme.domain.platform.Platform;
import com.acme.domain.platform.PlatformConfiguration;
import com.acme.domain.vehicle.Vehicle;
import com.acme.menu.MenuItem;
import com.acme.menu.MenuItem.NextAction;
import com.acme.menu.configuration.ConfigurationMenuItem;
import com.acme.persistence.PlatformRepository;
import org.beryx.textio.TextIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlatformOperation implements MenuItemOperation {

    private static final Logger logger = LoggerFactory.getLogger(PlatformOperation.class);
    static final String PLATFORM_NAME = "Platform name";

    private ConfigurationMenuItem.ConfigOperation configOperation;

    public PlatformOperation(ConfigurationMenuItem.ConfigOperation configOperation) {
        this.configOperation = configOperation;
    }

    @Override
    public MenuItem.NextAction execute(TextIO textIO, PlatformRepository platformRepository) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Platform Operation (%s) has been selected", configOperation));
        }

        switch (configOperation) {
            case CREATE:
                createPlatform(textIO, platformRepository);
                break;
            case GET_ALL:
                printAllPlatforms(textIO, platformRepository);
                break;
            case DELETE:
                deletePlatform(textIO, platformRepository);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + configOperation);
        }

        return NextAction.CONFIGURATION_MENU;
    }

    private void createPlatform(TextIO textIO, PlatformRepository platformRepository) {
        String platformName = textIO.newStringInputReader()
                .withNumberedPossibleValues(platformRepository.allPlatformsNames())
                .read(PLATFORM_NAME);

        Platform platformToCreate = new Platform(platformName, configurePlatform(textIO));

        platformRepository.savePlatform(platformToCreate);
        textIO.getTextTerminal().println(String.format("Creation of %s confirmed - Platform created", platformName));
    }

    private PlatformConfiguration configurePlatform(TextIO textIO) {
        PlatformConfiguration platformConfiguration = new PlatformConfiguration(new ArrayList<>(), new ArrayList<>());
        configureSupportedVehicleTypes(textIO, platformConfiguration);
        configureSupportedRepairTypes(textIO, platformConfiguration);
        return platformConfiguration;
    }

    private void configureSupportedRepairTypes(TextIO textIO, PlatformConfiguration platformConfiguration) {
        int numberOfRepairTypes = textIO.newIntInputReader()
                .withDefaultValue(1)
                .withMinVal(1)
                .read("Number of repair types supported by this platform:");
        for (int i = 0; i < numberOfRepairTypes; i++) {
            String repairTypeName = textIO.newStringInputReader()
                    .withMinLength(3)
                    .withMaxLength(1000)
                    .read("Short description for the repair:");
            double hoursNeededToComplete = textIO.newDoubleInputReader()
                    .withMinVal(0.25)
                    .withDefaultValue(1.0)
                    .read("Hours Needed to complete the repair:");
            platformConfiguration.addSupportedRepairType(repairTypeName, hoursNeededToComplete);
        }
    }

    private void configureSupportedVehicleTypes(TextIO textIO, PlatformConfiguration platformConfiguration) {
        int numberOfVehicles = textIO.newIntInputReader()
                .withDefaultValue(1)
                .withMinVal(1)
                .withMaxVal(Vehicle.Type.values().length)
                .read("Number of vehicle supported by this platform:");
        for (int i = 0; i < numberOfVehicles; i++) {
            Vehicle.Type vehicleType = textIO.newEnumInputReader(Vehicle.Type.class)
                    .read("Select vehicle type supported by this platform");
            platformConfiguration.addSupportedVehicle(vehicleType);
        }
    }

    private void printAllPlatforms(TextIO textIO, PlatformRepository platformRepository) {
        List<String> platformNames = platformRepository.allPlatformsNames();

        if (platformNames.isEmpty()) {
            textIO.getTextTerminal().println("No platform is present - Nothing changed");
        }

        textIO.getTextTerminal().println(
                platformNames.stream()
                        .map("- "::concat)
                        .collect(Collectors.toList())
        );
    }

    private void deletePlatform(TextIO textIO, PlatformRepository platformRepository) {
        String platformName = textIO.newStringInputReader()
                .withNumberedPossibleValues(platformRepository.allPlatformsNames())
                .read(PLATFORM_NAME);

        boolean deleteConfirmed = textIO.newBooleanInputReader()
                .withDefaultValue(true)
                .read(String.format("Are you sure do you want to delete the platform %s?", platformName));

        if (deleteConfirmed) {
            platformRepository.deletePlatform(platformName);
            textIO.getTextTerminal().println("Delete confirmed - Platform removed");
        } else {
            textIO.getTextTerminal().println("Delete operation aborted - Nothing changed");
        }
    }

}
