package dhl.controller;

import dhl.controller.player_logic.AI;
import dhl.controller.player_logic.Human;
import dhl.model.Game;
import dhl.model.Player;
import dhl.view.View;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GuiController {

    @FXML
    private Label redLabel;
    @FXML
    private Button startButton;
    @FXML
    private Label greenLabel;
    @FXML
    private Label blueLabel;
    @FXML
    private Label purpleLabel;
    @FXML
    private Label orangeLabel;
    @FXML
    private Rectangle redDiscardPile;
    @FXML
    private Rectangle greenDiscardPile;
    @FXML
    private Rectangle blueDiscardPile;
    @FXML
    private Rectangle purpleDiscardPile;
    @FXML
    private Rectangle orangeDiscardPile;

    @FXML
    private void setDirection() {
        System.out.println("Lol");
        redLabel.setText("+");
    }


    @FXML
    private BorderPane borderPane;
    private final List<TextField> names = new ArrayList<>();
    private final List<CheckBox> ais = new ArrayList<>();
    private final List<Circle> circles = new ArrayList<>();
    private final List<Rectangle> tokens = new ArrayList<>();

    Game model;
    View view;
    char[] symbols = {'\u2660', '\u2663', '\u2665', '\u2666'};

    @FXML
    public void initialize() {
        List<Node> nodes = getAllChildren(borderPane);
        for (Node node : nodes) {
            if (node.getId() != null) {
                if (node.getId().startsWith("name")) {
                    names.add((TextField) node);
                }
                if (node.getId().startsWith("ai")) {
                    ais.add((CheckBox) node);
                }
            }
        }
    }
    public List<Node> getAllChildren(Parent parent) {
        ArrayList<Node> children = new ArrayList<>();
        for (Node node : parent.getChildrenUnmodifiable()) {
            children.add(node);
            if (node instanceof Parent)
                children.addAll(getAllChildren((Parent) node));
        }
        return children;
    }

    public void getAllNodesInView() {
        for (Node node : borderPane.getChildren().filtered(node -> node.getId() != null)) {
            if (node.getId().startsWith("circle")) {
                circles.add(Integer.parseInt(node.getId().split("circle")[1]) - 1, (Circle) node);
            } else if (node.getId().startsWith("token")) {
                tokens.add(Integer.parseInt(node.getId().split("token")[1]) - 1, (Rectangle) node);
            }
        }
    }

    @FXML
    public void startGame() {
        int aiAmount = 0;
        String[] playerNames = new String[4];
        if (names.get(0).getText() == null) {
            return;
        }
        if (!names.get(0).getText().isEmpty()) {
            playerNames[0] = names.get(0).getText();
        }

        for (int i = 0; i < 3; i++) {
            if(ais.get(i).isSelected()) {
                aiAmount++;
            } else if(!names.get(i+1).getText().isEmpty()) {
                playerNames[i+1] = names.get(i+1).getText();
            }
        }
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerNames.length; i++) {
            players.add(new Player(playerNames[i], symbols[i], new Human(view)));
        }
        for (int i = 0; i < aiAmount; i++) {
            players.add(new Player("AI" + (i + 1), symbols[i + playerNames.length], new AI()));
        }
        model = new Game(players);

        /** while(!model.gameOver()){
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
        } **/
    }
}
