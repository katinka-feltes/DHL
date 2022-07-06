package dhl.model;

import dhl.Constants;

import java.util.Comparator;
import java.util.List;

/**
 * This Class implements the function of the figures in the game
 */
public class FigureFunction {
    private FigureFunction(){
    }

    /**
     * @param fieldIndex the index of the field to get the figure-amount from
     * @param figures the figures to take into account
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
     * @param figures the figures to take into account
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
     * @param figures the figures to take into account
     * @return the figure on the field or null if there is none
     */
    public static Figure getFigureByPos(int figurePos, List<Figure> figures) {
        figures.sort(Comparator.comparing(Figure::getPos));
        return figures.get(figures.size() - figurePos);
    }

    /**
     * @param fieldIndex the index of the field to get the figure from
     * @param figures the figures to take into account
     * @return the figure on the field or null if there is none
     * @throws Exception if there is no figure on the given field
     */
    public static Figure getFigureOnField(int fieldIndex, List<Figure> figures) throws Exception {
        for (Figure f : figures) {
            if (f.getPos() == fieldIndex) {
                return f;
            }
        }
        throw new Exception("No figure on this field.");
    }

    /**
     * returns if the spiderweb on the figure's field can be executed
     * @param figure the figure which is on a spiderweb field
     * @return true if the another field of the current color exists within the field
     */
    public static boolean spiderwebIsPossible(Figure figure) {
        char color = Constants.BASIC_FIELD[figure.getPos()].getColor();
        for(int pos = figure.getPos()+1; pos < 36; pos++) {
            if(Constants.BASIC_FIELD[pos].getColor() == color) {
                return true;
            }
        }
        return false;
    }
}