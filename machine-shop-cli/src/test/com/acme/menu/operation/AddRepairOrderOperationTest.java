package com.acme.menu.operation;

import com.acme.domain.platform.PlatformConfiguration;
import com.acme.domain.repair.RepairType;
import com.acme.domain.repair.RepairTypeNotSupportedException;
import com.acme.domain.vehicle.Vehicle;
import com.acme.domain.vehicle.VehicleNotSupportedException;
import org.beryx.textio.IntInputReader;
import org.beryx.textio.StringInputReader;
import org.beryx.textio.TextIO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.acme.menu.operation.AddRepairOrderOperation.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AddRepairOrderOperationTest {

    private static final String OIL_CHANGE = "Oil change";
    private static final String MOTOR_CLEANUP = "Motor cleanup";
    private static final String LICENSE_PLATE = "ABCDE";
    private AddRepairOrderOperation addRepairOrderOperation;
    @Mock
    private TextIO mockedTextIO;

    private IntInputReader mockedIntInputReader;

    private StringInputReader mockedStringInputReader;

    @Before
    public void setUp() {
        initMocks();

        addRepairOrderOperation = new AddRepairOrderOperation();
    }

    private void initMocks() {
        MockitoAnnotations.initMocks(this);
        mockedIntInputReader = Mockito.mock(IntInputReader.class, (Answer) invocation -> mockedIntInputReader);
        mockedStringInputReader = Mockito.mock(StringInputReader.class, (Answer) invocation -> mockedStringInputReader);
    }

    @Test
    public void whenInsertingOneRepairInTheOrder_thenThereIsOnlyOneScheduledRepair() {
        when(mockedTextIO.newIntInputReader()).thenReturn(mockedIntInputReader);
        when(mockedTextIO.newIntInputReader().read(NUMBER_OF_REPAIRS_TO_BE_EXECUTED)).thenReturn(1);

        when(mockedTextIO.newStringInputReader()).thenReturn(mockedStringInputReader);
        when(mockedTextIO.newStringInputReader().read(REPAIR_TYPE)).thenReturn(OIL_CHANGE);
        List<RepairType> scheduledRepairs = addRepairOrderOperation.scheduleRepairs(mockedTextIO, buildPlatformConfiguration());

        assertThat(scheduledRepairs).isNotEmpty();
        assertThat(scheduledRepairs).hasSize(1);
        assertThat(scheduledRepairs.get(0).getName()).isEqualTo(OIL_CHANGE);
    }

    @Test
    public void whenInsertingNoRepairInTheOrder_thenThereIsNoScheduledRepair() {
        when(mockedTextIO.newIntInputReader()).thenReturn(mockedIntInputReader);
        when(mockedTextIO.newIntInputReader().read(NUMBER_OF_REPAIRS_TO_BE_EXECUTED)).thenReturn(0);

        List<RepairType> scheduledRepairs = addRepairOrderOperation.scheduleRepairs(mockedTextIO, buildPlatformConfiguration());

        assertThat(scheduledRepairs).isEmpty();
    }

    @Test(expected = RepairTypeNotSupportedException.class)
    public void givenPlatformWithNoSupportedRepairTypes_whenInsertingRepairInTheOrder_thenException() {
        when(mockedTextIO.newIntInputReader()).thenReturn(mockedIntInputReader);
        when(mockedTextIO.newIntInputReader().read(NUMBER_OF_REPAIRS_TO_BE_EXECUTED)).thenReturn(1);

        when(mockedTextIO.newStringInputReader()).thenReturn(mockedStringInputReader);
        when(mockedTextIO.newStringInputReader().read(REPAIR_TYPE)).thenReturn(OIL_CHANGE);

        PlatformConfiguration platformConfiguration = new PlatformConfiguration(Collections.singletonList(Vehicle.Type.CAR),
                Collections.emptyList());
        addRepairOrderOperation.scheduleRepairs(mockedTextIO, platformConfiguration);
    }

    @Test
    public void whenChoosingVehicleForRepairOrder__thenReturnedVehicleIsProperlyPopulated() {
        when(mockedTextIO.newStringInputReader()).thenReturn(mockedStringInputReader);
        when(mockedTextIO.newStringInputReader().read(VEHICLE_TYPE)).thenReturn(Vehicle.Type.CAR.name());
        when(mockedTextIO.newStringInputReader().read(VEHICLE_LICENSE_PLATE)).thenReturn(LICENSE_PLATE);

        Vehicle vehicle = addRepairOrderOperation.configureVehicle(mockedTextIO, buildPlatformConfiguration());

        assertThat(vehicle).isNotNull();
        assertThat(vehicle.getType()).isEqualTo(Vehicle.Type.CAR);
        assertThat(vehicle.getLicensePlate()).isEqualTo(LICENSE_PLATE);
    }

    private PlatformConfiguration buildPlatformConfiguration() {
        List<Vehicle.Type> supportedVehicles = Arrays.asList(Vehicle.Type.CAR, Vehicle.Type.MOTORCYCLE);
        List<RepairType> supportedRepairTypes = Arrays.asList(new RepairType(OIL_CHANGE, 1.0),
                new RepairType(MOTOR_CLEANUP, 3));
        return new PlatformConfiguration(supportedVehicles, supportedRepairTypes);
    }

    @Test(expected = VehicleNotSupportedException.class)
    public void givenPlatformWithNoSupportedVehicles_whenChoosingVehicleForRepairOrder_thenException() {
        when(mockedTextIO.newStringInputReader()).thenReturn(mockedStringInputReader);
        when(mockedTextIO.newStringInputReader().read(VEHICLE_TYPE)).thenReturn(Vehicle.Type.CAR.name());
        when(mockedTextIO.newStringInputReader().read(VEHICLE_LICENSE_PLATE)).thenReturn(LICENSE_PLATE);

        PlatformConfiguration platformConfiguration = new PlatformConfiguration(Collections.emptyList(),
                Collections.singletonList(new RepairType(OIL_CHANGE, 1.0)));
        addRepairOrderOperation.configureVehicle(mockedTextIO, platformConfiguration);

    }

}
