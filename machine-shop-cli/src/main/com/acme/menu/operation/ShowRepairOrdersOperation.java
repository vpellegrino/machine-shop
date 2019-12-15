package com.acme.menu.operation;

import com.acme.menu.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowRepairOrdersOperation implements MenuItemOperation {
    private static final Logger logger = LoggerFactory.getLogger(ShowRepairOrdersOperation.class);

    @Override
    public MenuItem.NextAction execute() {
        logger.debug("Show Repair Order Operation has been selected");
        return MenuItem.NextAction.PROMPT_MENU;
    }

}
