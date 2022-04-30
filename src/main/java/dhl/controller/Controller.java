package dhl.controller;

import dhl.model.Card;
import dhl.model.Game;
import dhl.model.Player;
import dhl.view.View;


public class Controller {
    View view;
    Game model;

    public void startGame() {
        String[] playerName = view.inputPlayersNames(view.promptInt(2, 4, "How many players? (2, 3 or 4)"));
        model = new Game(playerName);
        view.printCurrentBoard(model);

        playCard(model.getPlayers().get(0), "r3");
        view.printCurrentBoard(model);
        playCard(model.getPlayers().get(0), "r5");
        view.printCurrentBoard(model);
        playCard(model.getPlayers().get(0), "r2");
        view.printCurrentBoard(model);
    }

    public void playCard(Player player, String cardString){

        //eig card von players hand bekommen
        char[] cardValue = cardString.toCharArray();
        Card card = new Card(Character.getNumericValue(cardValue[1]), cardValue[0]);

        // oracle or figure?

        //choose a figure
        while (true) {
            try {
                player.playCard(card, view.promptInt(0, 35, "Which figure do you want to move?"));
                break;
            } catch (Exception e) {
                view.error(e.getMessage());
            }
        }
    }

    public void takeTurn() {
        // play a card

        // oracle or figure

        //oracle: which number

        //figure: which field

        //special action field / special action oracle

        //**rare** if 3 Goblin
    }

    public void setView(View view) {
        this.view = view;
    }

}
