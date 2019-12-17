package com.acme.menu.operation;

import com.acme.menu.MenuItem;
import org.beryx.textio.TextIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.acme.menu.MenuItem.NextAction;

public class ExitOperation implements MenuItemOperation {

    private static final Logger logger = LoggerFactory.getLogger(ExitOperation.class);

    @Override
    public MenuItem.NextAction execute(TextIO textIO) {
        logger.debug("Exit Operation has been selected");
        return NextAction.DISPOSE_MENU;
    }

}
