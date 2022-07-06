package dhl.controller;

import dhl.Constants;
import dhl.Save;
import dhl.controller.player_logic.AI;
import dhl.controller.player_logic.Human;
import dhl.model.*;
import dhl.model.tokens.Goblin;
import dhl.model.tokens.Spiral;
import dhl.model.tokens.Token;
import dhl.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class is the Controller of the MVC pattern when the game is played on the CLI.
 * It is responsible for the communication between the Model and the View.
 */
public class   CliController {
    View view;
    Game model;

    /**
     * This method starts and keeps the game running.
     * The game starts with the input of the players names. While the game is not over the active player can take turn.
     */
    public void startGame() {
        if(view.promptPlayersChoice("load?") && Save.serializeDataIn()!=null){
            model = Save.serializeDataIn();
        } else {
            int playerAmount = view.promptInt(2, 4, "How many players?");
            int aiAmount = view.promptInt(0, playerAmount - 1, "How many AI players?");
            String[] playerNames = view.inputPlayersNames(playerAmount - aiAmount);
            List<Player> players = new ArrayList<>();
            for (int i = 0; i < playerNames.length; i++) {
                players.add(new Player(playerNames[i], Constants.symbols[i], new Human(view)));
            }
            for (int i = 0; i < aiAmount; i++) {
                players.add(new Player("AI" + (i + 1), Constants.symbols[i + playerNames.length], new AI()));
            }
            model = new Game(players);
        }

        Player activeP = model.getPlayers().get(0);
        while (!model.gameOver()) {
            takeTurn(activeP);
            activeP =  model.nextPlayer(); //set the next player as turn is done

            if (view.promptPlayersChoice("exit and save?")){
                try {Save.serializeDataOut(model);}
                catch (Exception e){
                    System.out.println("error with save");
                    System.out.println(e.getMessage());
                }
                return;
            }
        }
        // calculate final victory points and update highscore if necessary
        for (Player p : model.getPlayers()) {
            p.calcTokenPoints();
            model.updateHighscore(p.getVictoryPoints(), p.getName(), p.getPlayerLogic());
        }
        view.printResults(model);
        System.out.println("\nHighscore:" + model.getHighscores());
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
        waitForConfirmation(player.getName() + " it is your turn. Are you ready to play?", player);
        view.out(player.getName() + "'s Turn!");
        view.printCurrentBoard(model);
        view.printTopCards(player);
        view.printHand(player);
        // check if player can play a card and if yes ask
        if (player.canPlay() && player.getPlayerLogic().choose("Do you want to play a card? (if no you trash one)")) {

            playCard(player);

        } else {
            trashCard(player);
        }

        // draw cards up to eight again either from one discarding or from drawing pile
        if (model.canDrawFromDiscarding(player.getLastTrashed())) {
            view.printDiscardingPiles(model);
        }
        drawOne(player);
        waitForConfirmation("Are you done with your turn?", player);
    }

    /**
     * If the players hand card amount is beneath eight the player draws a card. Either from the discarding piles or
     * from the drawing pile.
     * If the player chooses the discard piles he can choose the color of the pile too.
     *
     * @param player the current player
     */
    private void drawOne(Player player) {
        if (model.canDrawFromDiscarding(player.getLastTrashed())
                && player.getHand().size() < 8
                && player.getPlayerLogic().choose("Do you want to draw your card from one of the discarding piles? (if no, you draw from the drawing pile)")) {

            while (true) {
                try {
                    char color = player.getPlayerLogic().choosePileColor("From what colored pile do you want to draw?");
                    player.drawFromDiscardingPile(model.getDiscardPile(color));
                    break;
                } catch (Exception e) {
                    view.error(e.getMessage());
                }
            }
        } else {
            player.drawFromDrawingPile();
        }
    }

    /**
     * player chooses card to play and then the figure is moved accordingly
     *
     * @param player the player that will play a card
     */
    public void playCard(Player player) {

        Card card;

        // choose a card from hand and place it on matching playedCardsPile
        while (true) {
            try {
                // get card as string and check type with view method
                card = player.getPlayerLogic().chooseCard("What card do you want to play?", player.getHand());
                // add card to played cards
                player.getPlayedCards(card.getColor()).add(card);
                // remove card from players hand
                player.getHand().remove(card);
                break;
            } catch (Exception e) {
                view.error(e.getMessage());
            }
        }

        // oracle or figure
        while (true) {
            try {
                boolean chosen = player.getPlayerLogic().choose("Do you want to move a figure? " +
                        "(if no, you move the zombie)");
                if(chosen) {
                    //chose figure
                    moveFigure(player, card);

                    // updating the board
                    view.printCurrentBoard(model);
                    view.printTopCards(player);
                    view.printHand(player);

                    // collecting or using tokens
                    usingToken(player);
                } else {
                    //chose oracle
                    moveOracle(player, card);
                }
                break;
            } catch (Exception e) {
                view.error(e.getMessage());
            }
        }
    }

