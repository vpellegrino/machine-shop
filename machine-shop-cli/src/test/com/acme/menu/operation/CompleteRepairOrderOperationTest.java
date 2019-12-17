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

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CompleteRepairOrderOperationTest {

    @Mock
    private PlatformRepository platformRepository;

    @Mock
    private Platform mockedPlatform;

    private CompleteRepairOrderOperation completeRepairOrderOperation;
    private RepairOrder repairOrder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        completeRepairOrderOperation = new CompleteRepairOrderOperation();
        repairOrder = new RepairOrder(new Vehicle(Vehicle.Type.CAR, "ABCDE"), Collections.singletonList(new RepairType("change oil", 1)));
    }

    @Test
    public void whenMarkingRepairOrderAsCompleted_thenFeedbackMessageGivesDetailsAboutIt() {
        when(mockedPlatform.markNextRepairOrderAsCompleted()).thenReturn(Optional.of(repairOrder));

        String orderFeedback = completeRepairOrderOperation.completeRepairOrderFeedback(platformRepository, mockedPlatform);
        assertThat(orderFeedback).startsWith("The repair order added on");
        assertThat(orderFeedback).endsWith("for the vehicle with license plate ABCDE, has been marked as completed");
    }

    @Test
    public void givenPlatformWithoutRepairOrders_whenMarkingRepairOrderAsCompleted_thenFeedbackMessageGivesDetailsAboutIt() {
        when(mockedPlatform.markNextRepairOrderAsCompleted()).thenReturn(Optional.empty());

        String orderFeedback = completeRepairOrderOperation.completeRepairOrderFeedback(platformRepository, mockedPlatform);
        assertThat(orderFeedback).isEqualTo("No repair order to process - Nothing changed");
    }
}