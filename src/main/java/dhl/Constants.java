package dhl;

import dhl.controller.GuiController;
import dhl.model.Field;
import dhl.model.LargeField;
import dhl.model.tokens.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * The Class to save all the constants used in the game.
 */
public class Constants {
    public static final int PORT_NUMBER = 4444;
    public static final char[] COLORS = {'r','g','b','p','o'};
    public static final String RED = "#d34a41";
    public static final String GREEN = "#6a9a3d";
    public static final String BLUE = "#424ebc";
    public static final String PURPLE = "#a45ab9";
    public static final String ORANGE = "#f2af4b";
    public static final Image IMG_ZOMBIE = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/zombie.png")));
    public static final Image IMG_GOBLIN = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/goblin.png")));
    public static final Image IMG_MIRROR = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/mirror.png")));
    public static final Image IMG_SKULL1 = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/skull1.PNG")));
    public static final Image IMG_SKULL2 = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/skull2.PNG")));
    public static final Image IMG_SKULL3 = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/skull3.PNG")));
    public static final Image IMG_SKULL4 = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/skull4.PNG")));
    public static final Image IMG_SPIRAL = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/spiral.png")));
    public static final Image IMG_STONE = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/stone.png")));
    public static final Image IMG_WEB = new Image(Objects.requireNonNull(GuiController.class.getResourceAsStream("/web.png")));
    public static final char[] symbols = {'\u2660', '\u2663', '\u2665', '\u2666'};
    public static int figureAmount = 3;
    public static int figuresInFinishOfOnePlayer = 3;
    //the amount of figures in the finish area of one player to end the game
    public static int totalFiguresInFinish = 5;
    //how many figures in total in the finish area end the game
    public static final Field[] BASIC_FIELD = {
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
            new LargeField(10, 'b', 1)};

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

    /**
     * gets the correct image to get given token
     * @param token the token to get the matching img for
     * @return Image that shows the given token
     */
    public static Image getTokenImage(Token token) {
        if (token instanceof Mirror) {
            return IMG_MIRROR;
        } else if (token instanceof Goblin) {
            return IMG_GOBLIN;
        } else if (token instanceof Skullpoint) {
            return getSkullImage(((Skullpoint) token).getPoints());
        } else if (token instanceof Spiral) {
            return IMG_SPIRAL;
        } else if (token instanceof Spiderweb) {
            return IMG_WEB;
        } else if (token instanceof WishingStone) {
            return IMG_STONE;
        }
        return null;
    }

    private static Image getSkullImage(int skullpoints) {
        switch (skullpoints){
            case 1:
                return IMG_SKULL1;
            case 2:
                return IMG_SKULL2;
            case 3:
                return IMG_SKULL3;
            case 4:
                return IMG_SKULL4;
            default:
                // do nothing
        }
        return null;
    }
}
