package dhl.controller;

import dhl.model.Game;
import dhl.view.View;


public class Controller {
    View view;
    Game model;

    public void startGame() {
        String[] playerName = view.inputPlayersNames(view.promptInt(2, 4, "how many players? (2, 3 or 4)"));
        model = new Game(playerName);
        view.printCurrentBoard(model);
    }

    public void setView(View view) {
        this.view = view;
    }

}
