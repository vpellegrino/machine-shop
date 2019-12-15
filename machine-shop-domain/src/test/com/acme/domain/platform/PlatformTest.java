package com.acme.domain.platform;

import com.acme.domain.repair.RepairOrder;
import com.acme.domain.repair.RepairType;
import com.acme.domain.repair.RepairTypeNotSupportedException;
import com.acme.domain.vehicle.Vehicle;
import com.acme.domain.vehicle.VehicleNotSupportedException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PlatformTest {

    private RepairType changeOil;
    private RepairType changeBrakes;
    private PlatformConfiguration carPlatformConfiguration;
    private PlatformConfiguration truckPlatformConfiguration;

    @Before
    public void setUp() {
        changeOil = new RepairType("change oil", 1);
        changeBrakes = new RepairType("changeBrakes", 2);
        carPlatformConfiguration = new PlatformConfiguration(
                Collections.singletonList(Vehicle.Type.CAR),
                Collections.singletonList(changeOil)
        );
        truckPlatformConfiguration = new PlatformConfiguration(
                Collections.singletonList(Vehicle.Type.TRUCK),
                Collections.singletonList(changeOil)
        );
    }

    @Test
    public void givenEmptyPlatform_whenAddingNewRepairOrder_thenPlannedRepairsNoMoreEmpty() {
        Platform carPlatform = new Platform("Car Platform", carPlatformConfiguration);

        carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "ABCDEF"), Collections.singletonList(changeOil));

        assertThat(carPlatform.getRepairOrders()).isNotEmpty();
        assertThat(carPlatform.getRepairOrders()).hasSize(1);
    }

    @Test(expected = RepairTypeNotSupportedException.class)
    public void givenPlatform_whenAddingUnsupportedRepairOrder_thenException() {
        Platform carPlatform = new Platform("Car Platform", carPlatformConfiguration);

        carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "ABCDEF"), Arrays.asList(changeOil, changeBrakes));

        assertThat(carPlatform.getRepairOrders()).isNotEmpty();
        assertThat(carPlatform.getRepairOrders()).hasSize(1);
    }

    @Test(expected = VehicleNotSupportedException.class)
    public void givenPlatformForTruckOnly_whenAddingNewRepairOrderForCar_thenException() {
        Platform truckPlatform = new Platform("Truck Platform", truckPlatformConfiguration);

        truckPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "ABCDEF"), Collections.singletonList(changeOil));
    }

    @Test
    public void givenEmptyPlatform_whenAddingNewRepairOrder_thenPlatformHoursToCompleteAreTheOnesOfSuchOrder() {
        Platform carPlatform = new Platform("Car Platform", carPlatformConfiguration);

        double hoursToCompleteAllRepairOrders = carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "ABCDEF"), Collections.singletonList(changeOil));

        assertThat(hoursToCompleteAllRepairOrders).isEqualTo(1);
    }

    @Test
    public void givenPlatformWithFewOrders_whenAddingNewRepairOrder_thenPlatformHoursGrowAccordingly() {
        Platform carPlatform = new Platform("Car Platform", carPlatformConfiguration);

        carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "ABCDEF"), Collections.singletonList(changeOil));
        carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "CDEFGH"), Collections.singletonList(changeOil));
        double hoursToCompleteAllRepairOrders = carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "ILMNOP"), Collections.singletonList(changeOil));

        assertThat(hoursToCompleteAllRepairOrders).isEqualTo(3);
    }

    @Test
    public void givenPlatformWithOnlyOneRepairOrder_whenMarkingNextRepairOrderAsCompleted_thenNoMoreRepairOrdersArePresent() {
        Platform carPlatform = new Platform("Car Platform", carPlatformConfiguration);
        carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "ABCDEF"), Collections.singletonList(changeOil));

        assertThat(carPlatform.markNextRepairOrderAsCompleted()).isNotEmpty();
        assertThat(carPlatform.getRepairOrders()).isEmpty();
    }

    @Test
    public void givenPlatformWithTwoRepairOrders_whenMarkingNextRepairOrderAsCompleted_thenOnlyOneIsPresent() {
        Platform carPlatform = new Platform("Car Platform", carPlatformConfiguration);
        carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "ABCDEF"), Collections.singletonList(changeOil));

        assertThat(carPlatform.markNextRepairOrderAsCompleted()).isNotEmpty();

        double hoursToCompleteAllRepairOrders = carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "ABCDEF"), Collections.singletonList(changeOil));
        assertThat(hoursToCompleteAllRepairOrders).isEqualTo(1);
        assertThat(carPlatform.getRepairOrders()).isNotEmpty();
    }

    @Test
    public void givenPlatformWithNoRepairOrder_whenMarkingNextRepairOrderAsCompleted_thenNothingIsReturnedAndNoExceptionThrown() {
        Platform carPlatform = new Platform("Car Platform", carPlatformConfiguration);

        assertThat(carPlatform.markNextRepairOrderAsCompleted()).isEmpty();
    }

    @Test
    public void givenPlatformWithFewOrders_whenAddingNewRepairOrder_thenFifoOrderIsRespected() {
        Platform carPlatform = new Platform("Car Platform", carPlatformConfiguration);

        carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "ABCDEF"), Collections.singletonList(changeOil));
        carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "CDEFGH"), Collections.singletonList(changeOil));
        carPlatform.newRepairOrder(new Vehicle(Vehicle.Type.CAR, "ILMNOP"), Collections.singletonList(changeOil));

        List<RepairOrder> repairOrders = new ArrayList<>(carPlatform.getRepairOrders());
        assertThat(repairOrders).hasSize(3);
        assertThat(repairOrders.get(0).getVehicle().getLicensePlate()).isEqualTo("ABCDEF");
        assertThat(repairOrders.get(1).getVehicle().getLicensePlate()).isEqualTo("CDEFGH");
        assertThat(repairOrders.get(2).getVehicle().getLicensePlate()).isEqualTo("ILMNOP");
    }

}