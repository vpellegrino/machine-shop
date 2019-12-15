package com.acme.menu.operation;

import com.acme.menu.MenuItem;
import com.acme.menu.MenuItem.NextAction;
import com.acme.menu.configuration.ConfigurationMenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlatformOperation implements MenuItemOperation {
    private static final Logger logger = LoggerFactory.getLogger(PlatformOperation.class);
    private ConfigurationMenuItem.CrudOperation crudOperation;


    public PlatformOperation(ConfigurationMenuItem.CrudOperation crudOperation) {
        this.crudOperation = crudOperation;
    }

    @Override
    public MenuItem.NextAction execute() {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Platform Operation (%s) has been selected", crudOperation));
        }

        return NextAction.CONFIGURATION_MENU;
    }
}
