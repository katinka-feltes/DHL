package dhl;

import dhl.controller.GuiController;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

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
    public static final Image IMG_SKULL1 = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/skull1.png")));
    public static final Image IMG_SKULL2 = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/skull2.png")));
    public static final Image IMG_SKULL3 = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/skull3.png")));
    public static final Image IMG_SKULL4 = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/skull4.png")));
    public static final Image IMG_SPIRAL = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/spiral.png")));
    public static final Image IMG_STONE = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/stone.png")));
    public static final Image IMG_WEB = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/web.png")));
    //add player to the players-list

    public static final char[] symbols = {'\u2660', '\u2663', '\u2665', '\u2666'};

    /**
     * changes a color from char to color
     *
     * @param color as a char
     * @return color according to string constant
     */
    public static Color charToColor(char color){
        switch (color){
            case 'r': return Color.web(RED);
            case 'g': return Color.web(GREEN);
            case 'b': return Color.web(BLUE);
            case 'p': return Color.web(PURPLE);
            case 'o': return Color.web(ORANGE);
            default: return Color.web("BLACK");
        }
    }
}
