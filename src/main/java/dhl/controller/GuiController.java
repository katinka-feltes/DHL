package dhl.controller;

import dhl.Constants;
import dhl.Save;
import dhl.controller.player_logic.AI;
import dhl.controller.player_logic.Human;
import dhl.controller.player_logic.PlayerLogic;
import dhl.model.*;
import dhl.model.tokens.Goblin;
import dhl.model.tokens.Spiral;
import dhl.model.tokens.Token;
import dhl.view.GuiUpdate;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static dhl.controller.State.*;
import static dhl.view.GuiUpdate.updateCards;

/**
 * This Class is the Controller of the MVC pattern when the game is played on the GUI.
 * It is responsible for the communication between the Model and the View.
 */
public class GuiController {
    private State state;
    private GuiAction guiAction;
    private Card chosenCard;
    private Figure chosenFigure;
    private Player activeP;
    private List<Player> staticPlayers = new ArrayList<>();

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
    private Game model;
    /**
     * contains the name of the current player
     */
    @FXML
    private Label playerName;


    /**
     * This method is called when the user clicks on the "Start Game" button.
     * @param event the click on the Button
     * @throws IOException if the FXML file is not found
     */
    @FXML
    public void startGame(ActionEvent event) throws IOException {

        List<Node> choiceBoxes = GuiUpdate.classifyChildren("choiceBox", root);
        List<Node> names = GuiUpdate.classifyChildren("name", root);
        List<Node> ais = GuiUpdate.classifyChildren("ai", root);

        List<Player> players = new ArrayList<>();

        // at least one name has to be entered and one name or ai
        if (((TextField)names.get(0)).getText().isEmpty() || ((TextField)names.get(1)).getText().isEmpty()
                && !((CheckBox)ais.get(0)).isSelected()) {
            return;
        } else { //if all criteria are passed, the first player who must be human is added
            String enteredName = ((TextField)names.get(0)).getText();
            char symbol = ((String)((ChoiceBox)choiceBoxes.get(0)).getSelectionModel().getSelectedItem()).charAt(0);
            players.add(new Player(enteredName, symbol, new Human()));
        }

        // add the other 3 players if they are entered
        for (int i = 1; i < 4; i++) {
            char symbol = ((String)((ChoiceBox)choiceBoxes.get(i)).getSelectionModel().getSelectedItem()).charAt(0);
            if (((CheckBox)ais.get(i-1)).isSelected()) { //if the player should be an ai
                players.add(new Player("COM" + (i), symbol, new AI()));
            } else if (!((TextField)names.get(i)).getText().isEmpty()) {
                String enteredName = ((TextField)names.get(i)).getText();
                players.add(new Player(enteredName, symbol, new Human()));
            }
        }

        model = new Game(players);
        guiAction = new GuiAction();

        loadNewScene(event, "/gui.fxml", true, true);
        staticPlayers.addAll(players);
        state = PREPARATION;
        activeP = model.getPlayers().get(0);
        toDo.setText("Click 'NEXT PLAYER' to start your turn.");
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
    public void onClick(MouseEvent e) throws Exception {
        Node item = (Node) e.getSource();
        if(model.gameOver()) { //game over screen
            for (Player p : model.getPlayers()) {
                p.calcTokenPoints();
                model.updateHighscore(p.getVictoryPoints(), p.getName(), p.getPlayerLogic());
            }
            loadNewScene(e, "/end.fxml", false, true);
            GuiUpdate.createEndScores(model.getSortedPlayers(), root);
            return;
        } else {
            guiAction.action(item);
        }
        updateAll();
    }

    /**
     * this method highlights the items that are clickable in each state
     * @param e the hovering cursor
     */
    @FXML
    private void highlightWhileHover(MouseEvent e){
        Node item = (Node) e.getSource();
        if(item == null){
            return;
        }
        if (state == PREPARATION && item.getId().startsWith("nextButton") ||(state == CHOOSEHANDCARD && item.getId().startsWith("handCard")) ||
                state == TRASHORPLAY && (item.getId().startsWith("discardingPile") || item.getId().startsWith("circle"))
                || (state == SPIDERWEB || state == SPIRAL) && item.getId().startsWith("circle")
                || state == GOBLIN && (item.getId().startsWith("handCard") || item.getId().startsWith("discardingPile"))
                || state == DRAW && item.getId().startsWith("drawingPile")) {
            Glow glow = new Glow();
            glow.setLevel(0.6);
            if (item.getEffect() == null){
                item.setEffect(glow);
            }else {
                item.setEffect(null);
            }
        }
    }

    private void updateAll() {
        //update cards
        updateCards(root, state, activeP);
        //update discarding piles
        updatePlayedCardsAndNames();
        // update tokens
        GuiUpdate.updateTokens(root, model.getFields());
        // update figures
        GuiUpdate.updateFigures(root, model.getPlayers(), model.getOracle());
        // update scores
        GuiUpdate.updateScores(root, model.getPlayers());
        //update discard piles
        GuiUpdate.updateDiscardPiles(root, model);
        //update collected tokens
        GuiUpdate.updateCollectedTokens(root, activeP);
        //write name and symbol of active player
        playerName.setText(activeP.getName() + ": " + activeP.getSymbol());
        //reset all effects
        GuiUpdate.resetEffects(root);
    }

    /**
     * a method for the AI to take a turn. Only call this if the current player is an AI
     */
    private void takeTurnAI(AI ai) {
        try {
            if(ai.choose("Do you want to play a card?")) {
                chosenCard = ai.chooseCard("What card do you want to play?", null);
                chosenFigure = ai.chooseFigure("", null);
                guiAction.play();

                 //ai always wants to use token
                while (state != DRAW) {
                    switch (state) {
                        case SPIDERWEB:
                            guiAction.useSpiderweb();
                            break;
                        case GOBLIN:
                            goblinAi(ai);
                            break;
                        case SPIRAL:
                            guiAction.useSpiral(ai.chooseSpiralPosition("", chosenFigure.getPos()));
                            break;
                        default:
                            //do nothing
                    }
                }
            } else {
                guiAction.trash(ai.chooseCard("What card do you want to trash?", null));
            }
            // draw to end turn
            if(activeP.getPlayerLogic().choose("Do you want to draw your card from one of the discarding piles?")) {
                activeP.drawFromDiscardingPile(model.getDiscardPile(
                        activeP.getPlayerLogic().choosePileColor("From what colored pile do you want to draw?")));
            } else {
                activeP.drawFromDrawingPile();
            }
            activeP = model.nextPlayer();
            state = PREPARATION;
            toDo.setText("The AI is done with its turn. Press 'NEXT PLAYER' to continue.");
            //startTurn(null);

            updateAll();
        } catch (Exception e) {
            System.err.println("The ai made a incorrect choice that should not occur.");
        }
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
            guiAction.useGoblinHand(ai.bestHandCardToTrash());
        } else {
            char pilecolor = ai.choosePileColor("From which pile do you want to trash the top card?");
            guiAction.useGoblinPile(activeP.getPlayedCards(pilecolor));

            int currentPlayerIndex = model.getPlayers().indexOf(activeP) + 1;
            List<Node> currentDiscardPile = GuiUpdate.classifyChildren("playedCardsNumber" +currentPlayerIndex + pilecolor, root);
            ((Label)currentDiscardPile.get(0)).setText("");
        }
    }



