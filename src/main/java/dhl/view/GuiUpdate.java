package dhl.view;

import dhl.Constants;
import dhl.controller.GuiController;
import dhl.controller.State;
import dhl.model.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

import static dhl.Constants.*;
import static dhl.controller.State.PREPARATION;

public class GuiUpdate {

    /**
     * updates the tokens on the field
     */
    public static void updateTokens(List<Node> tokens, Field[] fields) {
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
     * @param tokenBox the box in which the tokens are displayed
     */
    public static void updateCollectedTokens(Player activeP, HBox tokenBox){
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
     */
    public static void updateFigures(List<Node> circles, List<Player> players, int oraclePosition) {
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
     */
    public static void updateScores(List<Node> scores, List<Node> scoreNames, List<Player> players) {
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

    public static void updateDiscardPiles(List<Node> stackPanes, Game game) {
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
     */
    public static void updateCards(List<Node> handCards, State state , Player activeP) {
        if (!state.equals(PREPARATION)) {
            //get sorted hand cards
            List<Card> sortedHand = CardFunction.sortHand(activeP.getHand());
            for (int i = 0; i < handCards.size(); i++) {
                Rectangle card = (Rectangle) ((StackPane)handCards.get(i)).getChildren().get(0);
                Label cardNumber = (Label) ((StackPane)handCards.get(i)).getChildren().get(1);
                if (i < sortedHand.size()) {
                    cardNumber.setText(Integer.toString(sortedHand.get(i).getNumber()));
                    card.setFill(Constants.charToColor(sortedHand.get(i).getColor()));
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
     * this method resets all effects from all gui objects
     * @param borderPane the parent of all children
     */
    @FXML
    public static void resetEffects(BorderPane borderPane) {
        for (Node node : GuiController.getAllChildren(borderPane)) {
            node.setEffect(null);
        }
    }

}

