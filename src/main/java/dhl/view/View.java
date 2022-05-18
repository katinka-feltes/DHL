package dhl.view;

import dhl.model.Game;
import dhl.model.Player;

@SuppressWarnings("ClassEscapesDefinedScope")
/**
 * This Class represents the View. It is the only class that can be accessed from the outside.
 * It is responsible for the user interaction and the communication between the model and the view.
 */
public interface View {

    String[] inputPlayersNames(int playerAmount);
    int promptInt(int start, int end, String prompt);
    boolean promptPlayersChoice(String question);
    char promptColor(String prompt);
    String promptCardString(String prompt);

    void printCurrentBoard(Game game);
    void printHand(Player player);
    void printTopCards(Player player);
    void printResults(Game game);
    void printDiscardingPiles(Game game);
    void out(String str);
    void error(String str);

}
