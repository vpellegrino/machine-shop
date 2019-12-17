package com.acme.menu.operation;

import com.acme.domain.platform.Platform;
import com.acme.domain.platform.PlatformConfiguration;
import com.acme.domain.platform.PlatformNotFoundException;
import com.acme.domain.repair.RepairType;
import com.acme.domain.repair.RepairTypeNotSupportedException;
import com.acme.domain.vehicle.Vehicle;
import com.acme.domain.vehicle.VehicleNotSupportedException;
import com.acme.menu.MenuItem;
import com.acme.persistence.PlatformRepository;
import org.beryx.textio.TextIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddRepairOrderOperation implements MenuItemOperation {

    private static final Logger logger = LoggerFactory.getLogger(AddRepairOrderOperation.class);

    @Override
    public MenuItem.NextAction execute(TextIO textIO, PlatformRepository platformRepository) {
        logger.debug("Add Repair Order Operation has been selected");

        String platformName = textIO.newStringInputReader()
                .withNumberedPossibleValues(platformRepository.allPlatformsNames())
                .read("Platform name");

        Platform platform = platformRepository.platform(platformName).orElseThrow(PlatformNotFoundException::new);
        PlatformConfiguration platformConfiguration = platform.getPlatformConfiguration();
        double hoursNeededForCompletingRepair = platform.newRepairOrder(
                configureVehicle(textIO, platformConfiguration),
                scheduleRepairs(textIO, platformConfiguration)
        );
        platformRepository.savePlatform(platform);

        textIO.getTextTerminal().println(
                String.format("Estimated number of hours for completing such repair (plus waiting time) is %.2f", hoursNeededForCompletingRepair)
        );

        return MenuItem.NextAction.PRINCIPAL_MENU;
    }

    private List<RepairType> scheduleRepairs(TextIO textIO, PlatformConfiguration platformConfiguration) {
        List<RepairType> repairsToBeExecuted = new ArrayList<>();

        int numberOfRepairs = textIO.newIntInputReader()
                .withDefaultValue(1)
                .withMinVal(1)
                .read("Number of repairs to be executed:");

        for (int i = 0; i < numberOfRepairs; i++) {
            RepairType repairType = configureRepair(textIO, platformConfiguration);
            repairsToBeExecuted.add(repairType);
        }
        return repairsToBeExecuted;
    }

    private RepairType configureRepair(TextIO textIO, PlatformConfiguration platformConfiguration) {
        String repairTypeName = textIO.newStringInputReader()
                .withNumberedPossibleValues(repairTypes(platformConfiguration))
                .read("Repair type:");

        return platformConfiguration.getSupportedRepairTypes().stream()
                .filter(repairType -> repairTypeName.equals(repairType.getName()))
                .findAny()
                .orElseThrow(RepairTypeNotSupportedException::new);
    }

    private List<String> repairTypes(PlatformConfiguration platformConfiguration) {
        return platformConfiguration
                .getSupportedRepairTypes().stream()
                .map(RepairType::getName)
                .collect(Collectors.toList());
    }

    private Vehicle configureVehicle(TextIO textIO, PlatformConfiguration platformConfiguration) {
        String vehicleTypeName = textIO.newStringInputReader()
                .withNumberedPossibleValues(vehicleTypes(platformConfiguration))
                .read("Vehicle type:");

        String vehicleLicensePlate = textIO.newStringInputReader()
                .withMinLength(5)
                .withMaxLength(7)
                .read("Vehicle license plate:");
        return new Vehicle(vehicleTypeByName(platformConfiguration, vehicleTypeName), vehicleLicensePlate);
    }

    private Vehicle.Type vehicleTypeByName(PlatformConfiguration platformConfiguration, String vehicleTypeName) {
        return platformConfiguration.getSupportedVehicles().stream()
                .filter(vehicleType -> vehicleTypeName.equals(vehicleType.name()))
                .findAny()
                .orElseThrow(VehicleNotSupportedException::new);
    }

    private List<String> vehicleTypes(PlatformConfiguration platformConfiguration) {
        return platformConfiguration.getSupportedVehicles().stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }

}
