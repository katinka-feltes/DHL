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
        model.createDecks();

        while(!model.gameOver()){ //round should not be finished
            for (Player activeP: model.getPlayers()){
                if(!model.gameOver()) {
                    takeTurn(activeP);
                }
                else {
                    view.printResults(model);
                }
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
                player.placeFigure(card.getColor(), view.promptInt(1, 3,
                        "Which figure do you want to move? (1, 2 or 3 - the figure the furthest away from the start is 1)"));
                break;
            } catch (Exception e) {
                view.error(e.getMessage());
            }
        }

        //special action field / special action oracle (only when game is not over!)

        //**rare** if 3 Goblin

    }

    /**
     *
     * @param player
     */
    public void takeTurn(Player player) {
        try{
            boolean startTurn = false;
            while (!startTurn) {
                if (view.promptPlayersChoice(player.getName() + " it is your turn. Are you ready to play?")) {
                    startTurn = true;
                }
            }
        } catch (Exception e){
                System.out.println(e.getMessage());
        }

        //maybe all prints into one method in view?
        System.out.println(player.getName() + "'s Turn!"); // view needs to do this !!
        view.printCurrentBoard(model);
        view.printTopCards(player);
        view.printHand(player);

        // play or discard a card
        Card trashCard = null;
        // check if player can play a card and if yes ask
        if (player.canPlay() && view.promptPlayersChoice("Do you want to play a card? (if no you trash one)")){
            playCard(player);
        } else {
            while (true) {
                try {
                    String cardAsString = view.promptCardString("What card do you want to trash?");
                    trashCard = player.getCardFromHand(cardAsString);
                    model.putCardOnDiscardingPile(trashCard, player);
                    break;
                } catch (Exception e) {
                    view.error(e.getMessage());
                }
            }
        }

        // draw cards up to eight again either from one discarding or from drawing pile
        if(model.canDrawFromDiscarding(trashCard)) {
            view.printDiscardingPiles(model);
        }
        if(model.canDrawFromDiscarding(trashCard) && view.promptPlayersChoice("Do you want to draw your card from one of the discarding piles? (if not, you draw from the drawing pile)")){
            char color = view.promptColor("From what colored pile do you want to draw?");

            while (true) {
                try {
                    switch (color) {
                        case ('r'):
                            player.drawFromDiscardingPile(model.getDiscardingPileRed(), trashCard);
                            break;
                        case ('g'):
                            player.drawFromDiscardingPile(model.getDiscardingPileGreen(), trashCard);
                            break;
                        case ('b'):
                            player.drawFromDiscardingPile(model.getDiscardingPileBlue(), trashCard);
                            break;
                        case ('p'):
                            player.drawFromDiscardingPile(model.getDiscardingPilePurple(), trashCard);
                            break;
                        case ('o'):
                            player.drawFromDiscardingPile(model.getDiscardingPileOrange(), trashCard);
                            break;
                    }
                    break;
                } catch (Exception e){
                    view.error(e.getMessage());
                    color = view.promptColor("From what colored pile do you want to draw?");
                }
            }
        } else {
            player.drawFromDrawingPile(model.getDrawingPile());
        }
    }

    public void setView(View view) {
        this.view = view;
    }

}
