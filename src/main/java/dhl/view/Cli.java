package dhl.view;

//Suppressed warnings for the following import:

import dhl.model.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
@SuppressWarnings("ClassEscapesDefinedScope")
public class Cli implements View {

    private static final String TABULATOR = "\t\t";

    /** This method ask how many players are going to play.
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

    /**
     * this method allows the player to choose a card and write it as a string input.
     * @param prompt the prompt that is printed to the user
     * @return String of the Card that the user selected
     */
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
            }
        }
    }

    /**
     * This method allows the player to choose their name as long it is not longer than 16 characters.
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
    @Override
    public boolean promptPlayersChoice(String prompt){
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt + " [y/n]");

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
    @Override
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
    @Override
    public void error(String str) {
        System.err.println(str);
    }

    /**
     * prints the hand card of the player.
     * @param player the active player whose hand cards to show
     */
    @Override
    public void printHand(Player player) {
        System.out.println(player.getName() + "'s Hand Cards:");
        for (Card card : CardFunction.sortHand(player.getHand())){
            System.out.print(Character.toString(card.getColor())+ card.getNumber() + "\t");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * prints the top card of the players directional discarding piles.
     * @param player the active player whose top cards to show
     */
    @Override
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
    @Override
    public void printResults(Game game) {
        System.out.println();
        System.out.println("GAME OVER! The winner is " + game.getWinningPlayer().getName() + ".");

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
    @Override
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
    @Override
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
            String direction;
            if (pile.getDirection() == 0) {
                direction =  "+-";
            } else if (pile.getDirection() == 1) {
                direction = "+";
            } else {
                direction = "-";
            }
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
    @Override
    public void printCurrentBoard(Game game){

        List<Player> players = game.getPlayers();

        //legend for player-symbols
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
            System.out.print(p.getName() + ": " + p.getVictoryPoints() + "   ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * prints the filed index, points, color, tokens and figures on the board.
     *
     * @param rowStart the first field of the board that will be printed
     * @param rowEnd the last field of the board that will be printed
     * @param players the players of the game
     */
    private void printBoardPart(int rowStart, int rowEnd, List<Player> players){
        // print index
        for (int i = rowStart; i <= rowEnd; i++){
            System.out.print("(" + i + ") \t");
        }
        System.out.println();

        // print points of the fields
        for (int i = rowStart; i <= rowEnd; i++) {
            System.out.print(Game.FIELDS[i].getPoints() + TABULATOR);
        }
        System.out.println();

        // print colors of the fields
        for (int i = rowStart; i <= rowEnd; i++){
            System.out.print(Game.FIELDS[i].getColor() + TABULATOR);
        }
        System.out.println();

        // print tokens of the fields
        for (int i = rowStart; i <= rowEnd; i++){
            if(Game.FIELDS[i].getToken() != null) {
                if (Game.FIELDS[i] instanceof LargeField && ((LargeField) Game.FIELDS[i]).getTokenTwo() != null) {
                    System.out.print(Game.FIELDS[i].getToken().getSymbol() + " " + Game.FIELDS[i].getToken().getSymbol());
                } else {
                    System.out.print(Game.FIELDS[i].getToken().getSymbol());
                }
            }
            System.out.print(TABULATOR);
        }
        System.out.println();
        //Print figures of the players
        for (Player p: players) {
            for (int i = rowStart; i <= rowEnd; i++){
                int figureAmount = FigureFunction.getFigureAmountOnField(i, p.getFigures());
                for (int j = 0; j < figureAmount; j++){
                    System.out.print(p.getSymbol() + " ");
                }
                if (figureAmount == 3) {
                    System.out.print("\t");
                } else {
                    System.out.print(TABULATOR);
                }
            }
            System.out.println();
        }
    }
}