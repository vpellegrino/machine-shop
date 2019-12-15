package com.acme.menu.configuration;

import com.acme.menu.operation.MenuItemOperation;
import com.acme.menu.operation.PlatformOperation;

import static com.acme.menu.MenuItem.NextAction;

public enum ConfigurationMenuItem {
    LIST_ALL_PLATFORMS("List all platforms", new PlatformOperation(CrudOperation.GET_ALL)),
    ADD_PLATFORM("Add a platform", new PlatformOperation(CrudOperation.CREATE)),
    UPDATE_PLATFORM("Update a platform", new PlatformOperation(CrudOperation.UPDATE)),
    DELETE_PLATFORM("Delete a platform", new PlatformOperation(CrudOperation.DELETE)),
    PRINCIPAL_MENU("Go to the principal menu", () -> NextAction.PRINCIPAL_MENU);

    private final String description;
    private final MenuItemOperation itemOperation;

    ConfigurationMenuItem(String description, MenuItemOperation itemOperation) {
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

    public enum CrudOperation {
        CREATE, GET_ALL, UPDATE, DELETE
    }

}
