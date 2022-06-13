package dhl.controller;

import dhl.controller.player_logic.AI;
import dhl.controller.player_logic.Human;
import dhl.model.*;
import dhl.model.tokens.*;
import dhl.view.View;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
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
    DRAW,
    GAMEOVER
}

/**
 * This Class is the Controller of the MVC pattern when the game is played on the GUI.
 * It is responsible for the communication between the Model and the View.
 */
public class GuiController {

    private State state;
    private Card chosenCard;
    private Figure chosenFigure;
    char[] symbols = {'☠', '♛', '⚔', '❤'};
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

    public List<Node> classifyChildren(Parent parent, String searchId) {
        List<Node> nodes = new ArrayList<>();
        for (Node node : getAllChildren(parent)) {
            if (node.getId() != null && node.getId().startsWith(searchId)) {
                nodes.add(node);
            }
        }
        return nodes;
    }


    @FXML
    public void startGame(ActionEvent event) throws Exception {
        List<Node> names = classifyChildren(root, "name");
        List<Node> ais = classifyChildren(root, "ai");
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

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui.fxml"));
        fxmlLoader.setController(this);
        root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
        stage.centerOnScreen();

        state = State.PREPARATION;
        activeP = getNextPlayer();
        toDo.setText("Click any item to start your turn.");
        takeTurn();
    }
    @FXML
    private void onClick(MouseEvent e) {
        // chosenFigure = null; TODO: this at a different time
        Node item = (Node) e.getSource();
        System.out.println(state + item.getId());

        if (state == State.PREPARATION) { //click anything to start the turn
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
                } catch (Exception exception) {
                    toDo.setText(exception.getMessage());
                }
                state = State.PLAY;
            }
        } else if (state == State.SPIRAL && item.getId().startsWith("circle")) {
            Token token = Game.FIELDS[activeP.getLastMovedFigure().getPos()].getToken();
            int chosenPos = getIndex(item.getId(), "circle");
            if (chosenPos == chosenFigure.getPos()) { //click the current field to not use the spiral
                state = State.DRAW;
                toDo.setText("From which pile do you want to draw?");
            } else if (chosenPos != chosenFigure.getLatestPos() && chosenPos < chosenFigure.getPos()) { //correct field chosen
                ((Spiral) token).setChosenPos(chosenPos);
                token.action(activeP);
                updateCards();
                useToken(); //sets the new state
            } else {
                toDo.setText("You can not go to the field you came from!");
            }
        } else if (state == State.SPIDERWEB) {
            if (item.getId().startsWith("circle")) {
                Game.FIELDS[chosenFigure.getPos()].getToken().action(activeP);
                updateCards();
                useToken(); // sets the new state
            } else {
                state = State.DRAW;
                toDo.setText("From which pile do you want to draw?");
            }
        } else if (state == State.GOBLIN) {
            state = State.DRAW;
            toDo.setText("From which pile do you want to draw?");
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
        takeTurn();
    }
    @FXML
    private void takeTurn() {
        if (model.gameOver()) {
            state = State.GAMEOVER;
            return;
        }
        switch (state) {
            case PREPARATION:
                activeP = getNextPlayer();
                playerName.setText(activeP.getName() + ": " + activeP.getSymbol());
                toDo.setText("it's your turn. Click any card to start.");
                break;
            case TRASH:
                activeP.getHand().remove(chosenCard);
                activeP.putCardOnDiscardingPile(chosenCard);
                state = State.DRAW;
                toDo.setText("From which pile do you want to draw?");
                updateCards();
                break;
            case PLAY:
                try {
                    activeP.placeFigure(chosenCard.getColor(), chosenFigure);
                    activeP.getPlayedCards(chosenCard.getColor()).add(chosenCard);
                    activeP.getHand().remove(chosenCard);
                    state = State.DRAW;
                    useToken();
                } catch (IndexOutOfBoundsException indexE) {
                    toDo.setText("This figure can't move this far! Choose a different one or trash.");
                    state = State.TRASHORPLAY; //choose again what to do with the chosen card
                } catch (Exception e) {
                    state = State.TRASHORPLAY;
                    toDo.setText(e.getMessage());
                }
                break;
        }
        updateCards();
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
                        "or the one you are on to not use the action.");
                break;

            case "Goblin":
                state = State.GOBLIN; //TODO: this and not forget special action
                toDo.setText("OH OH not implemented yet. Click any card to continue.");
                break;

            case "Spiderweb":
                if (FigureFunction.spiderwebIsPossible(activeP.getLastMovedFigure())) {
                    state = State.SPIDERWEB;
                    toDo.setText("Click any field to go to the next same colored field. To say no click one discarding pile.");
                } else {
                    state = State.DRAW;
                    toDo.setText("From which pile do you want to draw?");
                }
                break;
            default:
                token.action(activeP);
                state = State.DRAW;
                toDo.setText("From which pile do you want to draw?");
                break;
        }
    }

    /**
     * updates the hand cards, discarding and direction discarding piles
     * hand cards white when players change
     */
    private void updateCards() {
        List<Node> handCards = classifyChildren(root, "handCard");
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
            //TODO: all the other cards
        } else {
            for (int i = 0; i < 8; i++) {
                Rectangle card = (Rectangle) ((StackPane)handCards.get(i)).getChildren().get(0);
                Label cardNumber = (Label) ((StackPane)handCards.get(i)).getChildren().get(1);
                cardNumber.setText("");
                card.setFill(Color.web("#c8d1d9"));
            }
        }
        // update played cards - direction discard piles
        for (DirectionDiscardPile pile : activeP.getPlayedCards()) {
            setDirection(pile);
            setDirectionDiscardPileNumber(pile);
        }
        // update discard piles
        for (Node pane : classifyChildren(root, "discardingPile")) {
            DiscardPile pile = getDiscardPileFromID(pane.getId());
            Label lbl = (Label) ((StackPane)pane).getChildren().get(1);
            if (pile.isEmpty()) {
                lbl.setText("");
            } else {
                lbl.setText(Integer.toString(pile.getTop().getNumber()));
            }
        }
        // update tokens
        updateTokens();
        // update figures
        updateFigures();
        // update scores
        updateScores();
    }

    /**
     * updates the tokens on the field
     */
    private void updateTokens() {
        int currentToken = 0;
        List<Node> tokens = classifyChildren(root, "token");
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

    /**
     * updates figures on field by adding player's symbol (as a label) to corresponding tilepane
     */
    private void updateFigures() {
        List<Node> circles = classifyChildren(root, "circle");
        for (Node circle : circles) {
            ((TilePane)circle).getChildren().clear();
        }
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
        List<Node> scores = classifyChildren(root, "scoresPlayer");
        List<Node> scoreNames = classifyChildren(root, "scoreName");
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
     * changes a color from char to color
     *
     * @param color as a char
     * @return color according to string constant
     */
    private Color changeColor(char color) {
        switch (color) {
            case 'r':
                return Color.web(RED);
            case 'g':
                return Color.web(GREEN);
            case 'b':
                return Color.web(BLUE);
            case 'p':
                return Color.web(PURPLE);
            case 'o':
                return Color.web(ORANGE);
            default:
                return Color.web("BLACK");
        }
    }

    /**
     * this method changes the direction label depending on the direction of the used discard pile
     *
     * @param pile the current direction discard pile
     */
    @FXML
    private void setDirection(DirectionDiscardPile pile) {

        String direction;

        if (pile.getDirection() == 1) {
            direction = "+";
        } else if (pile.getDirection() == -1) {
            direction = "-";
        } else {
            direction = " ";
        }

        for (Node label : classifyChildren(root, "discardingPile")) {
            if (label.getId().split("DirectionDiscardingPile")[0].toCharArray()[0] == pile.getColor()) {
                ((Label)label).setText(direction);
            }
        }
    }

    /**
     * this method shows the number from the top card on the used direction discard pile
     *
     * @param pile the current direction discard pile
     */
    @FXML
    private void setDirectionDiscardPileNumber(DirectionDiscardPile pile) {
        for (Node label : classifyChildren(root, "discardingPile")) {
            if (label.getId().split("DirectionDiscardingPile")[0].equals("C") &&
                    label.getId().split("DirectionDiscardingPile")[0].toCharArray()[0] == pile.getColor()) {
                String number = "" + pile.getTop().getNumber();
                ((Label)label).setText(number);
            }
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

    @FXML
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