    private void updatePlayedCardsAndNames() {
        int currentPlayerIndex = 0;
        for (int i = 0; i < staticPlayers.size(); i++) {
            if (staticPlayers.get(i).getName().equals(activeP.getName())){
                currentPlayerIndex = i + 1;
            }
        }

        if (chosenCard != null && activeP.getPlayedCards(chosenCard.getColor()).getTop() != null &&
                (state == DRAW || state == SPIRAL || state == SPIDERWEB || state == GOBLIN)) {
            char currentCardColor = activeP.getPlayedCards(chosenCard.getColor()).getTop().getColor();
            List<Node> currentDiscardPileNumber = GuiUpdate.classifyChildren("playedCardsNumber" + currentPlayerIndex + currentCardColor, root);
            List<Node> currentDiscardPileDir = GuiUpdate.classifyChildren("playedCardsDir" + currentPlayerIndex + currentCardColor, root);

            ((Label) currentDiscardPileNumber.get(0)).setText("" + chosenCard.getNumber());
            ((Label) currentDiscardPileDir.get(0)).setText("" + activeP.getPlayedCards(currentCardColor).getDirectionString());
        }
        List<Node> namesPlayedCards = GuiUpdate.classifyChildren("namePlayedCards", root);
        for (int i = 0; i < 4; i++) {
            if (i >= staticPlayers.size()) {
                ((Label)namesPlayedCards.get(i)).setText("");
            } else {
                ((Label)namesPlayedCards.get(i)).setText(staticPlayers.get(i).getName());
            }
        }
    }

