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

import static com.acme.menu.operation.PlatformOperation.PLATFORM_NAME;

public class AddRepairOrderOperation implements MenuItemOperation {

    private static final Logger logger = LoggerFactory.getLogger(AddRepairOrderOperation.class);
    static final String NUMBER_OF_REPAIRS_TO_BE_EXECUTED = "Number of repairs to be executed:";
    static final String REPAIR_TYPE = "Repair type:";
    static final String VEHICLE_TYPE = "Vehicle type:";
    static final String VEHICLE_LICENSE_PLATE = "Vehicle license plate:";

    @Override
    public MenuItem.NextAction execute(TextIO textIO, PlatformRepository platformRepository) {
        logger.debug("Add Repair Order Operation has been selected");

        String platformName = textIO.newStringInputReader()
                .withNumberedPossibleValues(platformRepository.allPlatformsNames())
                .read(PLATFORM_NAME);
        Platform platform;
        try {
            platform = platformRepository.platform(platformName).orElseThrow(PlatformNotFoundException::new);
        } catch (PlatformNotFoundException e) {
            String errorMsg = "Provided platform does not exists";
            return somethingNotConfiguredWell(errorMsg, textIO, e);
        }
        PlatformConfiguration platformConfiguration = platform.getPlatformConfiguration();
        double hoursNeededForCompletingRepair;

        try {
            hoursNeededForCompletingRepair = platform.newRepairOrder(
                    configureVehicle(textIO, platformConfiguration),
                    scheduleRepairs(textIO, platformConfiguration)
            );
        } catch (VehicleNotSupportedException e) {
            String errorMsg = "Provided vehicle is not present in the platform configuration";
            return somethingNotConfiguredWell(errorMsg, textIO, e);
        } catch (RepairTypeNotSupportedException e) {
            String errorMsg = "Provided repair type is not present in the platform configuration";
            return somethingNotConfiguredWell(errorMsg, textIO, e);
        }
        platformRepository.savePlatform(platform);

        textIO.getTextTerminal().println(
                String.format("Estimated number of hours for completing such repair (plus waiting time) is %.2f", hoursNeededForCompletingRepair)
        );

        return MenuItem.NextAction.PRINCIPAL_MENU;
    }

    private MenuItem.NextAction somethingNotConfiguredWell(String errorMsg, TextIO textIO, RuntimeException e) {
        logger.error(errorMsg, e);
        textIO.getTextTerminal().println(errorMsg);
        return MenuItem.NextAction.PRINCIPAL_MENU;
    }

    List<RepairType> scheduleRepairs(TextIO textIO, PlatformConfiguration platformConfiguration) {
        List<RepairType> repairsToBeExecuted = new ArrayList<>();

        int numberOfRepairs = textIO.newIntInputReader()
                .withDefaultValue(1)
                .withMinVal(1)
                .read(NUMBER_OF_REPAIRS_TO_BE_EXECUTED);

        for (int i = 0; i < numberOfRepairs; i++) {
            RepairType repairType = configureRepair(textIO, platformConfiguration);
            repairsToBeExecuted.add(repairType);
        }
        return repairsToBeExecuted;
    }

    private RepairType configureRepair(TextIO textIO, PlatformConfiguration platformConfiguration) {
        String repairTypeName = textIO.newStringInputReader()
                .withNumberedPossibleValues(repairTypes(platformConfiguration))
                .read(REPAIR_TYPE);

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

    Vehicle configureVehicle(TextIO textIO, PlatformConfiguration platformConfiguration) {
        String vehicleTypeName = textIO.newStringInputReader()
                .withNumberedPossibleValues(vehicleTypes(platformConfiguration))
                .read(VEHICLE_TYPE);

        String vehicleLicensePlate = textIO.newStringInputReader()
                .withMinLength(5)
                .withMaxLength(7)
                .read(VEHICLE_LICENSE_PLATE);
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
