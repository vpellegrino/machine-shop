package com.acme.menu.operation;

import com.acme.domain.platform.Platform;
import com.acme.domain.repair.RepairOrder;
import com.acme.domain.repair.RepairType;
import com.acme.menu.MenuItem;
import com.acme.persistence.PlatformRepository;
import org.beryx.textio.TextIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.acme.menu.operation.PlatformOperation.PLATFORM_NAME;

public class ShowRepairOrdersOperation implements MenuItemOperation {

    private static final Logger logger = LoggerFactory.getLogger(ShowRepairOrdersOperation.class);
    private static final String STRING_JOIN_DELIMITER = ", ";

    @Override
    public MenuItem.NextAction execute(TextIO textIO, PlatformRepository platformRepository) {
        logger.debug("Show Repair Order Operation has been selected");

        String platformName = textIO.newStringInputReader()
                .withNumberedPossibleValues(platformRepository.allPlatformsNames())
                .read(PLATFORM_NAME);

        textIO.getTextTerminal().println(repairOrdersForPlatform(platformRepository, platformName));

        return MenuItem.NextAction.PRINCIPAL_MENU;
    }

    private List<String> repairOrdersForPlatform(PlatformRepository platformRepository, String platformName) {
        return platformRepository.platform(platformName)
                .map(this::plannedRepairOrders)
                .orElse(Collections.emptyList());
    }

    private List<String> plannedRepairOrders(Platform platform) {
        return platform.getRepairOrders().stream()
                .map(this::repairOrderFormatter)
                .collect(Collectors.toList());
    }

    private String repairOrderFormatter(RepairOrder repairOrder) {
        return String.format("- Repair order, placed on %s, for vehicle of type %s, with license plate %s. Planned repairs: %s",
                repairOrder.getOrderDate(),
                repairOrder.getVehicle().getType(),
                repairOrder.getVehicle().getLicensePlate(),
                plannedRepairsFormatter(repairOrder.getScheduledRepairs())
        );
    }

    private String plannedRepairsFormatter(List<RepairType> repairTypes) {
        return repairTypes.stream()
                .map(RepairType::getName)
                .collect(Collectors.joining(STRING_JOIN_DELIMITER));
    }

}
