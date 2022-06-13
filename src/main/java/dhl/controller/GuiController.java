package dhl.controller;

import dhl.controller.player_logic.AI;
import dhl.controller.player_logic.Human;
import dhl.model.*;
import dhl.model.tokens.*;
import dhl.view.View;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

enum State {
    PREPARATION,
    CHOOSEHANDCARD,
    TRASHORPLAY,
    TRASH,
    PLAY,
    SPIDERWEB,
    SPIRAL,
    GOBLIN,
    DRAW
}

/**
 * This Class is the Controller of the MVC pattern when the game is played on the GUI.
 * It is responsible for the communication between the Model and the View.
 */
public class GuiController {

    private State state;
    private Card chosenCard;
    private Figure chosenFigure;
    private Player activeP;
    @FXML
    private Label toDo;
    @FXML
    private BorderPane root;

    private static final String RED = "#d34a41";
    private static final String GREEN = "#6a9a3d";
    private static final String BLUE = "#424ebc";
    private static final String PURPLE = "#a45ab9";
    private static final String ORANGE = "#f2af4b";

    private final Image imgGoblin = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/goblin.png")));
    private final Image imgMirror = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/mirror.png")));
    private final Image imgSkull = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/skull.png")));
    private final Image imgSpiral = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/spiral.png")));
    private final Image imgStone = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/stone.png")));
    private final Image imgWeb = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/web.png")));

    Game model;
    View view;
    @FXML
    private Label playerName;

    public List<Node> getAllChildren(Parent parent) {
        ArrayList<Node> children = new ArrayList<>();
        for (Node node : parent.getChildrenUnmodifiable()) {
            children.add(node);
            if (node instanceof Parent)
                children.addAll(getAllChildren((Parent) node));
        }
        return children;
    }

