package dhl.view;

import dhl.model.Game;
import dhl.model.Player;

public interface View {

    void printCurrentBoard(Game game);
    String[] inputPlayersNames(int playerAmount);
    int promptInt(int start, int end, String prompt);

    String promptCardString(String prompt);
    void error(String str);
    void printHand(Player player);
    void printTopCards(Player player);
}
