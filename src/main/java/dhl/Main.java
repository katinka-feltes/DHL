package dhl;

import dhl.controller.Controller;
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

        Controller c = new Controller();

        boolean cli = Arrays.asList(args).contains("--no-gui");
        if (cli) {
            c.setView(new Cli(c));
        } else {
            Gui.main(args);
            //c.setView(new Gui());
        }
        c.startGame();
    }
}
