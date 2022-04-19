package dhl.model;

public class Figure {
    private int pos;
    private char color;

    public Figure(char color) {
        pos = 0;
        this.color = color;
    }

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
}
