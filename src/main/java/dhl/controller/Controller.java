package dhl.controller;

import dhl.model.Game;
import dhl.view.View;


public class Controller {
    View view;
    Game model;

    public void startGame() {
        String[] playerName = view.inputPlayersNames(view.inputPlayerAmount());
        model = new Game(playerName);
        view.printCurrentBoard(model);
    }



    public void setView(View view) {
        this.view = view;
    }

}
