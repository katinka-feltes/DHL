package dhl.view;

import dhl.controller.GuiController;
import dhl.controller.State;
import dhl.model.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static dhl.Constants.*;
import static dhl.controller.State.PREPARATION;

/**
 * this class contains most of the update functions for the gui.
 */
public class GuiUpdate {

    /**
     * gets all the children of a given Parent
     * @param parent the parent you want to get the children from
     * @return a list of all the children of the parent
     */
    public static List<Node> getAllChildren(Parent parent) {
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
     * @param root the Borderpane to get the nodes from
     * @return all the matching children that start with the given string
     */
    public static List<Node> classifyChildren(String searchId, BorderPane root) {
        List<Node> nodes = new ArrayList<>();
        for (Node node : getAllChildren(root)) {
            if (node.getId() != null && node.getId().startsWith(searchId)) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    /**
     * updates the tokens on the field
     * @param root the BorderPane parent of all nodes
     * @param fields the fields of the game
     */
    public static void updateTokens(BorderPane root, Field[] fields) {
        List<Node> tokens = classifyChildren("token", root);
        int tokenIndex = 0;
        while (tokenIndex <= 39) {
            for (int i = 1; i <= 35; i++) {
                ((ImageView)tokens.get(tokenIndex)).setImage(getTokenImage(fields[i].getToken()));
                tokenIndex++;
                //if large field, check the second token
                if (fields[i] instanceof LargeField && ((LargeField) fields[i]).getTokenTwo() != null) {
                    ((ImageView)tokens.get(tokenIndex)).setImage(IMG_STONE);
                    tokenIndex++;
                } else if (fields[i] instanceof LargeField) {
                    ((ImageView)tokens.get(tokenIndex)).setImage(null);
                    tokenIndex++;
                }
            }
        }
    }

    /**
     * updates the tokens the player has already collected
     * @param activeP the active player
     * @param root the parent of all nodes
     */
    public static void updateCollectedTokens(BorderPane root, Player activeP){
        HBox tokenBox = (HBox)classifyChildren("collectedToken", root).get(0);
        //clear earlier tokens
        tokenBox.getChildren().clear();
        //print every wishing stone
        for (int i = 0; i < activeP.getTokens()[0]; i++){
            ImageView img = new ImageView(IMG_STONE);
            img.setFitHeight(40);
            img.setFitWidth(40);
            tokenBox.getChildren().add(img);
        }
        // for every mirror
        for (int i = 0; i < activeP.getTokens()[1]; i++){
            ImageView img = new ImageView(IMG_MIRROR);
            img.setFitHeight(40);
            img.setFitWidth(40);
            tokenBox.getChildren().add(img);
        }
    }

    /**
     * updates figures on field by adding player's symbol (as a label) to corresponding tilepane
     * @param root the BorderPane parent of all nodes
     * @param players list of all players
     * @param oraclePosition position of oracle
     */
    public static void updateFigures(BorderPane root, List<Player> players, int oraclePosition) {
        List<Node> circles = classifyChildren("circle", root);
        //clear all fields
        for (Node circle : circles) {
            ((TilePane)circle).getChildren().clear();
        }
        //print the figure symbols
        for (Player player : players) {
            for (Figure figure : player.getFigures()) {
                ((TilePane)circles.get(figure.getPos())).getChildren().add(new Label(Character.toString(player.getSymbol())));
            }
        }
        //print the oracle symbol
        ImageView image = new ImageView(IMG_ZOMBIE);
        image.setFitHeight(15);
        image.setFitWidth(15);
        ((TilePane)circles.get(oraclePosition)).getChildren().add(image);
    }

    /**
     * updates scores with corresponding player names
     * @param root BorderPane parent of all nodes
     * @param players list of all players
     */
    public static void updateScores(BorderPane root, List<Player> players) {
        List<Node> scores = classifyChildren("scorePlayer", root);
        List<Node> scoreNames = classifyChildren("scoreName", root);
        for (int i = 0; i < 4; i++) {
            if (i > players.size() - 1) {
                ((Label)scoreNames.get(i)).setText("");
                ((Label)scores.get(i)).setText("");
            } else {
                ((Label)scoreNames.get(i)).setText(players.get(i).getName());
                ((Label)scores.get(i)).setText(Integer.toString(players.get(i).getVictoryPoints()));
            }
        }
    }

    /**
     * updates the discard piles in the gui
     * @param root the parent of all
     * @param game the current game
     */
    public static void updateDiscardPiles(BorderPane root, Game game) {
        List<Node> stackPanes = classifyChildren("discardingPile", root);
        for(Node stackPane : stackPanes) {

            //get Discard Pile from ID
            char[] idArray = stackPane.getId().toCharArray();
            DiscardPile pile = game.getDiscardPile(idArray[idArray.length - 1]);

            Label lbl = (Label) ((StackPane)stackPane).getChildren().get(1);
            if (pile.isEmpty()){
                lbl.setText("");
            } else {
                lbl.setText(Integer.toString(pile.getTop().getNumber()));
            }
        }
    }

    /**
     * updates the hand cards, hand cards grey when players change
     * @param root parent of all nodes
     * @param state current state the game is in
     * @param activeP the active player
     */
    public static void updateCards(BorderPane root, State state , Player activeP) {
        List<Node> handCards = classifyChildren("handCard", root);
        if (!state.equals(PREPARATION)) {
            //get sorted hand cards
            List<Card> sortedHand = CardFunction.sortHand(activeP.getHand());
            for (int i = 0; i < handCards.size(); i++) {
                Rectangle card = (Rectangle) ((StackPane)handCards.get(i)).getChildren().get(0);
                Label cardNumber = (Label) ((StackPane)handCards.get(i)).getChildren().get(1);
                if (i < sortedHand.size()) {
                    cardNumber.setText(Integer.toString(sortedHand.get(i).getNumber()));
                    card.setFill(charToColor(sortedHand.get(i).getColor()));
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
     * adds the player names on end screen (in order: winning - losing)
     * @param winnerList list of players sorted by victory points
     * @param root the parent of all nodes
     */
    public static void createEndScores(List<Player> winnerList, BorderPane root) {
        List<Node> endScores = classifyChildren("endscores", root);
        for (int i = 0; i < 4; i++) {
            if (i > winnerList.size() - 1) {
                ((Label)endScores.get(i)).setText("");
            } else {
                ((Label)endScores.get(i)).setText(winnerList.get(i).getName());
            }
        }
    }

    /**
     * this method resets all effects from all gui objects
     * @param borderPane the parent of all children
     */
    @FXML
    public static void resetEffects(BorderPane borderPane) {
        for (Node node : getAllChildren(borderPane)) {
            node.setEffect(null);
        }
    }

    /**
     * loads a new scene
     * @param event event that triggers the new scene
     * @param sceneFile scene that should be loaded
     * @param max if setMaximized should be set or not
     * @param controller the controller to set in the new scene
     * @return the new Scene (borderpane)
     * @throws IOException if the FXML file is not found
     */
    public static BorderPane loadNewScene(Event event, String sceneFile, boolean max, GuiController controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GuiUpdate.class.getResource(sceneFile));
        fxmlLoader.setController(controller);
        BorderPane root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(max);
        stage.centerOnScreen();
        return root;
    }

}