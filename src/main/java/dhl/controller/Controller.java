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
                       e.getMessage();
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
    public void takeTurn(Player player) throws Exception {
            boolean startTurn = false;
            while (!startTurn) {
                if (view.promptPlayersChoice(player.getName() + " it is your turn. Are you ready to play?")) {
                    startTurn = true;
                }
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
        // updating the board
        view.printCurrentBoard(model);
        view.printTopCards(player);
        view.printHand(player);

        // collecting or using tokens
        usingToken(player);

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
            player.drawCardsUpToEight(model.getDrawingPile());
        }
    }

    /**
     *
     * @param player
     * @throws Exception
     */
    private void usingToken(Player player){
        int fieldIndex = player.getLastMovedFigure().getPos();
        String name = player.getLastMovedFigure().getField(fieldIndex).getToken().getName();
        Figure currentFigure = player.getLastMovedFigure();
        boolean choice;

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
                    view.out("You found a Skullpoint!");
                    doTokenAction(player, model);
                    break;

                case("Spiral") :
                    view.out("You found a Spiral!");
                    choice = view.promptPlayersChoice("You can move this figure backwards as far as you want. " +
                            "Except the field where you came from. " +
                            "Do you want to proceed with your action?");
                    if (choice) {
                        doTokenChoosePos(player, view.promptInt(0, fieldIndex,
                                "Where do you want to place your figure?"));
                        doTokenAction(player, model);
                    }
                        break;

                    case("Goblin") :
                    view.out("You found a Goblin!");
                    choice = view.promptPlayersChoice("You can discard a card from one of your discardpiles " +
                            "or from your hand. " +
                            "Do you want to proceed with your action?");
                    if (choice) {
                        doTokenPileChoice(player, view.promptColorAndHand("You can choose out of the following: " +
                                "r, g, b, p, o, h (hand)."));
                        if (player.getLastMovedFigure().getField(fieldIndex).getToken().getPileChoice() == 'h') {
                            doTokenCardChoice(player, view.promptCardString("What card do you want to trash?"));
                            doTokenAction(player, model);
                        }
                        doTokenAction(player, model);
                    }
                        break;

                    case("SpiderWeb") :
                    view.out("You found a Spiderweb!");
                    choice = view.promptPlayersChoice("You are allowed to move your " +
                            "figure to the next field with the same color. " +
                            "Do you want to proceed with your action?");

                    if (choice) {
                            doTokenChoosePos(player, view.promptInt(fieldIndex, 39,
                                    "Where do you want to place your figure?"));
                            doTokenAction(player, model);

                    }
                        break;
            }
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
    public void doTokenAction(Player player, Game model) {
        try{
            player.getLastMovedFigure().getField(player.getLastMovedFigure().getPos()).getToken().action(model, player);
        } catch (Exception e){
            view.error(e.getMessage());
        }

    }

    /**
     * calls the setChosenPos() method from the token
     * @param player the current player
     * @param pos the chosen position
     */
    public void doTokenChoosePos(Player player, int pos){
        player.getLastMovedFigure().getField(player.getLastMovedFigure().getPos()).getToken().
                setChosenPos(pos);
    }

    /**
     * calls the setPileChoice() method from the token
     * @param player the current player
     * @param color the chosen pile color
     */
    public void doTokenPileChoice(Player player, char color){
        player.getLastMovedFigure().getField(player.getLastMovedFigure().getPos()).getToken().setPileChoice(color);
    }

    /**
     * calls the setCardChoice() method from the Token
     * @param player the current player
     * @param card the chosen card
     */
    public void doTokenCardChoice(Player player, String card) {
        player.getLastMovedFigure().getField(player.getLastMovedFigure().getPos()).getToken().setCardChoice(card);
    }
}
