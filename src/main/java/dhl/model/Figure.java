package dhl.model;

public class Figure {
    private int pos;
    private int latestPos;
    private char color;

    /**
     *  Constructor for the Figure
     * @param color the color that the figure will have
     */
    public Figure(char color) {
        pos = 0;
        this.color = color;
    }

    /**
     * moves this figure to the next correctly colored field
     * @param color the color of the field that the figure should move to
     */
    public void move(char color) {
        int steps = 1;
        while (Game.FIELDS[pos + steps].getColor() != color) {
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

    public char getColor() {return color; }

    public int getLatestPos() {
        return latestPos;
    }
}
