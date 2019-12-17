package com.acme.domain.platform;

import com.acme.domain.repair.RepairType;
import com.acme.domain.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlatformConfiguration {
    private final List<Vehicle.Type> supportedVehicles;
    private final List<RepairType> supportedRepairTypes;

    public PlatformConfiguration(List<Vehicle.Type> supportedVehicles, List<RepairType> supportedRepairTypes) {
        this.supportedVehicles = supportedVehicles;
        this.supportedRepairTypes = supportedRepairTypes;
    }

    public List<RepairType> getSupportedRepairTypes() {
        return new ArrayList<>(supportedRepairTypes);
    }

    public List<Vehicle.Type> getSupportedVehicles() {
        return new ArrayList<>(supportedVehicles);
    }

    public void addSupportedRepairType(String name, double hoursNeededToComplete) {
        supportedRepairTypes.add(new RepairType(name, hoursNeededToComplete));
    }

    public void addSupportedVehicle(Vehicle.Type type) {
        supportedVehicles.add(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlatformConfiguration platformConfiguration = (PlatformConfiguration) o;
        return Objects.equals(supportedVehicles, platformConfiguration.supportedVehicles) &&
                Objects.equals(supportedRepairTypes, platformConfiguration.supportedRepairTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supportedVehicles, supportedRepairTypes);
    }

    @Override
    public String toString() {
        return "PlatformConfiguration{" +
                "supportedVehicles=" + supportedVehicles +
                ", repairTypeWithDuration=" + supportedRepairTypes +
                '}';
    }
}