    private void moveFigure(Player player, Card card) {
        while (true) {
            try {
                Figure chosenFig = player.getPlayerLogic().chooseFigure("Which figure do you want to move? " +
                        "(1, 2 or 3 - the figure the furthest away from the start is 1)", player.getFigures());
                player.placeFigure(card.getColor(), chosenFig);
                break;
            } catch (IndexOutOfBoundsException indexE) {
                view.error("This figure can't move this far.");
            } catch (Exception e) {
                view.error(e.getMessage());
            }
        }
    }

    private void moveOracle(Player player, Card card) {
        while (true) {
            try {
                int steps = player.getPlayerLogic().chooseOracleSteps("How far do you want to move the zombie? " +
                        "(It can move up to " + card.getOracleNumber() + " steps)", card.getOracleNumber());
                player.placeOracle(steps);
                break;
            } catch (IndexOutOfBoundsException indexE) {
                view.error("The oracle can't move past the last field.");
            } catch (Exception e) {
                view.error(e.getMessage());
            }
        }
    }

    /**
     * player chooses one card to trash from his hand and trashes it
     *
     * @param player the player that trashes a card
     */
    private void trashCard(Player player) {
        Card trash = player.getPlayerLogic().chooseCard("What card do you want to trash?", player.getHand());
        player.putCardOnDiscardingPile(trash);
    }

    /**
     * Method to wait for acceptance of the question
     *
     * @param question the question to accept
     */
    private void waitForConfirmation(String question, Player player) {
        while (true) {
            if (player.getPlayerLogic().choose(question)) {
                break;
            }
        }
    }

    /**
     * this method decides which token action is played
     *
     * @param player the current player
     */
    private void usingToken(Player player) {
        //get the token and remove it from the field if it is collectable
        Token token = model.getFields()[player.getLastMovedFigure().getPos()].collectToken();

        if (token != null) {
            view.out("You found a " + token.getName() + "!");

            switch (token.getName()) {

                case "Spiral":
                    doTokenSpiral(player);
                    break;

                case "Goblin":
                    doTokenGoblin(player);
                    doGoblinSpecialAction(player);
                    break;

                case "Spiderweb":
                    if (FigureFunction.spiderwebIsPossible(player.getLastMovedFigure()) &&
                            player.getPlayerLogic().choose("Your Figure gets moved to " +
                                "figure to the next field with the same color. Do you want to proceed with your action?")) {
                        token.action(player);
                        view.printCurrentBoard(model);
                        usingToken(player);
                    } else {
                        view.out("Sadly you cannot execute the spiderweb action");
                    }
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
     *
     * @param player the current player
     */
    private void doTokenSpiral(Player player) {
        Figure currentFigure = player.getLastMovedFigure();
        Token token = model.getFields()[player.getLastMovedFigure().getPos()].collectToken();

        if (player.getPlayerLogic().choose("You can move this figure backwards as far as you want. " +
                "Except the field where you came from. Do you want to proceed with your action?")) {
            int chosenPos = player.getPlayerLogic().chooseSpiralPosition("Where do you want to place your figure?", currentFigure.getPos());
            if (chosenPos != currentFigure.getLatestPos()) {
                ((Spiral) token).setChosenPos(chosenPos);
                token.action(player);
                view.printCurrentBoard(model);
                usingToken(player);
            } else {
                view.error("You can not go to the field you came from!");
                //recursion?
            }
        }
    }

    /**
     * this method controls the Token action
     *
     * @param player the current player
     */
    private void doTokenGoblin(Player player) {
        Token token = model.getFields()[player.getLastMovedFigure().getPos()].collectToken();

        if (player.getPlayerLogic().choose("You can discard a card from one of your discard piles " +
                "or from your hand. Do you want to proceed with your action?")) {
            goblinChosenPile(player, token);
        }

    }

    /**
     * this method controls the pile decision
     *
     * @param player the current player
     * @param token  the Goblin token
     */
    private void goblinChosenPile(Player player, Token token) {

        view.printTopCards(player);

        if (player.getPlayerLogic().choose("Do you want to trash one from your hand? (if no you can trash one card from your discard piles).")) {
            ((Goblin) token).setPileChoice('h');

            try {
                view.printHand(player);
                Card trash = player.getPlayerLogic().chooseCard("What card do you want to trash?", player.getHand());
                ((Goblin) token).setCardChoice(trash);
                token.action(player);
                drawOne(player);
            } catch (Exception e) {
                view.error(e.getMessage());
            }
        } else {
            ((Goblin) token).setPileChoice(player.getPlayerLogic().choosePileColor("From which pile do you want to trash the top card?"));
            if (!player.getPlayedCards(((Goblin) token).getPileChoice()).isEmpty()) {
                token.action(player);
            } else {
                view.error("This pile is empty! Try another color.");
                goblinChosenPile(player, token);
            }
        }
    }

    /**
     * this method controls the goblin special action
     *
     * @param player the current player
     */
    private void doGoblinSpecialAction(Player player) {
        if(!player.isGoblinSpecialPlayed() && player.amountFiguresGoblin()==3 &&
                player.getPlayerLogic().choose("Do you want to play your goblin-special? (you would earn "
                        + player.goblinSpecialPoints() + " points)")) {
            player.playGoblinSpecial();
        }
    }

    public void setView(View view) {
        this.view = view;
    }


}