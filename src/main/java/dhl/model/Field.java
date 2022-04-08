package dhl.model;

import dhl.model.tokens.Token;

public class Field {
    private final int points;
    private final char color;
    private Token token = null;

    /**
     * Constructor for Field.
     * @param points the points of the field.
     * @param color the color of the field. (r, g, b, p, o) r = red, g = green, b = blue, p = purple, o = orange
     * The Token needs to be set later.
     */
    public Field(int points, char color) {
        this.points = points;
        this.color = color;
    }

    public int getPoints() {
        return points;
    }

    public char getColor() {
        return color;
    }

    /**
     * Returns the Token of the Field and removes it if it is collectable.
     * @return the Token of the field or null if there is no token on the field.
     */
    public Token getToken() {
        if(token != null && token.collectable()) {
            Token temp = token;
            token = null;
            return temp;
        }
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}