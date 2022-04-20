package dhl.model;

public class Figure {
    private int pos;
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
     *
     * @param color the color of the field that the figure should move to
     * @return the amount of steps that the figure will move to get to the colored field as an int
     */
    public int move(char color) {
        int steps = 1;
        while (true) {
            if (Game.FIELDS[pos + steps].getColor() != color) {
                steps++;
            }else {
                break;
            }
        }
        return steps;
    }

    public int getPos() {
        return pos;
    }

    public char getColor() {return color; }
}
