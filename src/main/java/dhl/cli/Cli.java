package dhl.cli;

import dhl.model.Game;
import java.util.Scanner;
import java.util.InputMismatchException;


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
        Scanner scanner = new Scanner(System.in);
        int playerAmount = 0; //initializing the variable playerAmount
        boolean inputValid = false; //initializing the variable inputValid

        while(!inputValid){ //checks if the input is an integer and if it is inbound
            try{
                playerAmount = scanner.nextInt();
                // check if the number is inbound
                while (playerAmount < 2 || playerAmount > 4) {
                    System.out.println("Please enter a number between 2 and 4.");
                    playerAmount = scanner.nextInt();
                }
                    inputValid = true;
            } catch (InputMismatchException e){
                System.out.println("Input must be an integer!");
                scanner.nextLine();
            }
        }

        Game game = new Game(playerAmount);

    }
}
