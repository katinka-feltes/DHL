package dhl.view;

import dhl.model.Game;
import dhl.model.Player;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
public class Cli implements View {
    /**
     * The entry point of the CLI application.
     *
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {

    }

    /** This method ask how many players are going to play.
     *
     * @return playerAmount an integer which says how many players are going to play.
     */
    public int inputPlayerAmount(){
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
        return playerAmount;
    }


    /** This method allows the player to choose their name as long it is not longer than 16 characters.
     *
     * @param playerAmount the amount of players that will compete at the game
     * @return playersNames a String Array filled with the Names of the Players
     */
    @Override
    public String[] inputPlayersNames(int playerAmount){
        Scanner scanner = new Scanner(System.in);
        String [] playersNames = new String[playerAmount];
        int nameLength = 16;
        String playerName;

        for(int i = 0; i < playerAmount; i++) {
            int j = i + 1;
            System.out.println("Please enter the name of player " + j + ":");
            playerName = scanner.next();
            boolean toLong = false;
            while (!toLong) { // checks if the players name has the right length
                if (playerName.length() <= nameLength && !playerName.isEmpty()) {
                    playersNames[i] = (playerName);
                    toLong = true;
                } else {
                    System.out.println("Name can't be empty and only be " + nameLength + " characters long. Try again!");
                    playerName = scanner.next();
                }
            }
        }
        return playersNames;
    }


    /**
     * Prints out the current state of the board to the console
     * @param game the currently running game
     */
    public void printCurrentBoard(Game game){

        ArrayList<Player> players = game.getPlayers();

        //legend for player-symbols
        //player 1: C, player 2: H, player 3: K, player 4: M
        for (Player p : players){
            System.out.print(p.getName() + ":" + p.getSymbol() + "    ");
        }
        System.out.println();

        //print the boards first row (from 0-11)
        printBoardPart(0,11, players);
        //print the boards first row (from 12-23)
        printBoardPart(12,23, players);
        //print the boards first row (from 24-35)
        printBoardPart(24,35, players);

        //print points
        System.out.println("Points:");
        for (Player p: players) {
            System.out.print(p.getName() + " " + p.getVictoryPoints() +"   ");
        }
    }

    /**
     * @param rowStart the first field of the board that will be printed
     * @param rowEnd the last field of the board that will be printed
     * @param players the players of the game
     */
    private void printBoardPart(int rowStart, int rowEnd, ArrayList<Player> players){
        // print index
        for (int i = rowStart; i <= rowEnd; i++){
            System.out.print(("(" + i + ")    ").substring(0,7));
        }
        System.out.println();

        // print points of the fields
        for (int i = rowStart; i <= rowEnd; i++){
            System.out.print((Game.FIELDS[i].getPoints() + "      ").substring(0,7));
        }
        System.out.println();

        // print colors of the fields
        for (int i = rowStart; i <= rowEnd; i++){
            System.out.print(Game.FIELDS[i].getColor() + "      ");
        }
        System.out.println();
        //Print figures of the players
        for (Player p: players) {
            for (int i = rowStart; i <= rowEnd; i++){
                if (i == p.getFigures()[0].getPos() && i == p.getFigures()[1].getPos() && i == p.getFigures()[2].getPos()){
                    System.out.print(p.getSymbol() + " " + p.getSymbol() + " " + p.getSymbol() + "  ");
                } else if ((i == p.getFigures()[0].getPos() && i == p.getFigures()[1].getPos()) ||
                        (i == p.getFigures()[1].getPos()&& i == p.getFigures()[2].getPos()) ||
                        (i == p.getFigures()[0].getPos() && i == p.getFigures()[2].getPos())){
                    System.out.print(p.getSymbol() + " " + p.getSymbol() + "    ");
                } else if (i == p.getFigures()[0].getPos() || i == p.getFigures()[1].getPos() || i == p.getFigures()[2].getPos()){
                    System.out.print( p.getSymbol() + "      ");
                }
                else {
                    System.out.print("       ");
                }
            }
            System.out.println();
        }

    }
}
