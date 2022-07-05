package dhl.model;

import dhl.Constants;

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
     */
    public void move(char color) {
        int steps = 1;
        while (Constants.BASIC_FIELD[pos + steps].getColor() != color) {
            steps++;
        }
        latestPos = pos;
        pos+=steps; //move this figure the calculated amount of steps forward
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
