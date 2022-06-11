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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
    ACTION,
    TOKEN,
    DONE,
    DRAW, GAMEOVER
}

/**
 * This Class is the Controller of the MVC pattern when the game is played on the GUI.
 * It is responsible for the communication between the Model and the View.
 */
public class GuiController {


    private State state;
    private Card chosenCard;
    private Figure chosenFigure;
    private boolean tokenFound;
    @FXML private Label toDo;
    @FXML private BorderPane borderPane;
    @FXML private Label playerName;
    private Player activeP;

    private final List<TextField> names = new ArrayList<>();
    private final List<CheckBox> ais = new ArrayList<>();
    private final List<Circle> circles = new ArrayList<>();
    private final List<ImageView> tokens = new ArrayList<>();
    private final List<Label> directionDiscardingPiles = new ArrayList<>();
    private final List<StackPane> discardPiles = new ArrayList<>();
    private final List<StackPane> handCards = new ArrayList<>();

    private final String RED = "#d34a41";
    private final String GREEN = "#6a9a3d";
    private final String BLUE = "#424ebc";
    private final String PURPLE = "#a45ab9";
    private final String ORANGE = "#f2af4b";

    private final Image ImgEmpty = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/empty.png")));
    private final Image ImgGoblin = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/goblin.png")));
    private final Image ImgMirror = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/mirror.png")));
    private final Image ImgSkull = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/skull.png")));
    private final Image ImgSpiral = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/spiral.png")));
    private final Image ImgStone = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/stone.png")));
    private final Image ImgWeb = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/web.png")));

    Game model;
    View view;
    char[] symbols = {'\u2660', '\u2663', '\u2665', '\u2666'};

    public List<Node> getAllChildren(Parent parent) {
        ArrayList<Node> children = new ArrayList<>();
        for (Node node : parent.getChildrenUnmodifiable()) {
            children.add(node);
            if (node instanceof Parent)
                children.addAll(getAllChildren((Parent) node));
        }
        return children;
    }

    public void classifyChildren(Parent parent) {
        List<Node> nodes = getAllChildren(parent);
        for (Node node : nodes) {
            if (node.getId() != null) {
                if (node.getId().startsWith("circle")) {
                    circles.add((Circle) node);
                } else if (node.getId().startsWith("token")) {
                    tokens.add((ImageView) node);
                } else if (node.getId().startsWith("name")) {
                    names.add((TextField) node);
                } else if (node.getId().startsWith("ai")) {
                    ais.add((CheckBox) node);
                } else if (node.getId().startsWith("directionDiscardingPile")) {
                    directionDiscardingPiles.add((Label) node);
                } else if (node.getId().startsWith("discardingPile")) {
                    discardPiles.add((StackPane) node);
                } else if (node.getId().startsWith("handCard")) {
                    handCards.add((StackPane) node);
                }
            }
        }
    }

