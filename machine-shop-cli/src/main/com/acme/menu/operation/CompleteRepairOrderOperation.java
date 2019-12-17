package com.acme.menu.operation;

import com.acme.domain.platform.Platform;
import com.acme.domain.platform.PlatformNotFoundException;
import com.acme.domain.repair.RepairOrder;
import com.acme.menu.MenuItem;
import com.acme.persistence.PlatformRepository;
import org.beryx.textio.TextIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompleteRepairOrderOperation implements MenuItemOperation {

    private static final Logger logger = LoggerFactory.getLogger(CompleteRepairOrderOperation.class);

    @Override
    public MenuItem.NextAction execute(TextIO textIO, PlatformRepository platformRepository) {
        logger.debug("Complete Repair Order Operation has been selected");

        String platformName = textIO.newStringInputReader()
                .withNumberedPossibleValues(platformRepository.allPlatformsNames())
                .read("Platform name");

        Platform platform = platformRepository.platform(platformName).orElseThrow(PlatformNotFoundException::new);
        String commandFeedback = platform
                .markNextRepairOrderAsCompleted()
                .map(this::formatRepairOrderDetails)
                .orElse("No repair order to process - Nothing changed");
        platformRepository.savePlatform(platform);

        textIO.getTextTerminal().println(commandFeedback);

        return MenuItem.NextAction.PRINCIPAL_MENU;
    }

    private String formatRepairOrderDetails(RepairOrder repairOrder) {
        return String.format("The repair order added on %s, for the vehicle with license plate %s, has been marked as completed",
                repairOrder.getOrderDate(),
                repairOrder.getVehicle().getLicensePlate()
        );
    }

}
