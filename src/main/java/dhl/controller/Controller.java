package dhl.controller;

import dhl.model.Card;
import dhl.model.Figure;
import dhl.model.Game;
import dhl.model.Player;
import dhl.model.tokens.Goblin;
import dhl.model.tokens.Spiral;
import dhl.model.tokens.Token;
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
                    try {
                        takeTurn(activeP);
                    } catch (Exception e) {
                       view.error(e.getMessage());
                    }
                }
                else {
                    for (Player p : model.getPlayers()){
                        p.calcTokenPoints();
                    }
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
            } catch (IndexOutOfBoundsException indexE){
                view.error("This figure can't move this far.");
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
        view.out(System.lineSeparator().repeat(50)); //clear screen
        while (true) {
            if (view.promptPlayersChoice(player.getName() + " it is your turn. Are you ready to play?")) {
                break;
            }
        }
        view.out(player.getName() + "'s Turn!");
        view.printCurrentBoard(model);
        view.printTopCards(player);
        view.printHand(player);
        // check if player can play a card and if yes ask
        if (player.canPlay() && view.promptPlayersChoice("Do you want to play a card? (if no you trash one)")){
            playCard(player);
        } else {
            while (true) {
                try {
                    String cardAsString = view.promptCardString("What card do you want to trash?");
                    player.setLastTrashed(player.getCardFromHand(cardAsString));
                    player.putCardOnDiscardingPile(player.getLastTrashed());
                    break;
                } catch (Exception e) {
                    view.error(e.getMessage());
                }
            }
        }
        // updating the board
        view.printCurrentBoard(model);
        view.printTopCards(player);
        view.printHand(player);

        // collecting or using tokens
        usingToken(player);

        // draw cards up to eight again either from one discarding or from drawing pile
        if(model.canDrawFromDiscarding(player.getLastTrashed())) {
            view.printDiscardingPiles(model);
        }
        if(model.canDrawFromDiscarding(player.getLastTrashed()) && view.promptPlayersChoice("Do you want to draw your card from one of the discarding piles? (if not, you draw from the drawing pile)")){
            char color = view.promptColor("From what colored pile do you want to draw?");

            while (true) {
                try {
                    switch (color) {
                        case ('r'):
                            player.drawFromDiscardingPile(model.getDiscardingPileRed());
                            break;
                        case ('g'):
                            player.drawFromDiscardingPile(model.getDiscardingPileGreen());
                            break;
                        case ('b'):
                            player.drawFromDiscardingPile(model.getDiscardingPileBlue());
                            break;
                        case ('p'):
                            player.drawFromDiscardingPile(model.getDiscardingPilePurple());
                            break;
                        case ('o'):
                            player.drawFromDiscardingPile(model.getDiscardingPileOrange());
                            break;
                        default:
                    }
                    break;
                } catch (Exception e){
                    view.error(e.getMessage());
                    color = view.promptColor("From what colored pile do you want to draw?");
                }
            }
        } else {
            player.drawCardsUpToEight(model.getDrawingPile());
        }
        while (true) {
            if (view.promptPlayersChoice("are you done with your turn?")) {
                break;
            }
        }
    }

    /**
     * this method decides witch token action comes in to play
     *
     * @param player the current player
     */
    private void usingToken(Player player){
        //get the token and remove it from the field if it is collectable
        Token token = Game.FIELDS[player.getLastMovedFigure().getPos()].collectToken();
        if (token != null){
            view.out("You found a " + token.getName() + "!");

            switch (token.getName()){

                case("Spiral") :
                    doTokenSpiral(player);
                    break;

                case("Goblin") :
                    doTokenGoblin(player);
                    break;

                case("SpiderWeb") :
                    if (view.promptPlayersChoice("Your Figure gets moved to " +
                            "figure to the next field with the same color. Do you want to proceed with your action?")) {
                        token.action(player);
                        view.printCurrentBoard(model);
                        usingToken(player);
                    }
                    break;

                default:
                    token.action(player);
                    break;
            }
        }
    }

    /**
     * this method controls the Token action
     * @param player the current player
     */
    public void doTokenSpiral(Player player) {
        Figure currentFigure = player.getLastMovedFigure();
        Token token = Game.FIELDS[player.getLastMovedFigure().getPos()].collectToken();

        if (view.promptPlayersChoice("You can move this figure backwards as far as you want. " +
                "Except the field where you came from. Do you want to proceed with your action?")) {
            int chosenPos = view.promptInt(0, currentFigure.getPos(),
                    "Where do you want to place your figure?");
            if (chosenPos != currentFigure.getLatestPos()){
                ((Spiral)token).setChosenPos(chosenPos);
                token.action(player);
                view.printCurrentBoard(model);
                usingToken(player);
            } else {
                view.error("You can not go to the field you came from!");
            }
        }
    }

    /**
     * this method controls the Token action
     * @param player the current player
     */
    public void doTokenGoblin(Player player) {
        Token token = Game.FIELDS[player.getLastMovedFigure().getPos()].collectToken();

        if (view.promptPlayersChoice("You can discard a card from one of your discard piles " +
                "or from your hand. Do you want to proceed with your action?")) {

            ((Goblin)token).setPileChoice(view.promptColorAndHand("You can choose out of the following: " +
                    "r, g, b, p, o, h (hand)."));
            if (((Goblin)token).getPileChoice() == 'h') {
                    while (true) {
                        String cardAsString;
                        try {
                            cardAsString = view.promptCardString("What card do you want to trash?");
                            ((Goblin) token).setCardChoice(player.getCardFromHand(cardAsString));
                            token.action(player);
                            break;
                        } catch (Exception e) {
                            view.error(e.getMessage());
                        }
                    }
            }
            token.action(player);
        }
    }

    public void setView(View view) {
        this.view = view;
    }
}
