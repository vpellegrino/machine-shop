package com.acme.menu.operation;

import com.acme.domain.platform.Platform;
import com.acme.domain.repair.RepairOrder;
import com.acme.domain.repair.RepairType;
import com.acme.domain.vehicle.Vehicle;
import com.acme.persistence.PlatformRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static com.acme.menu.operation.ShowRepairOrdersOperation.NO_REPAIR_ORDER_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ShowRepairOrdersOperationTest {

    private static final String PLATFORM_NAME = "PLATFORM_NAME";
    private ShowRepairOrdersOperation showRepairOrdersOperation;
    @Mock
    private PlatformRepository mockedPlatformRepository;

    @Mock
    private Platform mockedPlatform;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        showRepairOrdersOperation = new ShowRepairOrdersOperation();
    }

    @Test
    public void whenListingAllRepairsForPlatform_thenMessagesAreCorrectlyFormatted() {
        when(mockedPlatformRepository.platform(PLATFORM_NAME)).thenReturn(Optional.of(mockedPlatform));
        when(mockedPlatform.getRepairOrders()).thenReturn(mockRepairOrders());

        List<String> repairOrdersReports = showRepairOrdersOperation.repairOrdersForPlatform(mockedPlatformRepository, PLATFORM_NAME);
        assertThat(repairOrdersReports).isNotEmpty();
        assertThat(repairOrdersReports).hasSize(1);
        assertThat(repairOrdersReports.get(0)).startsWith("- Repair order, placed on ");
        assertThat(repairOrdersReports.get(0)).endsWith("for vehicle of type CAR, with license plate ABCDE. Planned repairs: oil change");
    }

    @Test
    public void givenPlatformWithoutRepairs_whenListingThem_thenNoRepairOrderFoundMessage() {
        when(mockedPlatformRepository.platform(PLATFORM_NAME)).thenReturn(Optional.of(mockedPlatform));
        when(mockedPlatform.getRepairOrders()).thenReturn(new ArrayDeque<>());

        List<String> repairOrdersReports = showRepairOrdersOperation.repairOrdersForPlatform(mockedPlatformRepository, PLATFORM_NAME);
        assertThat(repairOrdersReports).isNotEmpty();
        assertThat(repairOrdersReports).hasSize(1);
        assertThat(repairOrdersReports.get(0)).isEqualTo(NO_REPAIR_ORDER_FOUND);
    }

    private Queue<RepairOrder> mockRepairOrders() {
        RepairOrder repairOrder = new RepairOrder(new Vehicle(Vehicle.Type.CAR, "ABCDE"), Collections.singletonList(new RepairType("oil change", 1)));
        Queue<RepairOrder> orderQueue = new LinkedList<>();
        orderQueue.offer(repairOrder);
        return orderQueue;
    }

}