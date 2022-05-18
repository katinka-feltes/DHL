package dhl.controller;

import dhl.model.*;
import dhl.model.tokens.Goblin;
import dhl.model.tokens.Spiral;
import dhl.model.tokens.Token;
import dhl.view.View;

/**
 * This Class is the Controller of the MVC pattern. It is responsible for the communication between the Model and the View.
 */
public class Controller {
    View view;
    Game model;

    /**
     * This method starts and keeps the game running.
     * The game starts with the input of the players names. While the game is not over the active player can take turn.
     */
    public void startGame() {
        String[] playerName = view.inputPlayersNames(view.promptInt(2, 4, "How many players? (2, 3 or 4)"));
        model = new Game(playerName);
        model.createDecks();

        while(!model.gameOver()){
            for (Player activeP: model.getPlayers()){
                if(!model.gameOver()) {
                    takeTurn(activeP);
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

    /**
     * After acceptance to start the turn, the player will trash or play a card.
     * If the player moves onto a field with a token, that action will take place.
     * At the end the hand will be filled up to 8 cards again.
     *
     * @param player the player to take the turn
     */
    public void takeTurn(Player player) {
        view.out(System.lineSeparator().repeat(50)); //clear screen
        waitForConfirmation(player.getName() + " it is your turn. Are you ready to play?");
        view.out(player.getName() + "'s Turn!");
        view.printCurrentBoard(model);
        view.printTopCards(player);
        view.printHand(player);
        // check if player can play a card and if yes ask
        if (player.canPlay() && view.promptPlayersChoice("Do you want to play a card? (if no you trash one)")){

            playCard(player);

            // updating the board
            view.printCurrentBoard(model);
            view.printTopCards(player);
            view.printHand(player);

            // collecting or using tokens
            usingToken(player);

        } else {
            trashCard(player);
        }

        // draw cards up to eight again either from one discarding or from drawing pile
        if(model.canDrawFromDiscarding(player.getLastTrashed())) {
            view.printDiscardingPiles(model);
        }
        drawOne(player);
        waitForConfirmation("Are you done with your turn?");
    }

    /**
     * If the players hand card amount is beneath eight the player draws a card. Either from the discarding piles or
     * from the drawing pile.
     * If the player chooses the discard piles he can choose the color of the pile too.
     * @param player the current player
     */
    private void drawOne(Player player) {
        if(model.canDrawFromDiscarding(player.getLastTrashed())
                && player.getHand().size() < 8
                && view.promptPlayersChoice("Do you want to draw your card from one of the discarding piles? (if not, you draw from the drawing pile)")){

            while (true) {
                try {
                    char color = view.promptColor("From what colored pile do you want to draw?");
                    player.drawFromDiscardingPile(model.getDiscardPileByColor(color));
                    break;
                } catch (Exception e){
                    view.error(e.getMessage());
                }
            }
        } else {
            player.drawFromDrawingPile();
        }
    }

    /**
     * player chooses card to play and then the figure is moved accordingly
     * @param player the player that will play a card
     */
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
                int figurePos = view.promptInt(1, 3,
                        "Which figure do you want to move? (1, 2 or 3 - the figure the furthest away from the start is 1)");
                player.placeFigure(card.getColor(), player.getFigureByPos(figurePos));
                break;
            } catch (IndexOutOfBoundsException indexE){
                view.error("This figure can't move this far.");
            } catch (Exception e) {
                view.error(e.getMessage());
            }
        }
    }

    /**
     * player chooses one card to trash from his hand and trashes it
     * @param player the player that trashes a card
     */
    private void trashCard(Player player) {
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

    /**
     * Method to wait for acceptance of the question
     * @param question the question to accept
     */
    private void waitForConfirmation(String question) {
        while (true) {
            if (view.promptPlayersChoice(question)) {
                break;
            }
        }
    }

    /**
     * this method decides witch token action comes in to play
     * @param player the current player
     */
    private void usingToken(Player player){
        //get the token and remove it from the field if it is collectable
        Token token = Game.FIELDS[player.getLastMovedFigure().getPos()].getToken();

        if (token != null){
            view.out("You found a " + token.getName() + "!");

            switch (token.getName()){

                case("Spiral") :
                    doTokenSpiral(player);
                    break;

                case("Goblin") :
                    doTokenGoblin(player);
                    if(!player.isGoblinSpecialPlayed() && player.allFiguresGoblin() &&
                            view.promptPlayersChoice("Do you want to play your goblin-special? (you would earn "
                            + player.goblinSpecialPoints() + " points)")){
                        player.playGoblinSpecial();
                    }
                    break;

                case("Spiderweb") :
                    if (view.promptPlayersChoice("Your Figure gets moved to " +
                            "figure to the next field with the same color. Do you want to proceed with your action?")) {
                        token.action(player);
                        view.printCurrentBoard(model);
                        usingToken(player);
                    }
                    break;
                case("Skullpoint") :
                    token.action(player);
                    break;
                case("WishingStone") :
                case("Mirror") :
                    Game.FIELDS[player.getLastMovedFigure().getPos()].collectToken();
                    view.printCurrentBoard(model);
                    break;
                default:
                    token.action(player);
                    break;
            }
        } else {
            view.out("There is no token available on this field");
        }
    }

    /**
     * this method controls the Token action
     * @param player the current player
     */
    private void doTokenSpiral(Player player) {
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
    private void doTokenGoblin(Player player) {
        Token token = Game.FIELDS[player.getLastMovedFigure().getPos()].collectToken();

        if (view.promptPlayersChoice("You can discard a card from one of your discard piles " +
                "or from your hand. Do you want to proceed with your action?")) {

            view.printTopCards(player);

            if (view.promptPlayersChoice("Do you want to trash one from your hand? (if no you can trash one card from your discard piles).")) {
                ((Goblin) token).setPileChoice('h');
                    while (true) {
                        String cardAsString;
                        try {
                            view.printHand(player);
                            cardAsString = view.promptCardString("What card do you want to trash?");
                            view.printHand(player);
                            ((Goblin) token).setCardChoice(player.getCardFromHand(cardAsString));
                            token.action(player);
                            drawOne(player);
                            break;
                        } catch (Exception e) {
                            view.error(e.getMessage());
                        }
                    }
            }else {
                while (true){
                        ((Goblin)token).setPileChoice(view.promptColor("From which pile do you want to trash the top card?"));
                        if (((Goblin)token).getPileChoice() == 'r' && !player.getPlayedCardsRed().isEmpty() ||
                                ((Goblin)token).getPileChoice() == 'g' && !player.getPlayedCardsGreen().isEmpty() ||
                                ((Goblin)token).getPileChoice() == 'b' && !player.getPlayedCardsBlue().isEmpty() ||
                                ((Goblin)token).getPileChoice() == 'o' && !player.getPlayedCardsOrange().isEmpty()) {

                            token.action(player);
                            break;
                        } else if (((Goblin)token).getPileChoice() == 'p' && !player.getPlayedCardsPurple().isEmpty()) {
                            token.action(player);
                            break;
                        } else {
                            view.error("This pile is empty! Try another color.");
                        }
                }
            }
        }
    }

    public void setView(View view) {
        this.view = view;
    }
}
