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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This Class is the Controller of the MVC pattern when the game is played on the GUI.
 * It is responsible for the communication between the Model and the View.
 */
public class GuiController {

    static final String PREPARATION = "preparation";
    static final String TRASHORPLAY = "trashOrPlay";
    static final String DONE = "done";
    static final String PLAY = "play";
    static final String TRASH = "trash";
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
    private final List<ImageView> tokens = new ArrayList<>();
    private final List<Label> directionDiscardingPiles = new ArrayList<>();
    private final List<Label> discardPiles = new ArrayList<>();
    private final List<Label> handLabels = new ArrayList<>();
    private final List<Rectangle> handCards = new ArrayList<>();

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
        updateTokens();
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
                activeP.drawFromDrawingPile();
                state = DONE;
                toDo.setText("Are you done with your turn?");
                updateCards();
                break;
            case PLAY:
                try {
                    activeP.putCardOnDiscardingPile(chosenCard);
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
        if (!state.equals(PREPARATION)) {
            //get sorted hand cards
            List<Card> sortedHand = CardFunction.sortHand(activeP.getHand());
            for (int i = 0; i < sortedHand.size(); i++) {
                handLabels.get(i).setText(Integer.toString(sortedHand.get(i).getNumber()));
                handCards.get(i).setFill(changeColor(sortedHand.get(i).getColor()));
            }
            //TODO: all the other cards
            updateTokens();
        } else {
            for (int i = 0; i < activeP.getHand().size(); i++) {
                handLabels.get(i).setText("0");
                handCards.get(i).setFill(Color.web("#c8d1d9"));
            }
        }
        //update directiondiscardpiles
        for(DirectionDiscardPile pile: activeP.getPlayedCards()) {
            setDirection(pile);
            setDirectionDiscardPileNumber(pile);
        }
        updateDiscardPiles();
    }

    /**
     * updates the tokens on the field
     */
    private void updateTokens() {
        int currentToken = 0;
        while(currentToken <= 40) {
            for(Field field: Game.FIELDS) {
                if(field.getToken() == null) {
                    tokens.get(currentToken).setImage(ImgEmpty);
                } else if(field instanceof LargeField && ((LargeField) field).getTokenTwo() != null) {
                    tokens.get(currentToken).setImage(ImgStone);
                    currentToken++;
                    tokens.get(currentToken).setImage(ImgStone);
                    currentToken++;
                } else {
                    if(field.getToken() instanceof Mirror) {
                        tokens.get(currentToken).setImage(ImgMirror);
                        currentToken++;
                    } else if(field.getToken() instanceof Goblin) {
                        tokens.get(currentToken).setImage(ImgGoblin);
                        currentToken++;
                    } else if(field.getToken() instanceof Skullpoint) {
                        tokens.get(currentToken).setImage(ImgSkull);
                        currentToken++;
                    } else if(field.getToken() instanceof Spiral) {
                        tokens.get(currentToken).setImage(ImgSpiral);
                        currentToken++;
                    } else if(field.getToken() instanceof Spiderweb) {
                        tokens.get(currentToken).setImage(ImgWeb);
                        currentToken++;
                    }
                }
            }
        }
    }

    /**
     * changes a color from char to color
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

    /**
     * updates the discarding piles
     */
    @FXML
    private void updateDiscardPiles() {
        setDiscardPileNumber(model.getDiscardPile('r'));
        setDiscardPileNumber(model.getDiscardPile('g'));
        setDiscardPileNumber(model.getDiscardPile('b'));
        setDiscardPileNumber(model.getDiscardPile('p'));
        setDiscardPileNumber(model.getDiscardPile('o'));
    }

    @FXML
    private void onClick(MouseEvent e) throws Exception {
        Node item = (Node) e.getSource();
        if (state.equals(PREPARATION) && item.getId().startsWith("choice")){
            state = CHOOSEHANDCARD;
        } else if (state.equals(CHOOSEHANDCARD) && item.getId().startsWith("cardHand")) {
            chosenCard = activeP.getHand().get(Integer.parseInt(item.getId().split("cardHand")[1]));
            state = TRASHORPLAY;
        } else if (state.equals(TRASHORPLAY) && item.getId().startsWith("choice")) {
            if (item.getId().equals("choice1")) {
                state = TRASH;
            } else if (item.getId().equals("choice2")) {
                state = PLAY;
            }
        }else if (state.equals(PLAY) && item.getId().startsWith("cardHand")) {
            chosenCard = activeP.getHand().get(Integer.parseInt(item.getId().split("cardHand")[1]));
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
        } else if (state.equals(DONE) && item.getId().equals("choice1")) {
            state = PREPARATION;
            activeP = getNextPlayer();
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