    public List<Node> classifyChildren(String searchId) {
        List<Node> nodes = new ArrayList<>();
        for (Node node : getAllChildren(root)) {
            if (node.getId() != null && node.getId().startsWith(searchId)) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    @FXML
    public void startGame(ActionEvent event) throws Exception {

        char[] symbols = {'☠', '♛', '⚔', '❤'};

        List<Node> names = classifyChildren("name");
        List<Node> ais = classifyChildren("ai");
        int aiAmount = 0;
        ArrayList<String> playerNames = new ArrayList<>();
        // al least one name has to be entered and one name or ai
        if (((TextField)names.get(0)).getText().isEmpty() ||
                (((TextField)names.get(0)).getText().isEmpty() &&
                        !((CheckBox)ais.get(0)).isSelected())) {
            return;
        } else {
            playerNames.add(((TextField)names.get(0)).getText());
        }

        for (int i = 0; i < 3; i++) {
            if (((CheckBox)ais.get(i)).isSelected()) {
                aiAmount++;
            } else if (!((TextField)names.get(i + 1)).getText().isEmpty()) {
                playerNames.add(((TextField)names.get(i + 1)).getText());
            }
        }
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerNames.size(); i++) {
            players.add(new Player(playerNames.get(i), symbols[i], new Human(view)));
        }
        for (int i = 0; i < aiAmount; i++) {
            players.add(new Player("AI" + (i + 1), symbols[i + playerNames.size()], new AI()));
        }
        model = new Game(players);

        loadNewScene(event, "/gui.fxml", true, true);

        state = State.PREPARATION;
        activeP = model.getPlayers().get(0);
        toDo.setText("Click any item to start your turn.");
        takeTurn();
    }

    /**
     * loads a new scene
     * @param event event that triggers the new scene
     * @param sceneFile scene that should be loaded
     * @param max if setMaximized should be set or not
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

    @FXML
    private void onClick(MouseEvent e) throws IOException {
        Node item = (Node) e.getSource();
        System.out.println(state + item.getId());

        if (state == State.PREPARATION) { //click anything to start the turn
            chosenFigure = null;
            chosenCard = null;
            state = State.CHOOSEHANDCARD;
            toDo.setText("Which card to you want to play or trash?");
        } else if ((state == State.CHOOSEHANDCARD || state == State.TRASHORPLAY) && item.getId().startsWith("handCard")) {
            chosenCard = activeP.getHand().get(getIndex(item.getId(), "handCard"));
            toDo.setText("Click a figure to move or trash it.");
            state = State.TRASHORPLAY;
        } else if (state == State.TRASHORPLAY) {
            if (item.getId().startsWith("discardingPile")) {
                state = State.TRASH;
            } else if (item.getId().startsWith("circle")) {
                try {
                    chosenFigure = activeP.getFigureOnField(getIndex(item.getId(), "circle"));
                    state = State.PLAY;
                } catch (Exception exception) {
                    toDo.setText(exception.getMessage());
                }
            }
        } else if (state == State.SPIRAL && item.getId().startsWith("circle")) {
            int chosenPos = getIndex(item.getId(), "circle");
            if (chosenPos == chosenFigure.getPos()) { //click the current field to not use the spiral
                state = State.DRAW;
                toDo.setText("From which pile do you want to draw?");
            } else {
                useSpiral(chosenPos);
            }
        } else if (state == State.SPIDERWEB) {
            if (item.getId().startsWith("circle")) {
                useSpiderweb();
            } else {
                state = State.DRAW;
                toDo.setText("From which pile do you want to draw?");
            }
        } else if (state == State.GOBLIN) {
            if (item.getId().startsWith("handCard")) {
                useGoblinHand(activeP.getHand().get(getIndex(item.getId(), "handCard")));
            } else if (item.getId().startsWith("playedCardsNumber" + (model.getPlayers().indexOf(activeP)+1))) {
                useGoblinPile(getPlayedCardsFromID(item.getId()));
            } else if (item.getId().startsWith("playedCardsNumber")) {
                toDo.setText("This is not your played cards pile.");
            } else if (!activeP.isGoblinSpecialPlayed() && activeP.amountFiguresGoblin() == 3) {
                toDo.setText("Do you want to play your goblin special action (and get " + activeP.goblinSpecialPoints()
                        + " points?" + "\nIf yes, click a field. If no, click any discarding pile");
                if(item.getId().startsWith("circle")) {
                    activeP.playGoblinSpecial();
                } else {
                    state = State.DRAW;
                    toDo.setText("From which pile do you want to draw?");
                }
            } else {
                state = State.DRAW;
                toDo.setText("From which pile do you want to draw?");
            }
        } else if (state == State.DRAW) {
            if (item.getId().startsWith("discardingPile")) {
                try {
                    activeP.drawFromDiscardingPile(getDiscardPileFromID(item.getId())); //draw one card from chosen pile
                    state = State.PREPARATION;
                } catch (Exception exc) {
                    toDo.setText(exc.getMessage());
                }
            } else if (item.getId().equals("drawingPile")) {
                activeP.drawFromDrawingPile();
                state = State.PREPARATION;
            }
        }
        if(item.getId().startsWith("menu")) {
            loadNewScene(e, "/start.fxml", false, false);
            return;
        }
        if(!model.gameOver()) {
            takeTurn();
        } else { //game over screen
            for (Player p : model.getPlayers()) {
                p.calcTokenPoints();
                model.updateHighscore(p.getVictoryPoints(), p.getName(), p.getPlayerLogic());
            }
            loadNewScene(e, "/end.fxml", false, true);
            createEndScores();
        }
    }

    @FXML
    private void takeTurn() {
        switch (state) {
            case PREPARATION:
                activeP = getNextPlayer();
                playerName.setText(activeP.getName() + ": " + activeP.getSymbol());
                if(activeP.getPlayerLogic() instanceof AI){
                    takeTurnAI();
                    break;
                }
                toDo.setText("it's your turn. Click any card to start.");
                break;
            case TRASH:
                trash(chosenCard);
                break;
            case PLAY:
                play();
                break;
        }
        //update cards
        updateCards();
        //update discarding piles
        updateDiscardPiles();
        // update tokens
        updateTokens();
        // update figures
        updateFigures();
        // update scores
        updateScores();
        
        updateCollectedTokens();
    }

    private void takeTurnAI() {
        AI ai = (AI)activeP.getPlayerLogic();
        if(ai.choose("Do you want to play a card?")){
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
                            if(ai.choose("Do you want to trash one from your hand?")) {
                                useGoblinHand(ai.bestHandCardToTrash());
                            } else {
                                useGoblinPile(activeP.getPlayedCards(ai.choosePileColor("From which pile do you want to trash the top card?")));
                            }
                            if(!activeP.isGoblinSpecialPlayed() && activeP.amountFiguresGoblin() == 3) {
                                if(ai.choose("Do you want to play your goblin-special?")) {
                                    activeP.playGoblinSpecial();
                                }
                            }
                            break;
                        case SPIRAL:
                            useSpiral(ai.chooseSpiralPosition("", chosenFigure.getPos()));
                    }
                }

            } catch (Exception e){
                System.out.println("The ai made a incorrect choice that should not occur.");
            }
        } else {
            trash(ai.chooseCard("What card do you want to trash?", null));
        }

        // draw to end turn
        activeP.drawFromDrawingPile();
        state = State.PREPARATION;
        takeTurn();
    }

    private void play() {
        try {
            activeP.placeFigure(chosenCard.getColor(), chosenFigure);

            activeP.getPlayedCards(chosenCard.getColor()).add(chosenCard);
            activeP.getHand().remove(chosenCard);

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
        updateCards();
        useToken(); // sets the new state
    }

    /**
     * executes goblin action if player chose a hand card to trash
     * @param card the player picked to trash
     */
    private void useGoblinHand(Card card) {
        ((Goblin)Game.FIELDS[chosenFigure.getPos()].getToken()).setPileChoice('h');
        ((Goblin)Game.FIELDS[chosenFigure.getPos()].getToken()).setCardChoice(card);
        Game.FIELDS[chosenFigure.getPos()].getToken().action(activeP);
        state = State.DRAW;
        toDo.setText("From which pile do you want to draw?");
    }

    /**
     * executes goblin action if player chose a played cards top card to trash
     * @param pile the player wants to trash the top card from
     */
    private void useGoblinPile(DirectionDiscardPile pile) {
        ((Goblin)Game.FIELDS[chosenFigure.getPos()].getToken()).setPileChoice(pile.getColor());
        Game.FIELDS[chosenFigure.getPos()].getToken().action(activeP);
        state = State.DRAW;
        toDo.setText("From which pile do you want to draw?");
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
            updateCards();
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
                toDo.setText("Click on a card you want to discard, either from your hand\nor from your played cards." +
                        " To say no click one discarding pile.");
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

    /**
     * updates the hand cards, discarding and direction discarding piles
     * hand cards white when players change
     */
    private void updateCards() {

        //update hand cards
        List<Node> handCards = classifyChildren("handCard");
        if (!state.equals(State.PREPARATION)) {
            //get sorted hand cards
            List<Card> sortedHand = CardFunction.sortHand(activeP.getHand());
            for (int i = 0; i < handCards.size(); i++) {
                Rectangle card = (Rectangle) ((StackPane)handCards.get(i)).getChildren().get(0);
                Label cardNumber = (Label) ((StackPane)handCards.get(i)).getChildren().get(1);
                if (i < sortedHand.size()) {
                    cardNumber.setText(Integer.toString(sortedHand.get(i).getNumber()));
                    card.setFill(changeColor(sortedHand.get(i).getColor()));
                } else {
                    cardNumber.setText("");
                    card.setFill(Color.web("#c8d1d9"));
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                Rectangle card = (Rectangle) ((StackPane)handCards.get(i)).getChildren().get(0);
                Label cardNumber = (Label) ((StackPane)handCards.get(i)).getChildren().get(1);
                cardNumber.setText("");
                card.setFill(Color.web("#c8d1d9"));
            }
        }

    }

    /**
     * updates the tokens on the field
     */
    private void updateTokens() {
        int currentToken = 0;
        List<Node> tokens = classifyChildren("token");
        while (currentToken <= 39) {
            for (int i = 1; i <= 35; i++) {
                if (Game.FIELDS[i].getToken() == null) {
                    ((ImageView)tokens.get(currentToken)).setImage(null);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Mirror) {
                    ((ImageView)tokens.get(currentToken)).setImage(imgMirror);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Goblin) {
                    ((ImageView)tokens.get(currentToken)).setImage(imgGoblin);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Skullpoint) {
                    ((ImageView)tokens.get(currentToken)).setImage(imgSkull);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Spiral) {
                    ((ImageView)tokens.get(currentToken)).setImage(imgSpiral);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Spiderweb) {
                    ((ImageView)tokens.get(currentToken)).setImage(imgWeb);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof WishingStone) {
                    ((ImageView)tokens.get(currentToken)).setImage(imgStone);
                    currentToken++;
                }
                //if large field, check the second token
                if (Game.FIELDS[i] instanceof LargeField && ((LargeField) Game.FIELDS[i]).getTokenTwo() != null) {
                    ((ImageView)tokens.get(currentToken)).setImage(imgStone);
                    currentToken++;
                } else if (Game.FIELDS[i] instanceof LargeField) {
                    ((ImageView)tokens.get(currentToken)).setImage(null);
                    currentToken++;
                }
            }
        }
    }

    private void updateDiscardPiles() {
        int currentPlayerIndex = model.getPlayers().indexOf(activeP);
        List<Node> discardPiles = classifyChildren("playedCards");
        for (Node pile : discardPiles) {
            DirectionDiscardPile playedCards = getPlayedCardsFromID(pile.getId());
            if (!playedCards.isEmpty()) {
                Card lastPlayedCard = activeP.getPlayedCards(playedCards.getTop().getColor()).getTop();
                if (pile.getId().equals("playedCardsNumber" + (currentPlayerIndex + 1) + lastPlayedCard.getColor())) {
                    ((Label)pile).setText("" + lastPlayedCard.getNumber());
                } else if (pile.getId().equals("playedCardsDir" + (currentPlayerIndex + 1) + lastPlayedCard.getColor())) {
                ((Label) pile).setText("" + playedCards.getDirectionString());
                }
            }
        }
    }

    private void updateCollectedTokens(){
        HBox tokenBox = (HBox)classifyChildren("collectedToken").get(0);
        //clear earlier tokens
        tokenBox.getChildren().clear();
        //print every wishing stone
        for (int i = 0; i < activeP.getTokens()[0]; i++){
            ImageView img = new ImageView(imgStone);
            img.setFitHeight(40);
            img.setFitWidth(40);
            tokenBox.getChildren().add(img);
        }
        // for every mirror
        for (int i = 0; i < activeP.getTokens()[1]; i++){
            ImageView img = new ImageView(imgMirror);
            img.setFitHeight(40);
            img.setFitWidth(40);
            tokenBox.getChildren().add(img);
        }
    }

    /**
     * updates figures on field by adding player's symbol (as a label) to corresponding tilepane
     */
    private void updateFigures() {
        List<Node> circles = classifyChildren("circle");
        //clear all fields
        for (Node circle : circles) {
            ((TilePane)circle).getChildren().clear();
        }
        //print the figure symbols
        for (Player player : model.getPlayers()) {
            for (Figure figure : player.getFigures()) {
                ((TilePane)circles.get(figure.getPos())).getChildren().add(new Label(Character.toString(player.getSymbol())));
            }
        }
    }

    /**
     * updates scores with corresponding player names
     */
    private void updateScores() {
        List<Node> scores = classifyChildren("scorePlayer");
        List<Node> scoreNames = classifyChildren("scoreName");
        for (int i = 0; i < 4; i++) {
            if (i > model.getPlayers().size() - 1) {
                ((Label)scoreNames.get(i)).setText("");
                ((Label)scores.get(i)).setText("");
            } else {
                ((Label)scoreNames.get(i)).setText(model.getPlayers().get(i).getName());
                ((Label)scores.get(i)).setText(Integer.toString(model.getPlayers().get(i).getVictoryPoints()));
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
                List<String> winnerList = calculateWinners();
                ((Label)endScores.get(i)).setText(winnerList.get(i));
            }
        }
    }

    /**
     * creates a list of player names ordered by victory points
     * @return string list with player names (0=winner, end of list=loser)
     */
    private List<String> calculateWinners() {
        List<String> winnerList = new ArrayList<>();
        List<Player> playerList = model.getPlayers();
        playerList.sort(Comparator.comparing(Player::getVictoryPoints));
        for(int i=playerList.size()-1; i>=0; i--) {
            winnerList.add(playerList.get(i).getName());
        }
        return winnerList;
    }

    /**
     * changes a color from char to color
     *
     * @param color as a char
     * @return color according to string constant
     */
    private Color changeColor(char color){
        switch (color){
            case 'r': return Color.web(RED);
            case 'g': return Color.web(GREEN);
            case 'b': return Color.web(BLUE);
            case 'p': return Color.web(PURPLE);
            case 'o': return Color.web(ORANGE);
            default: return Color.web("BLACK");
        }
    }

    /**
     * gets the matching discard pile to color in id
     *
     * @param id the piles' id with the color as the last character
     * @return the discard pile from the model
     */
    private DiscardPile getDiscardPileFromID(String id) {
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
