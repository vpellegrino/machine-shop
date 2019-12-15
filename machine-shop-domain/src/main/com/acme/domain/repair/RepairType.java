package com.acme.domain.repair;

import java.util.Objects;

public class RepairType {
    private final String name;
    private final double hoursNeededToComplete;

    public RepairType(String name, long hoursNeededToComplete) {
        this.name = name;
        this.hoursNeededToComplete = hoursNeededToComplete;
    }

    public double getHoursNeededToComplete() {
        return hoursNeededToComplete;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepairType that = (RepairType) o;
        return hoursNeededToComplete == that.hoursNeededToComplete &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, hoursNeededToComplete);
    }

    @Override
    public String toString() {
        return "RepairType{" +
                "name='" + name + '\'' +
                ", hoursNeededToComplete=" + hoursNeededToComplete +
                '}';
    }
}
