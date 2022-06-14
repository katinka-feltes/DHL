package dhl;

import dhl.controller.GuiController;
import dhl.model.Field;
import dhl.model.LargeField;
import javafx.scene.image.Image;

import java.util.Objects;

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
    public static final Field[] FIELDS = {
            new LargeField(0, 'w', 0),
            new Field(-4, 'p'),
            new Field(-4, 'o'),
            new Field(-4, 'r'),
            new Field(-4, 'g'),
            new Field(-4, 'b'),
            new Field(-4, 'p'),
            new LargeField(-3, 'p', 2),
            new Field(-3, 'o'),
            new Field(-3, 'r'),
            new Field(-2, 'g'),
            new Field(-2, 'b'),
            new Field(-2, 'p'),
            new Field(-2, 'o'),
            new LargeField(1, 'o', 2),
            new Field(1, 'r'),
            new Field(1, 'g'),
            new Field(2, 'b'),
            new Field(2, 'p'),
            new Field(2, 'o'),
            new Field(2, 'r'),
            new LargeField(3, 'r', 2),
            new Field(3, 'g'),
            new Field(3, 'b'),
            new Field(3, 'p'),
            new Field(5, 'o'),
            new Field(5, 'r'),
            new Field(5, 'g'),
            new LargeField(5, 'g', 2),
            new Field(6, 'b'),
            new Field(6, 'p'),
            new Field(6, 'o'),
            new Field(7, 'r'),
            new Field(7, 'g'),
            new Field(7, 'b'),
            new LargeField(10, 'b', 1)
    };
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
    public static char[] symbols = {'\u2660', '\u2663', '\u2665', '\u2666'};
}
