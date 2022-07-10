package dhl.model;

import java.io.Serializable;

/**
 * This Class represents a Figure. A Figure has a position, a last position and a color.
 */
public class Figure implements Serializable {
    private int pos;
    private int latestPos;

    /**
     * Constructor for the Figure
     */
    public Figure() {
        pos = 0;
    }

    /**
     * moves this figure to the next correctly colored field
     * @param color the color of the field that the figure should move to
     * @throws Exception if the figure would leave the field
     */
    public void move(char color) throws Exception{
        latestPos = pos;
        pos+=FigureFunction.steps(pos, color); //move this figure the calculated amount of steps forward
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos){
        this.pos = pos;
    }

    public int getLatestPos() {
        return latestPos;
    }
}
