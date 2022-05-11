package dhl.view;

import dhl.model.Game;
import dhl.model.Player;

@SuppressWarnings("ClassEscapesDefinedScope")
public interface View {

    void printCurrentBoard(Game game);
    String[] inputPlayersNames(int playerAmount);
    int promptInt(int start, int end, String prompt);
    boolean promptPlayersChoice(String question);
    char promptColor(String prompt);
    char promptColorAndHand(String prompt);

    String promptCardString(String prompt);
    void error(String str);
    void printHand(Player player);
    void printTopCards(Player player);
    void printResults(Game game);
    void printDiscardingPiles(Game game);
    void out(String str);


}
