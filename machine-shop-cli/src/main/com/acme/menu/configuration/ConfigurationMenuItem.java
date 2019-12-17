package com.acme.menu.configuration;

import com.acme.menu.operation.MenuItemOperation;
import com.acme.menu.operation.PlatformOperation;
import com.acme.persistence.PlatformRepository;
import org.beryx.textio.TextIO;

import static com.acme.menu.MenuItem.NextAction;
import static com.acme.menu.configuration.ConfigurationMenuItem.ConfigOperation.*;

public enum ConfigurationMenuItem {
    LIST_ALL_PLATFORMS("List all platforms", new PlatformOperation(GET_ALL)),
    ADD_PLATFORM("Add a platform", new PlatformOperation(CREATE)),
    DELETE_PLATFORM("Delete a platform", new PlatformOperation(DELETE)),
    PRINCIPAL_MENU("Go to the principal menu", (TextIO textIO, PlatformRepository platformRepository) -> NextAction.PRINCIPAL_MENU);

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

    public NextAction execute(TextIO textIO, PlatformRepository platformRepository) {
        return itemOperation.execute(textIO, platformRepository);
    }

    public enum ConfigOperation {
        CREATE, GET_ALL, DELETE
    }

}
