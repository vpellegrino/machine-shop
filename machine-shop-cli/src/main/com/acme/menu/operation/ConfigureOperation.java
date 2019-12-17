package com.acme.menu.operation;

import com.acme.menu.MenuItem;
import org.beryx.textio.TextIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigureOperation implements MenuItemOperation {

    private static final Logger logger = LoggerFactory.getLogger(ConfigureOperation.class);

    @Override
    public MenuItem.NextAction execute(TextIO textIO) {
        logger.debug("Configure Operation has been selected");
        return MenuItem.NextAction.CONFIGURATION_MENU;
    }

}
