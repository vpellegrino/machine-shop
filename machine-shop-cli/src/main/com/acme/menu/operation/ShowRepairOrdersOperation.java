package com.acme.menu.operation;

import com.acme.domain.platform.Platform;
import com.acme.domain.platform.PlatformNotFoundException;
import com.acme.domain.repair.RepairOrder;
import com.acme.domain.repair.RepairType;
import com.acme.menu.MenuItem;
import com.acme.persistence.PlatformRepository;
import org.beryx.textio.TextIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import static com.acme.domain.platform.PlatformNotFoundException.PROVIDED_PLATFORM_DOES_NOT_EXISTS;
import static com.acme.menu.operation.PlatformOperation.PLATFORM_NAME;

public class ShowRepairOrdersOperation implements MenuItemOperation {

    private static final Logger logger = LoggerFactory.getLogger(ShowRepairOrdersOperation.class);
    private static final String STRING_JOIN_DELIMITER = ", ";
    static final String NO_REPAIR_ORDER_FOUND = "No repair order to display at this time";
    private static final String REPAIR_ORDER_FORMAT_MSG = "- Repair order, placed on %s, for vehicle of type %s, with license plate %s. Planned repairs: %s";

    @Override
    public MenuItem.NextAction execute(TextIO textIO, PlatformRepository platformRepository) {
        logger.debug("Show Repair Order Operation has been selected");

        String platformName = textIO.newStringInputReader()
                .withNumberedPossibleValues(platformRepository.allPlatformsNames())
                .read(PLATFORM_NAME);

        try {
            textIO.getTextTerminal().println(repairOrdersForPlatform(platformRepository, platformName));
        } catch (PlatformNotFoundException e) {
            textIO.getTextTerminal().println(PROVIDED_PLATFORM_DOES_NOT_EXISTS);
        }

        return MenuItem.NextAction.PRINCIPAL_MENU;
    }

    List<String> repairOrdersForPlatform(PlatformRepository platformRepository, String platformName) {
        return platformRepository.platform(platformName)
                .map(this::checkIfAtLeastOneOrderExists)
                .orElseThrow(PlatformNotFoundException::new);
    }

    private List<String> checkIfAtLeastOneOrderExists(Platform platform) {
        Queue<RepairOrder> repairOrders = platform.getRepairOrders();
        if (repairOrders.isEmpty()) {
            return Collections.singletonList(NO_REPAIR_ORDER_FOUND);
        }
        return plannedRepairOrders(repairOrders);
    }

    private List<String> plannedRepairOrders(Queue<RepairOrder> repairOrders) {
        return repairOrders.stream()
                .map(this::repairOrderFormatter)
                .collect(Collectors.toList());
    }

    private String repairOrderFormatter(RepairOrder repairOrder) {
        return String.format(REPAIR_ORDER_FORMAT_MSG,
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
