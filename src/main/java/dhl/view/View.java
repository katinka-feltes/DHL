package dhl.view;

import dhl.model.Game;

public interface View {
    void printCurrentBoard(Game game);
    String[] inputPlayersNames(int playerAmount);
    int inputPlayerAmount();
}
