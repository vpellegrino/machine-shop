package com.acme.menu;

import org.beryx.textio.TextIO;

public class MenuCommander {

    private TextIO textIO;

    public MenuCommander(TextIO textIO) {
        this.textIO = textIO;
    }

    public void promptMenuChoices() {
        MenuItem.NextAction nextAction = textIO.newEnumInputReader(MenuItem.class)
                .read("Machine Shop - Available operations:")
                .execute();

        switch (nextAction) {
            case PROMPT_MENU:
                promptMenuChoices();
                break;
            case DISPOSE_MENU:
                disposeMenu();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + nextAction);
        }
    }

    private void disposeMenu() {
        textIO.dispose("Exiting from Machine Shop application");
    }

}
