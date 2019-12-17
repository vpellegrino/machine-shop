package com.acme.menu.operation;

import com.acme.domain.platform.Platform;
import com.acme.domain.platform.PlatformNotFoundException;
import com.acme.domain.repair.RepairOrder;
import com.acme.menu.MenuItem;
import com.acme.persistence.PlatformRepository;
import org.beryx.textio.TextIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.acme.domain.platform.PlatformNotFoundException.PROVIDED_PLATFORM_DOES_NOT_EXISTS;
import static com.acme.menu.operation.PlatformOperation.PLATFORM_NAME;

public class CompleteRepairOrderOperation implements MenuItemOperation {

    private static final Logger logger = LoggerFactory.getLogger(CompleteRepairOrderOperation.class);

    @Override
    public MenuItem.NextAction execute(TextIO textIO, PlatformRepository platformRepository) {
        logger.debug("Complete Repair Order Operation has been selected");

        String platformName = textIO.newStringInputReader()
                .withNumberedPossibleValues(platformRepository.allPlatformsNames())
                .read(PLATFORM_NAME);
        Platform platform;
        try {
            platform = platformRepository.platform(platformName).orElseThrow(PlatformNotFoundException::new);
        } catch (PlatformNotFoundException e) {
            textIO.getTextTerminal().println(PROVIDED_PLATFORM_DOES_NOT_EXISTS);
            return MenuItem.NextAction.PRINCIPAL_MENU;
        }

        textIO.getTextTerminal().println(completeRepairOrderFeedback(platformRepository, platform));

        return MenuItem.NextAction.PRINCIPAL_MENU;
    }

    String completeRepairOrderFeedback(PlatformRepository platformRepository, Platform platform) {
        String commandFeedback = platform
                .markNextRepairOrderAsCompleted()
                .map(this::formatRepairOrderDetails)
                .orElse("No repair order to process - Nothing changed");
        platformRepository.savePlatform(platform);
        return commandFeedback;
    }

    private String formatRepairOrderDetails(RepairOrder repairOrder) {
        return String.format("The repair order added on %s, for the vehicle with license plate %s, has been marked as completed",
                repairOrder.getOrderDate(),
                repairOrder.getVehicle().getLicensePlate()
        );
    }

}