    private int getIndex(String id, String elementName) {
        String[] x = id.split(elementName);
        return Integer.parseInt(x[1]);
    }

    /**
     * This method is called when "next player" button is clicked
     * @param event the click on the Button
     */
    @FXML
    public void startTurn(MouseEvent event)  {
        if (state == PREPARATION) {
            chosenFigure = null;
            chosenCard = null;
            activeP.setLastTrashed(null);
            state = CHOOSEHANDCARD;
            toDo.setText("Which card to you want to play or trash?");

            PlayerLogic pl = activeP.getPlayerLogic();
            if (pl instanceof AI) {
                takeTurnAI((AI) pl);
            }
        }
        updateAll();
    }

    /**
     * This method is called when the user clicks on the "Save" button.
     * @param event the click on the Button
     */
    @FXML
    public void save(ActionEvent event)  {
        if(state == PREPARATION || state == CHOOSEHANDCARD || state == TRASHORPLAY) {
            try {
                Save.serializeDataOut(model);
                toDo.setText("Save successful.\n" + toDo.getText());
            } catch (Exception e) {
                System.out.println("error with save");
            }
        } else {
            toDo.setText("You can only save before the turn was started.\n" + toDo.getText());
        }
    }

    /**
     * This method is called when the user clicks on the "Load last saved Game" button.
     * @param event the click on the Button
     * @throws IOException if the FXML file is not found
     */
    @FXML
    public void loadFromSaved(ActionEvent event) throws IOException {
        model = Save.serializeDataIn();

        if(model == null){
            System.out.println("Error loading the game.");
            return;
        }

        loadNewScene(event, "/gui.fxml", true, true);

        state = PREPARATION;
        activeP = model.getPlayers().get(0);
        toDo.setText("Click any item to start your turn.");
        updateAll();
    }

    /**
     * This method is called when the user clicks on the "Load Highscores" button.
     * @param event the click on the Button
     * @throws IOException if the FXML file is not found
     */
    @FXML
    public void loadHighscores(ActionEvent event) throws IOException {
        loadNewScene(event, "/highscores.fxml", false, true);
        List<Node> scoresInList = GuiUpdate.classifyChildren("scoresField", root);
        VBox scores = (VBox)scoresInList.get(0);
        scores.getChildren().clear();
        List<String> highscores = Game.getHighscores();
        for(String score : highscores) {
            scores.getChildren().add(new Label(score));
        }
    }
    @FXML
    public void loadGamemodes(ActionEvent event) throws IOException {
        loadNewScene(event, "/settings.fxml", false, true);
    }

    /**
     * This method is called when the user clicks on the "Menu" or "Start new Game" button.
     * @param e the click on the Button
     * @throws IOException if the FXML file is not found
     */
    @FXML
    public void loadMenu(Event e) throws IOException {
        loadNewScene(e, "/start.fxml", false, false);
    }

