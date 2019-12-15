package com.acme.domain.vehicle;

import java.util.Objects;

public class Vehicle {
    private final Type type;
    private final String licensePlate;

    public Vehicle(Type type, String licensePlate) {
        this.type = type;
        this.licensePlate = licensePlate;
    }

    public Type getType() {
        return type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return type == vehicle.type &&
                Objects.equals(licensePlate, vehicle.licensePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, licensePlate);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "type=" + type +
                ", licensePlate='" + licensePlate + '\'' +
                '}';
    }

    public enum Type {
        CAR, MOTORCYCLE, TRUCK, BUS, BICYCLE, SHIP, AIRCRAFT, OTHER
    }
}
