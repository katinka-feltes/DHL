package dhl.controller;

import dhl.model.Game;
import dhl.view.Cli;

public class Controller {

    public void startGame(String[] playerName) {
        Game game = new Game(playerName);
        Cli cli = new Cli();
        cli.printCurrentBoard(game);
    }
}
