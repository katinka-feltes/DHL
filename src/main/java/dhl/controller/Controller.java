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

        for (int i = 0; i<10; i++){
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
                card = player.getCardFromHand(cardAsString); //number 10 needs to be added
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
                        "Which figure do you want to move? (Type the index of the field)"));
                break;
            } catch (Exception e) {
                view.error(e.getMessage());
            }
        }

        //special action field / special action oracle

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
