package dhl.controller;

import dhl.controller.player_logic.AI;
import dhl.controller.player_logic.Human;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiController {

    @FXML
    private Label redLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;

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
                    ais.add( (CheckBox) node);
                }
            }
        }
    }

    public void startGame(ActionEvent event) throws IOException {
        classifyChildren(borderPane);
        int aiAmount = 0;
        ArrayList<String> playerNames = new ArrayList<>();
        if (names.get(0).getText().isEmpty()) {
            return;
        }
        if (!names.get(0).getText().isEmpty()) {
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

        Parent root = FXMLLoader.load(getClass().getResource("/gui.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void startGamew() {

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
