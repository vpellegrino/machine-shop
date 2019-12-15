package com.acme;

import com.acme.menu.MenuCommander;
import org.beryx.textio.TextIoFactory;

public class MachineShop {

    public static void main(String[] args) {
        MenuCommander menuCommander = new MenuCommander(TextIoFactory.getTextIO());
        menuCommander.promptMenuChoices();
    }

}

