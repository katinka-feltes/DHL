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

        while(!model.gameOver()){ //round should not be finished
            for (Player activeP : model.getPlayers()){
                takeTurn(activeP);
            }
        }
    }

    public void playCard(Player player){

        Card card;

        // choose a card from hand and place it on matching playedCardsPile
        while (true) {
            try {
                // get card as string and check type with view method
                String cardAsString = view.promptCardString("What card do you want to play?");
                card = player.getCardFromHand(cardAsString);
                player.addCardToPlayedCards(card);
                break;
            } catch (Exception e) {
                view.error(e.getMessage());
            }
        }

        // oracle or figure
        //oracle: which number

        //figure: which field
        while (true) {
            try {
                player.placeFigure(card.getColor(), view.promptInt(0, 35,
                        "Which figure do you want to move? (1, 2 or 3 - the figure the furthest away from the start is 1)"));
                break;
            } catch (Exception e) {
                view.error(e.getMessage());
            }
        }

        //special action field / special action oracle (only when game is not over!)

        //**rare** if 3 Goblin

    }

    public void takeTurn(Player player) {

        //maybe all prints into one method in view?
        System.out.println(player.getName() + "'s Turn!"); // view needs to do this !!
        view.printCurrentBoard(model);
        view.printTopCards(player);
        view.printHand(player);

        // play or discard a card
        playCard(player);

        // draw cards up to eight again
        player.drawCardsUpToEight(model.getDrawingPile());
    }

    public void setView(View view) {
        this.view = view;
    }

}
