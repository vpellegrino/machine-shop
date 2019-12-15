package com.acme.menu.operation;

import com.acme.menu.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddRepairOrderOperation implements MenuItemOperation {

    private static final Logger logger = LoggerFactory.getLogger(AddRepairOrderOperation.class);

    @Override
    public MenuItem.NextAction execute() {
        logger.debug("Add Repair Order Operation has been selected");
        return MenuItem.NextAction.PROMPT_MENU;
    }

}
