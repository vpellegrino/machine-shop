package com.acme.domain.platform;

import com.acme.domain.repair.RepairOrder;
import com.acme.domain.repair.RepairType;
import com.acme.domain.repair.RepairTypeNotSupportedException;
import com.acme.domain.vehicle.Vehicle;
import com.acme.domain.vehicle.VehicleNotSupportedException;

import java.util.*;

public class Platform {
    private final String name;
    private final Queue<RepairOrder> repairOrders;
    private final PlatformConfiguration platformConfiguration;
    private double hoursOfRemainingWork;

    public Platform(String name, PlatformConfiguration platformConfiguration) {
        repairOrders = new LinkedList<>();
        this.name = name;
        this.platformConfiguration = platformConfiguration;
    }

    public String getName() {
        return name;
    }

    public PlatformConfiguration getPlatformConfiguration() {
        return platformConfiguration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Platform platform = (Platform) o;
        return name.equals(platform.name) &&
                repairOrders.equals(platform.repairOrders) &&
                platformConfiguration.equals(platform.platformConfiguration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, repairOrders, platformConfiguration);
    }

    @Override
    public String toString() {
        return "Platform{" +
                "name='" + name + '\'' +
                ", repairOrders=" + repairOrders +
                ", platformConfiguration=" + platformConfiguration +
                '}';
    }

    public Queue<RepairOrder> getRepairOrders() {
        return new LinkedList<>(repairOrders);
    }

    public double newRepairOrder(Vehicle vehicle, List<RepairType> repairsToBeExecuted) {
        if (vehicleIsSupportedByPlatform(vehicle)) {
            String errorMessage = String.format("Platform %s does not support vehicles of type %s", name, vehicle.getType());
            throw new VehicleNotSupportedException(errorMessage);
        }
        if (allRepairsAreSupportedByPlatform(repairsToBeExecuted)) {
            String errorMessage = String.format("Platform %s does not support repair of type %s", name, repairsToBeExecuted);
            throw new RepairTypeNotSupportedException(errorMessage);
        }
        this.repairOrders.offer(new RepairOrder(vehicle, repairsToBeExecuted));
        return incrementHoursOfRemainingWork(repairsToBeExecuted);
    }

    public Optional<RepairOrder> markNextRepairOrderAsCompleted() {
        decrementHoursOfRemainingWork();
        return Optional.ofNullable(repairOrders.poll());
    }

    private double decrementHoursOfRemainingWork() {
        double spentHours = Optional.ofNullable(repairOrders.peek())
                .map(repairOrder -> hoursNeededToCompleteRepairOrder(repairOrder.getScheduledRepairs()))
                .orElse(0.0);

        hoursOfRemainingWork -= spentHours;

        return hoursOfRemainingWork;
    }

    private double hoursNeededToCompleteRepairOrder(List<RepairType> scheduledRepairs) {
        return scheduledRepairs.stream().mapToDouble(RepairType::getHoursNeededToComplete).sum();
    }

    private double incrementHoursOfRemainingWork(List<RepairType> repairsToBeExecuted) {
        double hoursToSpend = hoursNeededToCompleteRepairOrder(repairsToBeExecuted);

        hoursOfRemainingWork += hoursToSpend;

        return hoursOfRemainingWork;
    }

    private boolean allRepairsAreSupportedByPlatform(List<RepairType> repairsToBeExecuted) {
        return !platformConfiguration.getSupportedRepairTypes().containsAll(repairsToBeExecuted);
    }

    private boolean vehicleIsSupportedByPlatform(Vehicle vehicle) {
        return !platformConfiguration.getSupportedVehicles().contains(vehicle.getType());
    }
}
