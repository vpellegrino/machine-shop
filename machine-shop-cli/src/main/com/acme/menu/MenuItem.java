package com.acme.menu;

import com.acme.menu.operation.*;
import com.acme.persistence.PlatformRepository;
import org.beryx.textio.TextIO;

public enum MenuItem {
    SHOW_REPAIR_ORDERS("Show all repair orders, scheduled for today", new ShowRepairOrdersOperation()),
    ADD_REPAIR_ORDER("Add a new repair order", new AddRepairOrderOperation()),
    COMPLETE_REPAIR_ORDER("Mark next repair order as completed", new CompleteRepairOrderOperation()),
    CONFIGURE("Configure your Machine Shop", new ConfigureOperation()),
    EXIT("Exit from the application", new ExitOperation());

    private final MenuItemOperation itemOperation;
    private final String description;

    MenuItem(String description, MenuItemOperation itemOperation) {
        this.description = description;
        this.itemOperation = itemOperation;
    }

    @Override
    public String toString() {
        return this.description;
    }

    public NextAction execute(TextIO textIO, PlatformRepository platformRepository) {
        return itemOperation.execute(textIO, platformRepository);
    }

    public enum NextAction {
        PRINCIPAL_MENU, CONFIGURATION_MENU, DISPOSE_MENU
    }
}
