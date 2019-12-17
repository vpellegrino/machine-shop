package com.acme.menu.operation;

import com.acme.persistence.PlatformRepository;
import org.beryx.textio.TextIO;

import static com.acme.menu.MenuItem.NextAction;

public interface MenuItemOperation {
    NextAction execute(TextIO textIO, PlatformRepository platformRepository);
}
