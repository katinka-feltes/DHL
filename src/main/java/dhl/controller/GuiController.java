package dhl.controller;

import dhl.controller.player_logic.AI;
import dhl.controller.player_logic.Human;
import dhl.controller.player_logic.PlayerLogic;
import dhl.model.*;
import dhl.model.tokens.Goblin;
import dhl.model.tokens.Spiral;
import dhl.model.tokens.Token;
import dhl.view.GuiUpdate;
import dhl.view.View;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static dhl.view.GuiUpdate.updateCards;

/**
 * This Class is the Controller of the MVC pattern when the game is played on the GUI.
 * It is responsible for the communication between the Model and the View.
 */
public class GuiController {
    private State state;
    private Card chosenCard;
    private Figure chosenFigure;
    private Player activeP;
    /**
     * This Label shows what the player has to do.
     */
    @FXML
    private Label toDo;
    /**
     * This is the root Pane of the current stage
     */
    @FXML
    private BorderPane root;
    static Game model;
    View view;
    /**
     * contains the name of the current player
     */
    @FXML
    private Label playerName;

    /**
     * gets all the children of a given Parent
     * @param parent the parent you want to get the children from
     * @return a list of all the children of the parent
     */
    public List<Node> getAllChildren(Parent parent) {
        ArrayList<Node> children = new ArrayList<>();
        for (Node node : parent.getChildrenUnmodifiable()) {
            children.add(node);
            if (node instanceof Parent)
                children.addAll(getAllChildren((Parent) node));
        }
        return children;
    }

