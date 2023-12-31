package dhl.view;

//Suppressed warnings for the following import:

import dhl.Constants;
import dhl.model.*;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
public class Cli implements Serializable {

    private static final String TABULATOR = "\t\t";

    /**
     * This method asks the player a question that expects an int as an answer
     * @param start min the int can be
     * @param end max the int can be
     * @param prompt the question to ask the player
     * @return result an Integer which says how many players are going to play.
     */
    public int promptInt(int start, int end, String prompt) {
        System.out.print(prompt + " (" + start + "-" + end + ")" + TABULATOR);
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

    /**
     * this method allows the player to choose a card and write it as a string input.
     * @param prompt the prompt that is printed to the user
     * @return String of the Card that the user selected
     */
    public String promptCardString(String prompt){
        System.out.print(prompt + TABULATOR);
        Scanner scanner = new Scanner(System.in);

            try {
                String result = scanner.next();
                char[] resultChars = result.toCharArray();
                if ("rgbop".contains(Character.toString(resultChars[0])) &&
                        (resultChars.length == 2 &&  "0123456789".contains(Character.toString(resultChars[1])) ||
                        resultChars.length == 3 && resultChars[2] == '0' && resultChars[1] == '1')) {
                    return result;
                } else {
                    error("Please enter one of the options above");
                }
            } catch (Exception e) { //checks if the input is an integer and if it is inbound
                error(e.getMessage());
            }
        return promptCardString(""); //call method again if no correct card was entered
    }

    /**
     * This method allows the player to choose their name as long it is not longer than 16 characters.
     * @param playerAmount the amount of players that will compete at the game
     * @return playersNames a String Array filled with the Names of the Players
     */
    public String[] inputPlayersNames(int playerAmount){
        Scanner scanner = new Scanner(System.in);
        String [] playersNames = new String[playerAmount];
        int nameLength = 16;

        for(int i = 0; i < playerAmount; i++) {
            int j = i + 1;
            System.out.println("Please enter the name of player " + j + ":");
            String playerName = scanner.nextLine();
            while (true) { // checks if the players name has the right length
                if (playerName.length() <= nameLength && !playerName.isEmpty()) {
                    playersNames[i] = playerName;
                    break;
                } else {
                    error("Name can't be empty and only be " + nameLength + " characters long. Try again!");
                    playerName = scanner.nextLine();
                }
            }
        }
        return playersNames;
    }

    /**
     * this method enables the player to take a choice.
     * @param prompt the question the player is asked.
     * @return a boolean that transfers to yes(true) and no(false).
     */
    public boolean promptPlayersChoice(String prompt){
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt + " [y/n]" + TABULATOR);

        while(true){
            try {
                String input = scanner.next();
                if (input.equals("y")) {
                    return true;
                } else if (input.equals("n")) {
                    return false;
                } else {
                    throw new Exception("Please enter either y or n.");
                }
            }
            catch (Exception e){
                error(e.getMessage());
            }
        }
    }

    /**
     * This method allows the player to choose a color.
     * @param prompt the message that the player is asked
     * @return the input char (r, g, b, o or p)
     */
    public char promptColor(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt + " [r, g, b, o, or p]");

