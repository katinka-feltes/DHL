package dhl.cli;

import dhl.model.Game;

import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
public class Cli {
    /**
     * The entry point of the CLI application.
     *
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        System.out.println("How many players? (2, 3 or 4)");
        Scanner scanner = new Scanner(System.in) ;
        int playerAmount = scanner.nextInt();

        Game game = new Game(playerAmount);

    }
}
