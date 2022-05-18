package dhl.view;

import dhl.model.Game;
import dhl.model.Player;

@SuppressWarnings("ClassEscapesDefinedScope")
/**
 * This Class represents the View. It is the only class that can be accessed from the outside.
 * It is responsible for the user interaction and the communication between the model and the view.
 */
public interface View {

    /**
     * Method to get input of names
     * @param playerAmount the amount of names to get
     * @return String array with all names
     */
    String[] inputPlayersNames(int playerAmount);

    /**
     * get number input
     * @param start lowest number allowed
     * @param end highest number allowed
     * @param prompt the reason for the input to display
     * @return the input int
     */
    int promptInt(int start, int end, String prompt);

    /**
     * this method enables the player to take a choice.
     * @param prompt the question the player is asked.
     * @return a boolean that transfers to yes(true) and no(false).
     */
    boolean promptPlayersChoice(String prompt);

    /**
     * This method allows the player to choose a color.
     * @param prompt the message that the player is asked
     * @return the input char (r, g, b, o or p)
     */
    char promptColor(String prompt);

    /**
     * this method allows the player to choose a card and write it as a string input.
     * @param prompt the prompt that is printed to the user
     * @return String of the Card that the user selected
     */
    String promptCardString(String prompt);

    /**
     * Prints out the current state of the board to the console
     * @param game the currently running game
     */
    void printCurrentBoard(Game game);

    /**
     * prints the hand card of the player.
     * @param player the active player whose hand cards to show
     */
    void printHand(Player player);

    /**
     * prints the top card of the players directional discarding piles.
     * @param player the active player whose top cards to show
     */
    void printTopCards(Player player);

    /**
     * prints out the results when the game is over
     * @param game the game that ended
     */
    void printResults(Game game);

    /**
     * prints the top cards of all discarding piles
     * @param game the current game
     */
    void printDiscardingPiles(Game game);

    /**
     * prints a message in the terminal.
     * @param str the message as a string
     */
    void out(String str);

    /**
     * this method pints an error in the terminal.
     * @param str Error Message
     */
    void error(String str);

}
