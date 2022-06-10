package dhl.controller;

import dhl.controller.player_logic.AI;
import dhl.controller.player_logic.Human;
import dhl.model.DirectionDiscardPile;
import dhl.model.DiscardPile;
import dhl.model.Game;
import dhl.model.Player;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class is the Controller of the MVC pattern when the game is played on the GUI.
 * It is responsible for the communication between the Model and the View.
 */
public class GuiController {

    static final String PREPARATION = "preparation";
    static final String TRASHORPLAY = "trashOrPlay";
    static final String DONE = "done";
    static final String PLAY = "play";
    static final String ACTION = "action";
    static final String TOKEN = "token";
    static final String CHOOSEHANDCARD = "chooseHandCard";
    private Card chosenCard;
    private boolean tokenFound;
    @FXML private Label toDo;
    @FXML private BorderPane borderPane;
    @FXML private Label playerName;
    private String state;
    private Player activeP;

    private final List<TextField> names = new ArrayList<>();
    private final List<CheckBox> ais = new ArrayList<>();
    private final List<Circle> circles = new ArrayList<>();
    private final List<Rectangle> tokens = new ArrayList<>();
    private final List<Label> directionDiscardingPiles = new ArrayList<>();
    private final List<Label> discardPiles = new ArrayList<>();
    private final List<Label> handLabels = new ArrayList<>();
    private final List<Rectangle> handCards = new ArrayList<>();

    private final String RED = "#d34a41";
    private final String GREEN = "#6a9a3d";
    private final String BLUE = "#424ebc";
    private final String PURPLE = "#a45ab9";
    private final String ORANGE = "#D05E00";

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
                    tokens.add((Rectangle) node);
                } else if (node.getId().startsWith("name")) {
                    names.add((TextField) node);
                } else if (node.getId().startsWith("ai")) {
                    ais.add((CheckBox) node);
                } else if (node.getId().startsWith("directionDiscardingPile")) {
                    directionDiscardingPiles.add((Label) node);
                } else if (node.getId().startsWith("DiscardingPile")) {
                    discardPiles.add((Label) node);
                } else if (node.getId().startsWith("LabelHand")) {
                    handLabels.add((Label) node);
                } else if (node.getId().startsWith("cardHand")) {
                    handCards.add((Rectangle) node);
                }
            }
        }
    }
    @FXML
    public void startGame(ActionEvent event) throws IOException {
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
            if(ais.get(i).isSelected()) {
                aiAmount++;
            } else if(!names.get(i+1).getText().isEmpty()) {
                playerNames.add(names.get(i+1).getText());
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

        state = PREPARATION;
        activeP = getNextPlayer();
        takeTurn();
    }

    @FXML
    private void takeTurn() {
        if (state.equals(PREPARATION)) {
            playerName.setText(activeP.getName());
            toDo.setText("it's your turn");
        } else if (state.equals(TRASHORPLAY)) {
            for (int i = 0; i <= 7; i++) {
                handLabels.get(i).setText(Integer.toString(activeP.getHand().get(i).getNumber()));
                handCards.get(i).setFill(changeColor(activeP.getHand().get(i).getColor()));
            }
        }
    }
    /**
     * changes a color from char to string
     * @param color as a char
     * @return color as HEX in String
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
     * @param pile the current direction discard pile
     */
     @FXML
     private void setDirection(DirectionDiscardPile pile) {

         String direction;

         if (pile.getDirection() == 1){
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

    /**
     * this method shows the number from the top card on the used discard pile
     * @param pile the current discard pile
     */
    @FXML
    private void setDiscardPileNumber(DiscardPile pile) {
        for (Label label : directionDiscardingPiles) {
            if (label.getId().split("DiscardPile", "DiscardPile".length() + 1)[0].equals("C") &&
                    label.getId().split("DiscardPile")[0].toCharArray()[0] == pile.getColor()) {
                String number = "" + pile.getTop().getNumber();
                label.setText(number);
            }
        }
    }


    @FXML
    private void onClick(MouseEvent e) {
        Node item = (Node) e.getSource();
        if (state.equals(PREPARATION) && item.getId().startsWith("choice")){
            state = CHOOSEHANDCARD;
        } else if (state.equals(CHOOSEHANDCARD) && item.getId().startsWith("cardHand")) {
            chosenCard = activeP.getHand().get(Integer.parseInt(item.getId().split("cardHand")[0]));
            state = TRASHORPLAY;
        } else if (state.equals(TRASHORPLAY) && item.getId().startsWith("choice")) {
            if (item.getId().equals("choice1")) {
                state = DONE;
            } else if(item.getId().equals("choice2")) {
                state = PLAY;
            }
        }else if (state.equals(PLAY) && item.getId().startsWith("cardHand")) {
            chosenCard = activeP.getHand().get(Integer.parseInt(item.getId().split("cardHand")[0]));
            state = TOKEN;
        } else if (state.equals(TOKEN) && item.getId().equals("choice1")) {
            if (tokenFound) {
                state = ACTION;
            } else {
                state = DONE;
            }
        } else if (state.equals(ACTION)) {
            if (item.getId().equals("choice1")) {
                state = DONE;
            } else if (item.getId().equals("choice2")) {
                state = TOKEN;
            }
        }
            takeTurn();
    }

    @FXML
    private int getCardIndex(String cardID) {
       return Integer.parseInt(cardID.split("handCard")[0]);
    }

    private Player getNextPlayer(){
        int index = model.getPlayers().indexOf(activeP);
        if (index == model.getPlayers().size() - 1) {
            return model.getPlayers().get(0);
        } else {
            return model.getPlayers().get(index + 1);
        }
    }
}
