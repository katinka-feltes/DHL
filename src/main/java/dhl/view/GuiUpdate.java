package dhl.view;

import dhl.model.Figure;
import dhl.model.Game;
import dhl.model.LargeField;
import dhl.model.Player;
import dhl.model.tokens.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

import java.util.List;

import static dhl.Constants.*;

public class GuiUpdate {

    /**
     * updates the tokens on the field
     */
    public static void updateTokens(List<Node> tokens) {
        int currentToken = 0;
        while (currentToken <= 39) {
            for (int i = 1; i <= 35; i++) {
                if (Game.FIELDS[i].getToken() == null) {
                    ((ImageView)tokens.get(currentToken)).setImage(null);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Mirror) {
                    ((ImageView)tokens.get(currentToken)).setImage(IMG_MIRROR);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Goblin) {
                    ((ImageView)tokens.get(currentToken)).setImage(IMG_GOBLIN);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Skullpoint && ((Skullpoint) Game.FIELDS[i].getToken()).getPoints() == 1) {
                    ((ImageView)tokens.get(currentToken)).setImage(IMG_SKULL1);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Skullpoint && ((Skullpoint) Game.FIELDS[i].getToken()).getPoints() == 2) {
                    ((ImageView)tokens.get(currentToken)).setImage(IMG_SKULL2);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Skullpoint && ((Skullpoint) Game.FIELDS[i].getToken()).getPoints() == 3) {
                    ((ImageView)tokens.get(currentToken)).setImage(IMG_SKULL3);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Skullpoint && ((Skullpoint) Game.FIELDS[i].getToken()).getPoints() == 4) {
                    ((ImageView)tokens.get(currentToken)).setImage(IMG_SKULL4);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Spiral) {
                    ((ImageView)tokens.get(currentToken)).setImage(IMG_SPIRAL);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof Spiderweb) {
                    ((ImageView)tokens.get(currentToken)).setImage(IMG_WEB);
                    currentToken++;
                } else if (Game.FIELDS[i].getToken() instanceof WishingStone) {
                    ((ImageView)tokens.get(currentToken)).setImage(IMG_STONE);
                    currentToken++;
                }
                //if large field, check the second token
                if (Game.FIELDS[i] instanceof LargeField && ((LargeField) Game.FIELDS[i]).getTokenTwo() != null) {
                    ((ImageView)tokens.get(currentToken)).setImage(IMG_STONE);
                    currentToken++;
                } else if (Game.FIELDS[i] instanceof LargeField) {
                    ((ImageView)tokens.get(currentToken)).setImage(null);
                    currentToken++;
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
    public static void updateFigures(List<Node> circles, List<Player> players) {
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
}