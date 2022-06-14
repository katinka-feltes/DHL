package dhl;

import dhl.controller.GuiController;
import javafx.scene.image.Image;

import java.util.Objects;

/**
 * The Class to save all the constants used in the game.
 */
public class Constants {
    public static final String RED = "#d34a41";
    public static final String GREEN = "#6a9a3d";
    public static final String BLUE = "#424ebc";
    public static final String PURPLE = "#a45ab9";
    public static final String ORANGE = "#f2af4b";
    public static final Image IMG_GOBLIN = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/goblin.png")));
    public static final Image IMG_MIRROR = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/mirror.png")));
    public static final Image IMG_SKULL = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/skull.png")));
    public static final Image IMG_SPIRAL = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/spiral.png")));
    public static final Image IMG_STONE = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/stone.png")));
    public static final Image IMG_WEB = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/web.png")));
    //add player to the players-list
    //other symbols
    //♛ \u265B BLACK CHESS QUEEN
    //♜ \u265C BLACK CHESS ROOK
    //♝ \u265D BLACK CHESS BISHOP
    //♞ \u265E BLACK CHESS KNIGHT
    //our symbols
    //♠ \u2660 BLACK SPADE SUIT
    //♣ \u2663 BLACK CLUB SUIT
    //♥ \u2665 BLACK HEART SUIT
    //♦ \u2666 BLACK DIAMOND SUIT
    public static final char[] symbols = {'\u2660', '\u2663', '\u2665', '\u2666'};

    private Constants() {
    }
}
