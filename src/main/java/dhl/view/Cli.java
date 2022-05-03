package dhl.view;

import dhl.controller.Controller;
import dhl.model.Card;
import dhl.model.DirectionDiscardPile;
import dhl.model.Game;
import dhl.model.Player;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
public class Cli implements View {

    private final Controller controller;

    public Cli(Controller controller){
        this.controller = controller;
    }

    /**
     * The entry point of the CLI application.
     *
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {

    }

    /** This method ask how many players are going to play.
     *
     * @return result an Integer which says how many players are going to play.
     */
    @Override
    public int promptInt(int start, int end, String prompt) {
        System.out.println(prompt);
        Scanner scanner = new Scanner(System.in);

        while(true){ //checks if the input is an integer and if it is inbound
            try{
                int result = scanner.nextInt();
                // check if the number is inbound
                while (result < start || result > end) {
                    error("Please enter a number between " + start + " and " + end + ".");
                    result = scanner.nextInt();
                }
                return result;
            } catch (InputMismatchException e){
                error("Input must be an integer!");
                scanner.nextLine();
            }
        }
    }

    @Override
    public String promptCardString(String prompt){
        System.out.println(prompt);
        Scanner scanner = new Scanner(System.in);

        while(true) { //checks if the input is an integer and if it is inbound
            try {
                String result = scanner.next();
                char[] resultChars = result.toCharArray();
                if (resultChars.length >= 2 && "rgbop".contains(Character.toString(resultChars[0])) && "0123456789".contains(Character.toString(resultChars[1]))) {
                    if (resultChars.length == 3 && resultChars[2] != '0' && resultChars[1] == '1') {
                        throw new Exception("There is no card greater than 10");
                    } else if (resultChars.length > 3) {
                        throw new Exception("Please enter one of the options above (max. length 3)");
                    }
                    return result;
                } else {
                    error("Please enter one of the options above");
                }
            } catch (Exception e) {
                error(e.getMessage());
                scanner.nextLine();
            }
        }
    }

    /**
     * @param str Error Message
     */
    @Override
    public void error(String str) {
        System.err.println(str);
    }

    /**
     * @param player the active player whose hand cards to show
     */
    @Override
    public void printHand(Player player) {
        System.out.println("Active Player's (" + player.getName() + ") Hand Cards:");
        for (Card card : player.getHand()){
            System.out.print(Character.toString(card.getColor())+ card.getNumber() + "   ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * @param player the active player whose top cards to show
     */
    @Override
    public void printTopCards(Player player) {
        System.out.println("Active Player's (" + player.getName() + ") Top Card:");
        printTop(player.getPlayedCardsRed());
        printTop(player.getPlayedCardsBlue());
        printTop(player.getPlayedCardsGreen());
        printTop(player.getPlayedCardsPurple());
        printTop(player.getPlayedCardsOrange());
        System.out.println();
        System.out.println();
    }

    /**
     * @param pile the pile which color, top card and direction to print
     */
    private void printTop( DirectionDiscardPile pile ){
        String color = Character.toString(pile.getColor());
        if (pile.isEmpty()){
            System.out.print(color + printDirection(pile) + "    ");
        }
        else {
            System.out.print(color + pile.getTop().getNumber() + printDirection(pile) + "   ");
        }
    }

    /**
     * @param pile pile whose direction to print
     * @return direction as a String
     */
    private String printDirection(DirectionDiscardPile pile) {
        if (pile.getDirection() == 0) {
            return "+-";
        } else if (pile.getDirection() == 1) {
            return "+";
        } else {
            return "-";
        }
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

        for(int i = 0; i < playerAmount; i++) {
            int j = i + 1;
            System.out.println("Please enter the name of player " + j + ":");
            String playerName = scanner.next();
            while (true) { // checks if the players name has the right length
                if (playerName.length() <= nameLength && !playerName.isEmpty()) {
                    playersNames[i] = (playerName);
                    break;
                } else {
                    error("Name can't be empty and only be " + nameLength + " characters long. Try again!");
                    playerName = scanner.next();
                }
            }
        }
        return playersNames;
    }

    /** this method enables the player to take a choice.
     *
     * @param prompt the question the player is asked.
     * @return a boolean that transfers to yes(true) and no(false).
     * @throws Exception if the input is yes nor no.
     */
    public boolean promptPlayersChoice(String prompt) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);

        while(true){
            try {
                String input = scanner.next();
                if (input.equals("yes")) {
                    return true;
                } else if (input.equals("no")) {
                    return false;
                } else {
                    throw new Exception("Please enter either yes or no.");
                }
            }
            catch (Exception e){
                error(e.getMessage());
                scanner.nextLine();
            }
        }
    }

    /**
     * Prints out the current state of the board to the console
     * @param game the currently running game
     */
    @Override
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
        System.out.print("Points: ");
        for (Player p: players) {
            System.out.print(p.getName() + " " + p.getVictoryPoints() + "   ");
        }
        System.out.println();
        System.out.println();
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
        for (int i = rowStart; i <= rowEnd; i++) {
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
                switch (p.getFigureAmountOnField(i)){
                    case (3):
                        System.out.print(p.getSymbol() + " " + p.getSymbol() + " " + p.getSymbol() + "  ");
                        break;
                    case (2):
                        System.out.print(p.getSymbol() + " " + p.getSymbol() + "    ");
                        break;
                    case (1):
                        System.out.print( p.getSymbol() + "      ");
                        break;
                    default:
                        System.out.print("       ");
                }
            }
            System.out.println();
        }
    }
}