    /**
     * This method is called when the user clicks on the "Load Highscores" button.
     * @param event the click on the save
     * @throws IOException if the FXML file is not found
    */
     @FXML
     public void saveGamemodes(ActionEvent event) throws IOException {

         List<Node> choiceBoxes = GuiUpdate.classifyChildren("choiceBox", root);
         //list with first choice box for amount of players, one for total figures in finish
         // and last one for one player's figures in finish area

         int fAmount = Integer.parseInt((String)((ChoiceBox)choiceBoxes.get(0)).getSelectionModel().getSelectedItem());
         int totalFig = Integer.parseInt((String)((ChoiceBox)choiceBoxes.get(1)).getSelectionModel().getSelectedItem());
         int singleFigAmount = Integer.parseInt((String)((ChoiceBox)choiceBoxes.get(2)).getSelectionModel().getSelectedItem());

         //TODO: how to know if total fig is accepted?
         if(totalFig <= fAmount*4 && singleFigAmount <= fAmount) {
             Constants.figureAmount = fAmount;
             Constants.totalFiguresInFinish = totalFig;
             Constants.figuresInFinishOfOnePlayer = singleFigAmount;
             loadMenu(event);
         } else {
            System.out.println("invalid values"); //TODO;
         }
     }




     private class GuiAction {
        public void action(Node item) throws Exception {
            if ((state == CHOOSEHANDCARD || state == TRASHORPLAY) && item.getId().startsWith("handCard")) {
                chosenCard = activeP.getHand().get(getIndex(item.getId(), "handCard"));
                toDo.setText("Click a figure to move, the zombie at the bottom to move the zombie figure or trash it.");
                state = TRASHORPLAY;
            } else if (state == TRASHORPLAY) {
                trashOrPlay(item);
            } else if (state == ORACLE) {
                oracle(item);
            } else if (state == SPIRAL) {
                spiral(item);
            } else if (state == SPIDERWEB) {
                spiderweb(item);
            } else if (state == GOBLIN) {
                goblin(item);
            } else if (state == DRAW) {
                draw(item);
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
                state = TRASHORPLAY; //choose again what to do with the chosen card
            } catch (Exception e) {
                state = TRASHORPLAY;
                toDo.setText(e.getMessage());
            }
        }

        private void useSpiderweb() {
            model.getFields()[chosenFigure.getPos()].getToken().action(activeP);
            updateCards(root, state, activeP);
            useToken(); // sets the new state
        }

        /**
         * executes goblin action if player chose a hand card to trash
         * @param card the player picked to trash
         */
        private void useGoblinHand(Card card) {
            ((Goblin) model.getFields()[chosenFigure.getPos()].getToken()).setPileChoice('h');
            ((Goblin) model.getFields()[chosenFigure.getPos()].getToken()).setCardChoice(card);
            model.getFields()[chosenFigure.getPos()].getToken().action(activeP);
            state = DRAW;
            toDo.setText("From which pile do you want to draw?");
        }

        /**
         * executes goblin action if player chose a played cards top card to trash
         * @param pile the player wants to trash the top card from
         */
        private void useGoblinPile(DirectionDiscardPile pile) {
            ((Goblin) model.getFields()[chosenFigure.getPos()].getToken()).setPileChoice(pile.getColor());
            model.getFields()[chosenFigure.getPos()].getToken().action(activeP);
            state = DRAW;
            toDo.setText("From which pile do you want to draw?");

            int currentPlayerIndex = model.getPlayers().indexOf(activeP) + 1;
            List<Node> currentDiscardPileName = GuiUpdate.classifyChildren("playedCardsNumber" +currentPlayerIndex + pile.getColor(), root);
            List<Node> currentDiscardPileDir = GuiUpdate.classifyChildren("playedCardsDir" +currentPlayerIndex + pile.getColor(), root);
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
            Token token = model.getFields()[activeP.getLastMovedFigure().getPos()].getToken();

            if (chosenPos != chosenFigure.getLatestPos() && chosenPos < chosenFigure.getPos()) { //correct field chosen
                ((Spiral) token).setChosenPos(chosenPos);
                token.action(activeP);
                updateCards(root, state, activeP);
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
            state = DRAW;
            toDo.setText("From which pile do you want to draw?");
        }

        private void useToken() {
            Token token = model.getFields()[chosenFigure.getPos()].collectToken();

            if (token == null) {
                state = DRAW;
                toDo.setText("From which pile do you want to draw?");
                return;
            }

            switch (token.getName()) {

                case "Spiral":
                    state = SPIRAL;
                    toDo.setText("Click the field you want to go back to (except the one you came from) " +
                            "\nor the one you are on to not use the action.");
                    break;

                case "Goblin":
                    state = GOBLIN;
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
                        state = SPIDERWEB;
                        toDo.setText("Click any field to go to the next same colored field. \nTo say no click one discarding pile.");
                    } else {
                        state = DRAW;
                        toDo.setText("From which pile do you want to draw?");
                    }
                    break;
                default:
                    token.action(activeP);
                    state = DRAW;
                    toDo.setText("You found a " + token.getName()+ ". From which pile do you want to draw?");
                    break;
            }
        }