    /**
     * classifies all the children of the root pane as per a given String
     * @param searchId the classification string
     * @return all the matching children that start with the given string
     */
    public List<Node> classifyChildren(String searchId) {
        List<Node> nodes = new ArrayList<>();
        for (Node node : getAllChildren(root)) {
            if (node.getId() != null && node.getId().startsWith(searchId)) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    /**
     * This method is called when the user clicks on the "Start Game" button.
     * @param event the click on the Button
     * @throws IOException if the FXML file is not found
     */
    @FXML
    public void startGame(ActionEvent event) throws IOException {

        List<Node> choiceBoxes = classifyChildren("choiceBox");
        List<Node> names = classifyChildren("name");
        List<Node> ais = classifyChildren("ai");

        List<Player> players = new ArrayList<>();

        // at least one name has to be entered and one name or ai
        if (((TextField)names.get(0)).getText().isEmpty() || ((TextField)names.get(1)).getText().isEmpty()
                && !((CheckBox)ais.get(0)).isSelected()) {
            return;
        } else { //if all criteria are passed, the first player who must be human is added
            String enteredName = ((TextField)names.get(0)).getText();
            char symbol = ((String)((ChoiceBox)choiceBoxes.get(0)).getSelectionModel().getSelectedItem()).charAt(0);
            players.add(new Player(enteredName, symbol, new Human(view)));
        }

        // add the other 3 players if they are entered
        for (int i = 1; i < 4; i++) {
            char symbol = ((String)((ChoiceBox)choiceBoxes.get(i)).getSelectionModel().getSelectedItem()).charAt(0);
            if (((CheckBox)ais.get(i-1)).isSelected()) { //if the player should be an ai
                players.add(new Player("COM" + (i), symbol, new AI()));
            } else if (!((TextField)names.get(i)).getText().isEmpty()) {
                String enteredName = ((TextField)names.get(i)).getText();
                players.add(new Player(enteredName, symbol, new Human(view)));
            }
        }

        model = new Game(players);

        loadNewScene(event, "/gui.fxml", true, true);

        state = State.PREPARATION;
        activeP = model.getPlayers().get(0);
        toDo.setText("Click any item to start your turn.");
        updateAll();
    }

    /**
     * loads a new scene
     * @param event event that triggers the new scene
     * @param sceneFile scene that should be loaded
     * @param max if setMaximized should be set or not
     * @throws IOException if the FXML file is not found
     */
    private void loadNewScene(Event event, String sceneFile, boolean max, boolean setController) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneFile));
        if (setController) {
            fxmlLoader.setController(this);
        }
        root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(max);
        stage.centerOnScreen();
    }

    /**
     * This method is called when the user clicks on any clickable item in the Game.
     * @param e the click on the item
     * @throws IOException if the FXML file is not found
     */
    @FXML
    public void onClick(MouseEvent e) throws IOException {
        Node item = (Node) e.getSource();
        if(model.gameOver()) { //game over screen
            for (Player p : model.getPlayers()) {
                p.calcTokenPoints();
                model.updateHighscore(p.getVictoryPoints(), p.getName(), p.getPlayerLogic());
            }
            loadNewScene(e, "/end.fxml", false, true);
            createEndScores();
        } else if (state == State.PREPARATION) { //click anything to start the turn
            chosenFigure = null;
            chosenCard = null;
            activeP.setLastTrashed(null);
            state = State.CHOOSEHANDCARD;
            toDo.setText("Which card to you want to play or trash?");

            PlayerLogic pl = activeP.getPlayerLogic();
            if (pl instanceof AI) {
                takeTurnAI((AI) pl);
            }
        } else if ((state == State.CHOOSEHANDCARD || state == State.TRASHORPLAY) && item.getId().startsWith("handCard")) {
            chosenCard = activeP.getHand().get(getIndex(item.getId(), "handCard"));
            toDo.setText("Click a figure to move or trash it.");
            state = State.TRASHORPLAY;
        } else if (state == State.TRASHORPLAY) {
            trashOrPlay(item);
        } else if (state == State.SPIRAL && item.getId().startsWith("circle")) {
            spiral(item);
        } else if (state == State.SPIDERWEB) {
            spiderweb(item);
        } else if (state == State.GOBLIN) {
            goblin(item);
        } else if (state == State.DRAW && draw(item)) {
            return;
        }
        if(item.getId().startsWith("menu")) {
            loadNewScene(e, "/start.fxml", false, false);
            return;
        }
        updateAll();
    }

    private boolean draw(Node item) {
        if (item.getId().startsWith("discardingPile")) {
            try {
                activeP.drawFromDiscardingPile(getDiscardPileFromID(item.getId())); //draw one card from chosen pile
            } catch (Exception exc) {
                toDo.setText(exc.getMessage());
                return true;
            }
        } else if (item.getId().equals("drawingPile")) {
            activeP.drawFromDrawingPile();
        }
        if(activeP.getHand().size() == 8){
            activeP = getNextPlayer();
            toDo.setText("Click any item to start your turn.");
            state = State.PREPARATION;
        }
        return false;
    }

    private void goblin(Node item) {
        if (item.getId().startsWith("circle") && activeP.amountFiguresGoblin() == 3 ) {
            activeP.playGoblinSpecial();
            toDo.setText("Click on a card you want to discard, either from your hand\nor from your played cards." +
                    " To say no click one discarding pile.");
        } else if (item.getId().startsWith("handCard")) {
            useGoblinHand(activeP.getHand().get(getIndex(item.getId(), "handCard")));
        } else if (item.getId().startsWith("playedCardsNumber" + (model.getPlayers().indexOf(activeP)+1))) {
            useGoblinPile(getPlayedCardsFromID(item.getId()));
        } else if (item.getId().startsWith("playedCardsNumber")) {
            toDo.setText("This is not your played cards pile.");
        } else {
            state = State.DRAW;
            toDo.setText("From which pile do you want to draw?");
        }
    }

    private void spiderweb(Node item) {
        if (item.getId().startsWith("circle")) {
            useSpiderweb();
        } else {
            state = State.DRAW;
            toDo.setText("From which pile do you want to draw?");
        }
    }

    private void spiral(Node item) {
        int chosenPos = getIndex(item.getId(), "circle");
        if (chosenPos == chosenFigure.getPos()) { //click the current field to not use the spiral
            state = State.DRAW;
            toDo.setText("From which pile do you want to draw?");
        } else {
            useSpiral(chosenPos);
        }
    }

    private void trashOrPlay(Node item) {
        if (item.getId().startsWith("discardingPile")) {
            trash(chosenCard);
        } else if (item.getId().startsWith("circle")) {
            try {
                chosenFigure = FigureFunction.getFigureOnField(getIndex(item.getId(), "circle"), activeP.getFigures());
                play();
            } catch (Exception exception) {
                toDo.setText(exception.getMessage());
            }
        }
    }

    private void updateAll() {
        //update cards
        updateCards(classifyChildren("handCard"), state, activeP);
        //update discarding piles
        updatePlayedCardsAndNames();
        // update tokens
        GuiUpdate.updateTokens(classifyChildren("token"));
        // update figures
        GuiUpdate.updateFigures(classifyChildren("circle"), model.getPlayers());
        // update scores
        GuiUpdate.updateScores(classifyChildren("scorePlayer"), classifyChildren("scoreName"), model.getPlayers());
        //update discard piles
        GuiUpdate.updateDiscardPiles(classifyChildren("discardingPile"));
        //update collected tokens
        GuiUpdate.updateCollectedTokens(activeP, (HBox)classifyChildren("collectedToken").get(0));
        //write name and symbol of active player
        playerName.setText(activeP.getName() + ": " + activeP.getSymbol());
    }

    /**
     * a method for the AI to take a turn. Only call this if the current player is an AI
     */
    private void takeTurnAI(AI ai) {
        if(ai.choose("Do you want to play a card?")) {
            try {
                chosenCard = ai.chooseCard("What card do you want to play?", null);
                chosenFigure = ai.chooseFigure("", null);

                play();

                //ai always wants to use token
                while (state != State.DRAW) {
                    switch (state) {
                        case SPIDERWEB:
                            useSpiderweb();
                            break;
                        case GOBLIN:
                            goblinAi(ai);
                            break;
                        case SPIRAL:
                            useSpiral(ai.chooseSpiralPosition("", chosenFigure.getPos()));
                            break;
                        default:
                            //do nothing
                    }
                }
            } catch (Exception e) {
                System.err.println("The ai made a incorrect choice that should not occur.");
            }
        } else {
            trash(ai.chooseCard("What card do you want to trash?", null));
        }
        // draw to end turn
        updateAll();
        activeP.drawFromDrawingPile();
        activeP = getNextPlayer();
        state = State.PREPARATION;
        toDo.setText("The AI is done with its turn. Press any Card to continue.");
    }

    /**
     *
     * @param ai the playerlogic of the current player if it's an ai
     */
    private void goblinAi(AI ai) {
        if (!activeP.isGoblinSpecialPlayed() && activeP.amountFiguresGoblin() == 3 &&
            ai.choose("Do you want to play your goblin-special?")) {
            activeP.playGoblinSpecial();
        }
        if (ai.choose("Do you want to trash one from your hand?")) {
            useGoblinHand(ai.bestHandCardToTrash());
        } else {
            char pilecolor = ai.choosePileColor("From which pile do you want to trash the top card?");
            useGoblinPile(activeP.getPlayedCards(pilecolor));

            int currentPlayerIndex = model.getPlayers().indexOf(activeP) + 1;
            List<Node> currentDiscardPile = classifyChildren("playedCardsNumber" +currentPlayerIndex + pilecolor);
            ((Label)currentDiscardPile.get(0)).setText("");
        }
    }

    private void play() {
        toDo.setText("it's your turn. Click any card to start.");
        try {
            activeP.getPlayedCards(chosenCard.getColor()).add(chosenCard);
            activeP.getHand().remove(chosenCard);

            activeP.placeFigure(chosenCard.getColor(), chosenFigure);

            useToken();

        } catch (IndexOutOfBoundsException indexE) {
            toDo.setText("This figure can't move this far! \nChoose a different one or trash.");
            state = State.TRASHORPLAY; //choose again what to do with the chosen card
        } catch (Exception e) {
            state = State.TRASHORPLAY;
            toDo.setText(e.getMessage());
        }
    }

    private void useSpiderweb() {
        Game.FIELDS[chosenFigure.getPos()].getToken().action(activeP);
        updateCards(classifyChildren("handCard"), state, activeP);
        useToken(); // sets the new state
    }

    /**
     * executes goblin action if player chose a hand card to trash
     * @param card the player picked to trash
     */
    private void useGoblinHand(Card card) {
        ((Goblin) Game.FIELDS[chosenFigure.getPos()].getToken()).setPileChoice('h');
        ((Goblin) Game.FIELDS[chosenFigure.getPos()].getToken()).setCardChoice(card);
        Game.FIELDS[chosenFigure.getPos()].getToken().action(activeP);
        state = State.DRAW;
        toDo.setText("From which pile do you want to draw?");
    }

    /**
     * executes goblin action if player chose a played cards top card to trash
     * @param pile the player wants to trash the top card from
     */
    private void useGoblinPile(DirectionDiscardPile pile) {
        ((Goblin) Game.FIELDS[chosenFigure.getPos()].getToken()).setPileChoice(pile.getColor());
        Game.FIELDS[chosenFigure.getPos()].getToken().action(activeP);
        state = State.DRAW;
        toDo.setText("From which pile do you want to draw?");

        int currentPlayerIndex = model.getPlayers().indexOf(activeP) + 1;
        List<Node> currentDiscardPileName = classifyChildren("playedCardsNumber" +currentPlayerIndex + pile.getColor());
        List<Node> currentDiscardPileDir = classifyChildren("playedCardsDir" +currentPlayerIndex + pile.getColor());
        ((Label) currentDiscardPileDir.get(0)).setText("" + pile.getDirectionString());
        if (pile.isEmpty()) {
            ((Label) currentDiscardPileName.get(0)).setText("");
        } else {
            ((Label) currentDiscardPileName.get(0)).setText("" + pile.getTop().getNumber());
        }

    }

    /**
     * executes spiral action
     * @param chosenPos position the player chose for his figure to move to
     */
    private void useSpiral (int chosenPos){
        Token token = Game.FIELDS[activeP.getLastMovedFigure().getPos()].getToken();

        if (chosenPos != chosenFigure.getLatestPos() && chosenPos < chosenFigure.getPos()) { //correct field chosen
            ((Spiral) token).setChosenPos(chosenPos);
            token.action(activeP);
            updateCards(classifyChildren("handCard"), state, activeP);
            useToken(); //sets the new state
        } else {
            toDo.setText("You can not go to the field you came from!");
        }
    }

    /**
     * trashs a card the player chose
     * @param card card the player chose to trash
     */
    private void trash(Card card) {
        activeP.putCardOnDiscardingPile(card); //places card on pile and removes it from hand
        state = State.DRAW;
        toDo.setText("From which pile do you want to draw?");
    }

    private void useToken() {
        Token token = Game.FIELDS[chosenFigure.getPos()].collectToken();

        if (token == null) {
            state = State.DRAW;
            toDo.setText("From which pile do you want to draw?");
            return;
        }

        switch (token.getName()) {

            case "Spiral":
                state = State.SPIRAL;
                toDo.setText("Click the field you want to go back to (except the one you came from) " +
                        "\nor the one you are on to not use the action.");
                break;

            case "Goblin":
                state = State.GOBLIN;
                if (!activeP.isGoblinSpecialPlayed() && activeP.amountFiguresGoblin() == 3) {
                    toDo.setText("Do you want to play your goblin special action " +
                            "(and get " + activeP.goblinSpecialPoints() + ") points. \nIf yes, click a field. " +
                            "If no, just click the card you want to trash with the goblin action" +
                            "\nor one discarding pile to do nothing. ");
                } else {
                    toDo.setText("Click on a card you want to discard, either from your hand\nor from your played cards." +
                            " To say no click one discarding pile.");
                }
                break;

            case "Spiderweb":
                if (FigureFunction.spiderwebIsPossible(activeP.getLastMovedFigure())) {
                    state = State.SPIDERWEB;
                    toDo.setText("Click any field to go to the next same colored field. \nTo say no click one discarding pile.");
                } else {
                    state = State.DRAW;
                    toDo.setText("From which pile do you want to draw?");
                }
                break;
            default:
                token.action(activeP);
                state = State.DRAW;
                toDo.setText("You found a " + token.getName()+ ". From which pile do you want to draw?");
                break;
        }
    }
    private void updatePlayedCardsAndNames() {
        int currentPlayerIndex = model.getPlayers().indexOf(activeP) + 1;
        if (chosenCard != null && activeP.getPlayedCards(chosenCard.getColor()).getTop() != null &&
                (state == State.DRAW || state == State.SPIRAL || state == State.SPIDERWEB || state == State.GOBLIN)) {
            char currentCardColor = activeP.getPlayedCards(chosenCard.getColor()).getTop().getColor();
            List<Node> currentDiscardPileNumber = classifyChildren("playedCardsNumber" + currentPlayerIndex + currentCardColor);
            List<Node> currentDiscardPileDir = classifyChildren("playedCardsDir" + currentPlayerIndex + currentCardColor);

            ((Label) currentDiscardPileNumber.get(0)).setText("" + chosenCard.getNumber());
            ((Label) currentDiscardPileDir.get(0)).setText("" + activeP.getPlayedCards(currentCardColor).getDirectionString());
        }
        List<Node> namesPlayedCards = classifyChildren("namePlayedCards");
        for (int i = 0; i < 4; i++) {
            if (i >= model.getPlayers().size()) {
                ((Label)namesPlayedCards.get(i)).setText("");
            } else {
                ((Label)namesPlayedCards.get(i)).setText(model.getPlayers().get(i).getName());
            }
        }
    }

    /**
     * adds the player names on end screen (in order: winning - losing)
     */
    private void createEndScores() {
        List<Node> endScores = classifyChildren("endscores");
        for (int i = 0; i < 4; i++) {
            if (i > model.getPlayers().size() - 1) {
                ((Label)endScores.get(i)).setText("");
            } else {
                List<Player> winnerList = model.getSortedPlayers();
                ((Label)endScores.get(i)).setText(winnerList.get(i).getName());
            }
        }
    }

    /**
     * gets the matching discard pile to color in id
     *
     * @param id the piles' id with the color as the last character
     * @return the discard pile from the model
     */
    public static DiscardPile getDiscardPileFromID(String id) {
        char[] idArray = id.toCharArray();
        return model.getDiscardPile(idArray[idArray.length - 1]);
    }
    /**
     * gets the matching direction discard pile to color in id
     *
     * @param id the piles' id with the color as the last character
     * @return the direction discard pile from the player
     */
    private DirectionDiscardPile getPlayedCardsFromID(String id) {
        char[] idArray = id.toCharArray();
        return activeP.getPlayedCards(idArray[idArray.length - 1]);
    }

    private int getIndex(String id, String elementName) {
        String[] x = id.split(elementName);
        return Integer.parseInt(x[1]);
    }

    private Player getNextPlayer() {
        int index = model.getPlayers().indexOf(activeP);
        if (index == model.getPlayers().size() - 1) {
            return model.getPlayers().get(0);
        } else {
            return model.getPlayers().get(index + 1);
        }
    }

}