        while(true){
            try {
                String input = scanner.next();
                switch (input) {
                    case "r":
                        return 'r';
                    case "g":
                        return 'g';
                    case "b":
                        return 'b';
                    case "p":
                        return 'p';
                    case "o":
                        return 'o';
                    default:
                        throw new Exception("Please enter either r, g, b, o or p.");
                }
            }
            catch (Exception e){
                error(e.getMessage());
            }
        }
    }

    /**
     * this method pints an error in the terminal.
     * @param str Error Message
     */
    public void error(String str) {
        System.err.println(str);
    }

    /**
     * prints the hand card of the player.
     * @param player the active player whose hand cards to show
     */
    public void printHand(Player player) {
        System.out.println(player.getName() + "'s Hand Cards:");
        for (Card card : CardFunction.sortHand(player.getHand())){
            System.out.print(Character.toString(card.getColor())+ card.getNumber() + "  ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * prints the top card of the players directional discarding piles.
     * @param player the active player whose top cards to show
     */
    public void printTopCards(Player player) {
        System.out.println(player.getName() + "'s Top Card:");
        printTop(player.getPlayedCards('b'));
        printTop(player.getPlayedCards('g'));
        printTop(player.getPlayedCards('o'));
        printTop(player.getPlayedCards('p'));
        printTop(player.getPlayedCards('r'));
        System.out.println();
        System.out.println();
    }

    /**
     * prints out the results when the game is over
     * @param game the game that ended
     */
    public void printResults(Game game) {
        System.out.println();
        Player winner = game.getSortedPlayers().get(0);
        System.out.println("GAME OVER! The winner is " + winner.getName() + ".");

        //print points
        System.out.print("Points: ");
        for (Player p: game.getPlayers()) {
            System.out.print(p.getName() + ": " + p.getVictoryPoints() + "   ");
        }
        System.out.println();
    }

    /**
     * prints the top cards of all discarding piles
     * @param game the current game
     */
    public void printDiscardingPiles(Game game) {
        System.out.println();
        System.out.println("Top Cards of all Discarding Piles: ");
        printTop(game.getDiscardPile('b'));
        printTop(game.getDiscardPile('g'));
        printTop(game.getDiscardPile('o'));
        printTop(game.getDiscardPile('p'));
        printTop(game.getDiscardPile('r'));
        System.out.println();
    }

    /**
     * prints a message in the terminal.
     * @param str the message as a string
     */
    public void out(String str) {
        System.out.println(str);
    }

    /**
     * prints the top card and the direction of the pile.
     * @param pile the pile which color, top card and direction to print
     */
    private void printTop(DirectionDiscardPile pile){
        String color = Character.toString(pile.getColor());
        if (pile.isEmpty()){
            System.out.print(color + "+-" + TABULATOR);
        }
        else {
            String direction = pile.getDirectionString();
            System.out.print(color + pile.getTop().getNumber() + direction + " \t");
        }
    }

    /**
     * prints the top card of the discard pile.
     * @param pile the pile which color, top card and direction to print
     */
    private void printTop(DiscardPile pile){
        String color = Character.toString(pile.getColor());
        if (pile.isEmpty()){
            System.out.print(color + "\t");
        }
        else {
            System.out.print(color + pile.getTop().getNumber() +  " \t");
        }
    }

    /**
     * Prints out the current state of the board to the console
     * @param game the currently running game
     */
    public void printCurrentBoard(Game game){

        List<Player> players = game.getPlayers();

        //legend for player-symbols
        for (Player p : players){
            System.out.print(p.getName() + ":" + p.getSymbol() + "    ");
        }
        System.out.println();

        Print print = new Print();
        //print the boards first row (from 0-11)
        print.boardPart(0,11, game);
        //print the boards first row (from 12-23)
        print.boardPart(12,23, game);
        //print the boards first row (from 24-35)
        print.boardPart(24,35, game);

        //print points
        System.out.print("Points: ");
        for (Player p: players) {
            System.out.print(p.getName() + ": " + p.getVictoryPoints() + "   ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * contains the methods that print the board
     */
    private static class Print {

        /**
         * Prints a part of the board onto the cli
         * with the field index, points, color, figures, token and oracle
         * @param rowStart the index of the first field dto print
         * @param rowEnd index of the last field to print
         * @param game the game to print the game from
         */
        public void boardPart(int rowStart, int rowEnd, Game game){
            basic(rowStart, rowEnd);
            token(rowStart, rowEnd, game.getFields());
            figures(rowStart, rowEnd, game.getPlayers());
            oracle(rowStart, rowEnd, game.getOracle());
        }
        private void basic(int rowStart, int rowEnd) {

            Field[] fields = Constants.BASIC_FIELD;

            //print index
            for (int i = rowStart; i <= rowEnd; i++) {
                System.out.print("(" + i + ") \t");
            }
            System.out.println();

            // print points of the fields
            for (int i = rowStart; i <= rowEnd; i++) {
                System.out.print(fields[i].getPoints() + TABULATOR);
            }
            System.out.println();

            // print colors of the fields
            for (int i = rowStart; i <= rowEnd; i++) {
                System.out.print(fields[i].getColor() + TABULATOR);
            }
            System.out.println();
        }

        private void token(int rowStart, int rowEnd, Field[] fields) {
            for (int i = rowStart; i <= rowEnd; i++) {
                if (fields[i].getToken() != null) {
                    if (fields[i] instanceof LargeField && ((LargeField) fields[i]).getTokenTwo() != null) {
                        System.out.print(fields[i].getToken().getSymbol() + " " + fields[i].getToken().getSymbol());
                    } else {
                        System.out.print(fields[i].getToken().getSymbol());
                    }
                }
                System.out.print(TABULATOR);
            }
            System.out.println();
        }

        private void figures(int rowStart, int rowEnd, List<Player> players) {
            for (Player p : players) {
                for (int i = rowStart; i <= rowEnd; i++) {
                    int figureAmount = FigureFunction.getFigureAmountOnField(i, p.getFigures());
                    for (int j = 0; j < figureAmount; j++) {
                        System.out.print(p.getSymbol() + " ");
                    }
                    if (figureAmount > 1) {
                        System.out.print("\t");
                    } else {
                        System.out.print(TABULATOR);
                    }
                }
                System.out.println();
            }
        }

        private void oracle(int rowStart, int rowEnd, int oraclePos) {
            for (int i = rowStart; i <= rowEnd; i++) {
                if (i == oraclePos) {
                    System.out.print("♛");
                } else {
                    System.out.print(TABULATOR);
                }
            }
            System.out.println();
        }
    }
}