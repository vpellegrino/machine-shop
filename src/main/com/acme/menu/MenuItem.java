package com.acme.menu;

import com.acme.menu.operation.*;

public enum MenuItem {
    SHOW_REPAIR_ORDERS("Show all repair orders, scheduled for today", new ShowRepairOrdersOperation()),
    ADD_REPAIR_ORDER("Add a new repair order", new AddRepairOrderOperation()),
    COMPLETE_REPAIR_ORDER("Mark a repair order as completed", new CompleteRepairOrderOperation()),
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

    public NextAction execute() {
        return itemOperation.execute();
    }

    public enum NextAction {
        PROMPT_MENU, DISPOSE_MENU
    }
}
