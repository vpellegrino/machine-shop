package com.acme.menu;

import com.acme.menu.configuration.ConfigurationMenuItem;
import org.beryx.textio.TextIO;

public class MenuCommander {

    private TextIO textIO;

    public MenuCommander(TextIO textIO) {
        this.textIO = textIO;
    }

    public void promptMenuChoices() {
        doNextAction(
                textIO.newEnumInputReader(MenuItem.class)
                        .read("Machine Shop - Available operations:")
                        .execute(textIO)
        );
    }

    private void promptConfigurationMenuChoices() {
        doNextAction(
                textIO.newEnumInputReader(ConfigurationMenuItem.class)
                        .read("Configure your Machine Shop:")
                        .execute(textIO)
        );
    }

    private void doNextAction(MenuItem.NextAction nextAction) {
        switch (nextAction) {
            case PRINCIPAL_MENU:
                promptMenuChoices();
                break;
            case CONFIGURATION_MENU:
                promptConfigurationMenuChoices();
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
