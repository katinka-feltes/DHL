package dhl;

import dhl.controller.CliController;
import dhl.view.Cli;
import dhl.view.Gui;

import java.util.Arrays;

/**
 * The common starting point of the GUI and the CLI. Depending on the given command line arguments either the GUI or the
 * CLI interface are initialized.
 */
public class Main {
    /**
     * The external entry point of the application.
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {

        CliController c = new CliController();

        boolean cli = Arrays.asList(args).contains("--no-gui");
        if (cli) {
            c.setView(new Cli());
            c.startGame();
        } else {
            //c.setView(new Gui());
            Gui.main(args);
        }
    }
}
