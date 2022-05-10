package dhl.controller;

import dhl.model.Card;
import dhl.model.Figure;
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
                    try {
                        takeTurn(activeP);
                    } catch (Exception e) {
                       view.error(e.getMessage());
                    }
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
        int fieldIndex = player.getLastMovedFigure().getPos();
        String name = player.getLastMovedFigure().getField(fieldIndex).getToken().getName();
        Figure currentFigure = player.getLastMovedFigure();

        if (currentFigure.getField(fieldIndex).getToken().isCollectable()){

            switch (name) {
                case ("WishingStone"):
                    view.out("You found a Wishing Stone!");
                    doCollectToken(player);
                    break;

                case ("Mirror"):
                    view.out("You found a Mirror");
                    doCollectToken(player);
                    break;
            }
        } else {
            switch (name){

                case("Skullpoints") :
                    doTokenSkullpoints(player, model);
                    break;

                case("Spiral") :
                    doTokenSpiral(player, model, currentFigure, fieldIndex);
                    break;

                case("Goblin") :
                    doTokenGoblin(player, model, fieldIndex);
                    break;

                case("SpiderWeb") :
                    doTokenSpiderWeb(player, model);
                    break;
            }
        }
    }

    /**
     * this method controls the Token action
     * @param player the current player
     * @param model the current game status
     */
    public void doTokenSkullpoints(Player player, Game model) {
        view.out("You found a Skullpoint!");
        getTokenAction(player, model);
    }

    /**
     * this method controls the Token action
     * @param player the current player
     * @param model the current game status
     * @param currentFigure the current Figure
     * @param fieldIndex the field index of the field the token lies on
     */
    public void doTokenSpiral(Player player, Game model, Figure currentFigure, int fieldIndex) {
        boolean choice;
        int chosenPos;

        view.out("You found a Spiral!");
        choice = view.promptPlayersChoice("You can move this figure backwards as far as you want. " +
                "Except the field where you came from. " +
                "Do you want to proceed with your action?");
        if (choice) {
            chosenPos = view.promptInt(0, fieldIndex,
                    "Where do you want to place your figure?");
            if (chosenPos != currentFigure.getLatestPos()){
                setTokenChoosePos(player, chosenPos);
                getTokenAction(player, model);
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
     * @param model the current game status
     * @param fieldIndex the field index of the field the token lies on
     */
    public void doTokenGoblin(Player player, Game model, int fieldIndex) {
        boolean choice;

        view.out("You found a Goblin!");
        choice = view.promptPlayersChoice("You can discard a card from one of your discardpiles " +
                "or from your hand. " +
                "Do you want to proceed with your action?");
        if (choice) {
            setTokenPileChoice(player, view.promptColorAndHand("You can choose out of the following: " +
                    "r, g, b, p, o, h (hand)."));
            if (player.getLastMovedFigure().getField(fieldIndex).getToken().getPileChoice() == 'h') {
                setTokenCardChoice(player, view.promptCardString("What card do you want to trash?"));
                getTokenAction(player, model);
            }
            getTokenAction(player, model);
        }
    }

    /**
     * this method controls if the Token action is valid and does the token action
     * @param player the current player
     * @param model the current game status
     */
    public void doTokenSpiderWeb(Player player, Game model) {
        boolean choice;

        view.out("You found a Spiderweb!");
        choice = view.promptPlayersChoice("Your Figure gets moved to " +
                "figure to the next field with the same color. " +
                "Do you want to proceed with your action?");
        if (choice) {
            getTokenAction(player, model);
            view.printCurrentBoard(model);
            usingToken(player);
        }
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * calls the collectToken() method.
     * @param player the current player
     */
    public void doCollectToken(Player player) {
        player.getLastMovedFigure().getField(player.getLastMovedFigure().getPos()).collectToken();
    }

    /**
     * calls the action() method from the token
     * @param player the current player
     * @param model the current game status
     */
    public void getTokenAction(Player player, Game model) {
            player.getLastMovedFigure().getField(player.getLastMovedFigure().getPos()).getToken().action(model, player);
    }

    /**
     * calls the setChosenPos() method from the token
     * @param player the current player
     * @param pos the chosen position
     */
    public void setTokenChoosePos(Player player, int pos){
        player.getLastMovedFigure().getField(player.getLastMovedFigure().getPos()).getToken().
                setChosenPos(pos);
    }

    /**
     * calls the setPileChoice() method from the token
     * @param player the current player
     * @param color the chosen pile color
     */
    public void setTokenPileChoice(Player player, char color){
        player.getLastMovedFigure().getField(player.getLastMovedFigure().getPos()).getToken().setPileChoice(color);
    }

    /**
     * calls the setCardChoice() method from the Token
     * @param player the current player
     * @param card the chosen card
     */
    public void setTokenCardChoice(Player player, String card) {
        player.getLastMovedFigure().getField(player.getLastMovedFigure().getPos()).getToken().setCardChoice(card);
    }
}