    @FXML
    public void startGame(ActionEvent event) throws Exception {
        classifyChildren(borderPane);
        int aiAmount = 0;
        ArrayList<String> playerNames = new ArrayList<>();
        // al least one name has to be entered and one name or ai
        if (names.get(0).getText().isEmpty() || (names.get(1).getText().isEmpty() && !ais.get(0).isSelected())) {
            return;
        } else {
            playerNames.add(names.get(0).getText());
        }

        for (int i = 0; i < 3; i++) {
            if (ais.get(i).isSelected()) {
                aiAmount++;
            } else if (!names.get(i + 1).getText().isEmpty()) {
                playerNames.add(names.get(i + 1).getText());
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
        Parent newRoot = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(newRoot);
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
        stage.centerOnScreen();
        classifyChildren(newRoot);

        state = State.PREPARATION;
        activeP = getNextPlayer();
        takeTurn();
    }

    @FXML
    private void takeTurn() throws Exception {
        switch (state) {
            case PREPARATION:
                playerName.setText(activeP.getName());
                toDo.setText("it's your turn");
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
                    activeP.getPlayedCards(chosenCard.getColor()).add(chosenCard);
                    activeP.getHand().remove(chosenCard);
                } catch (Exception e) {
                    toDo.setText(e.getMessage());
                }
                break;
        }
        updateCards();
    }

    /**
     * updates the hand cards, discarding and direction discarding piles
     * hand cards white when players change
     */
    private void updateCards() {
        classifyChildren(borderPane);
        if (!state.equals(State.PREPARATION)) {
            //get sorted hand cards
            List<Card> sortedHand = CardFunction.sortHand(activeP.getHand());
            for (int i = 0; i < sortedHand.size(); i++) {
                Rectangle card = (Rectangle)handCards.get(i).getChildren().get(0);
                Label cardNumber = (Label)handCards.get(i).getChildren().get(1);
                cardNumber.setText(Integer.toString(sortedHand.get(i).getNumber()));
                card.setFill(changeColor(sortedHand.get(i).getColor()));
            }
            //TODO: all the other cards
        } else {
            for (int i = 0; i < activeP.getHand().size(); i++) {
                Rectangle card = (Rectangle)handCards.get(i).getChildren().get(0);
                Label cardNumber = (Label)handCards.get(i).getChildren().get(1);
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
        for(StackPane pilePane : discardPiles){
            DiscardPile pile = getDiscardPileFromID(pilePane.getId());
            Label lbl = (Label) pilePane.getChildren().get(1);
            if (pile.isEmpty()){
                lbl.setText("");
            } else {
                lbl.setText(Integer.toString(pile.getTop().getNumber()));
            }
        }
        // update tokens
        updateTokens();
    }

    /**
     * updates the tokens on the field
     */
    private void updateTokens() {
        int currentToken = 0;
        while (currentToken <= 39) {
            for (int i = 1; i <= 35; i++) {
                if (Game.FIELDS[i].getToken() == null) {
                    tokens.get(currentToken).setImage(ImgEmpty);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Mirror) {
                    tokens.get(currentToken).setImage(ImgMirror);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Goblin) {
                    tokens.get(currentToken).setImage(ImgGoblin);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Skullpoint) {
                    tokens.get(currentToken).setImage(ImgSkull);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Spiral) {
                    tokens.get(currentToken).setImage(ImgSpiral);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Spiderweb) {
                    tokens.get(currentToken).setImage(ImgWeb);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof WishingStone) {
                    tokens.get(currentToken).setImage(ImgStone);
                    currentToken++;
                }
                if (Game.FIELDS[i] instanceof LargeField && ((LargeField) Game.FIELDS[i]).getTokenTwo() != null) {
                    tokens.get(currentToken).setImage(ImgStone);
                    currentToken++;
                } else if (Game.FIELDS[i] instanceof LargeField) {
                    tokens.get(currentToken).setImage(ImgEmpty);
                    currentToken++;
                }
            }
        }
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

        for (Label label : directionDiscardingPiles) {
            if (label.getId().split("DirectionDiscardPile")[0].toCharArray()[0] == pile.getColor()) {
                label.setText(direction);
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
        for (Label label : directionDiscardingPiles) {
            if (label.getId().split("DirectionDiscardPile", "DirectionDiscardPile".length() + 1)[0].equals("C") &&
                    label.getId().split("DirectionDiscardPile")[0].toCharArray()[0] == pile.getColor()) {
                String number = "" + pile.getTop().getNumber();
                label.setText(number);
            }
        }
    }

    @FXML
    private void onClick(MouseEvent e) throws Exception {
        Node item = (Node) e.getSource();
        System.out.println(state + item.getId());
        if (state  == State.PREPARATION){ //click anything to start thr turn
            state = State.CHOOSEHANDCARD;
        } else if (state.equals(State.CHOOSEHANDCARD) && item.getId().startsWith("handCard")) {
            chosenCard = activeP.getHand().get(getIndex(item.getId(), "handCard"));
            state = State.TRASHORPLAY;
        } else if (state == State.TRASHORPLAY) {
            if (item.getId().startsWith("discardingPile")) {
                System.out.println("trash");
                state = State.TRASH;
            } else if (item.getId().startsWith("circle")) {
                try{
                    chosenFigure = activeP.getFigureOnField(getIndex(item.getId(), "circle"));
                } catch (Exception exception){

                }
                state = State.PLAY;
            }
        }else if (state == State.TRASH && item.getId().startsWith("cardHand")) {
            chosenCard = activeP.getHand().get(getIndex(item.getId(), "handCard"));
            state = State.TOKEN; //what is this state for? -Katinka
        } else if (state == State.TOKEN && item.getId().equals("choice1")) {
            if (tokenFound) {
                state = State.ACTION;
            } else {
                state = State.DONE;
            }
        } else if (state == State.TOKEN) {
            if (item.getId().equals("choice1")) {
                state = State.DONE;
            } else if (item.getId().equals("choice2")) {
                state = State.TOKEN;
            }
        } else if (state == State.DONE) { //click anything to end turn
            state = State.PREPARATION;
            activeP = getNextPlayer();
        } else if (state == State.DRAW) {
            if (item.getId().startsWith("discardingPile")) {
                activeP.drawFromDiscardingPile(getDiscardPileFromID(item.getId())); //draw one card from chosen pile
            } else if (item.getId().equals("drawingPile")) {
                activeP.drawFromDrawingPile();
            }
            state = State.DONE;
        }
        takeTurn();
    }

    /**
     * gets the matching discard pile to color in id
     * @param id the piles id with the color as the last character
     * @return the discard pile from the model
     */
    private DiscardPile getDiscardPileFromID(String id) {
        char[] idArray =  id.toCharArray();
        return model.getDiscardPile(idArray[idArray.length-1]);
    }

    @FXML
    private int getIndex(String id, String elementName) {
        return Integer.parseInt(id.split(elementName)[0]);
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