        private void draw(Node item) {
            if (item.getId().startsWith("discardingPile")) {
                try {
                    //get discard pile from id
                    char[] idArray = item.getId().toCharArray();
                    DiscardPile chosenPile = model.getDiscardPile(idArray[idArray.length - 1]);
                    activeP.drawFromDiscardingPile(chosenPile); //draw one card from chosen pile
                } catch (Exception exc) {
                    toDo.setText(exc.getMessage());
                }
            } else if (item.getId().equals("drawingPile")) {
                activeP.drawFromDrawingPile();
            }
            if(activeP.getHand().size() == 8){
                state = PREPARATION;
                activeP = model.nextPlayer();
                toDo.setText("Click on 'NEXT PLAYER' to start your turn.");
            }
        }

        private void goblin(Node item) {
            if (item.getId().startsWith("circle") && activeP.amountFiguresGoblin() == 3 ) {
                activeP.playGoblinSpecial();
                toDo.setText("Click on a card you want to discard, either from your hand\nor from your played cards." +
                        " To say no click one discarding pile.");
            } else if (item.getId().startsWith("handCard")) {
                useGoblinHand(activeP.getHand().get(getIndex(item.getId(), "handCard")));
            } else if (item.getId().startsWith("playedCardsNumber" + (model.getPlayers().indexOf(activeP)+1))) {
                //get played cards from the id
                char[] idArray = item.getId().toCharArray();
                DirectionDiscardPile playedCards =  activeP.getPlayedCards(idArray[idArray.length - 1]);
                useGoblinPile(playedCards);
            } else if (item.getId().startsWith("playedCardsNumber")) {
                toDo.setText("This is not your played cards pile.");
            } else {
                state = DRAW;
                toDo.setText("From which pile do you want to draw?");
            }
        }

        private void spiderweb(Node item) {
            if (item.getId().startsWith("circle")) {
                useSpiderweb();
            } else {
                state = DRAW;
                toDo.setText("From which pile do you want to draw?");
            }
        }

        private void spiral(Node item) {
            if(item.getId().startsWith("circle")) {
                int chosenPos = getIndex(item.getId(), "circle");
                if (chosenPos == chosenFigure.getPos()) { //click the current field to not use the spiral
                    state = DRAW;
                    toDo.setText("From which pile do you want to draw?");
                } else {
                    useSpiral(chosenPos);
                }
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
            } else if (item.getId().startsWith("zombie")) {
                toDo.setText("Click on the field you want to move the zombie to.\nIt can move up to " +
                        chosenCard.getOracleNumber() + " steps forward");
                state = ORACLE;
            }
        }

        /**
         * what happens in state oracle after item is clicked
         * @param item the clicked item
         * @throws Exception if the figure would leave the field or if card does not fit to pile
         */
        private void oracle(Node item) throws Exception {
            try{
                activeP.getPlayedCards(chosenCard.getColor()).add(chosenCard);
                activeP.getHand().remove(chosenCard);

                toDo.setText("Click on the field you want to move the zombie to.\nIt can move up to " +
                        chosenCard.getOracleNumber() + " steps forward");
                int chosenPosition = getIndex(item.getId(), "circle");
                if(chosenPosition <= model.getOracle()+chosenCard.getOracleNumber() && chosenPosition > model.getOracle()) {
                    activeP.placeOracle(chosenPosition - model.getOracle());
                    state = DRAW;
                    toDo.setText("From which pile do you want to draw?");
                }
            } catch (Exception e) {
                state = TRASHORPLAY;
                toDo.setText(e.getMessage());
            }
        }
    }
}

