package com.acme.domain.repair;

import com.acme.domain.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RepairOrder {
    private final Date orderDate;
    private final Vehicle vehicle;
    private final List<RepairType> scheduledRepairs;

    public RepairOrder(Vehicle vehicle, List<RepairType> scheduledRepairs) {
        this.orderDate = new Date();
        this.vehicle = vehicle;
        this.scheduledRepairs = scheduledRepairs;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public List<RepairType> getScheduledRepairs() {
        return new ArrayList<>(scheduledRepairs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepairOrder that = (RepairOrder) o;
        return orderDate.equals(that.orderDate) &&
                vehicle.equals(that.vehicle) &&
                scheduledRepairs.equals(that.scheduledRepairs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDate, vehicle, scheduledRepairs);
    }

    @Override
    public String toString() {
        return "RepairOrder{" +
                "orderDate=" + orderDate +
                ", vehicle=" + vehicle +
                ", scheduledRepairs=" + scheduledRepairs +
                '}';
    }
}
