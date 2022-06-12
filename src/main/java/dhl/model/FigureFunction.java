package dhl.model;

import java.util.Comparator;
import java.util.List;

/**
 * This Class implements the function of the figures in the game
 */
public class FigureFunction {

    /**
     * @param fieldIndex the index of the field to get the figure-amount from
     * @return the amount of figures on the field as an int
     */
    public static int getFigureAmountOnField(int fieldIndex, List<Figure> figures) {
        int amount = 0;
        for (Figure f : figures) {
            if (f.getPos() == fieldIndex) {
                amount++;
            }
        }
        return amount;
    }

    /**
     * counts the figures in the finish area.
     * @return the amount of figures in the finish area.
     */
    public static int getFigureAmountInFinishArea(List<Figure> figures){
        int amount = 0;
        for(int field = 22; field <= 35; field++){ //should be 22 but for better testing 4
            amount += getFigureAmountOnField(field, figures);
        }
        return amount;
    }

    /**
     * @param figurePos 1 for the figure first on the board, 2 or 3
     * @return the figure on the field or null if there is none
     */
    public static Figure getFigureByPos(int figurePos, List<Figure> figures) {
        figures.sort(Comparator.comparing(Figure::getPos));
        return figures.get(figures.size() - figurePos);
    }

    public static boolean spiderwebIsPossible(Figure figure) {
        char color = Game.FIELDS[figure.getPos()].getColor();
        for(int pos = figure.getPos()+1; pos < 36; pos++) {
            if(Game.FIELDS[pos].getColor() == color) {
                return true;
            }
        }
        return false;
    }
}