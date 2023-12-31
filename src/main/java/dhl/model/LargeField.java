package dhl.model;

import dhl.model.tokens.Token;
import dhl.model.tokens.WishingStone;

import java.io.Serializable;

/**
 * This Class represents a Large Field. A Large Field is a Field with another Token on it.
 */
public class LargeField extends Field implements Serializable {
    private Token tokenTwo = null;

    /**
     * Constructor for Field.
     *
     * @param points the points of the field.
     * @param color  the color of the field. (r, g, b, p, o, w) r = red, g = green, b = blue, p = purple, o = orange,
     *               w = white
     * @param wishingStoneAmount how many wishing points the large field has
     */
    public LargeField(int points, char color, int wishingStoneAmount) {
        super(points, color);
        if (wishingStoneAmount > 0) {
            super.setToken(new WishingStone());
            if (wishingStoneAmount == 2) {
                tokenTwo = new WishingStone                                                                                                                        ();
            }
        }
    }

    /**
     * returns and removes the second token if there is one otherwise the first one or nothing if there is none.
     * @return either the first or the second token or null.
     */
    @Override
    public Token collectToken() {
        if (tokenTwo != null) {
            Token temp = tokenTwo;
            tokenTwo = null;
            return temp;
        } else if (super.getToken() != null) {
            Token temp = super.collectToken();
            super.setToken(null);
            return temp;
        }
       return null;
    }

    public Token getTokenTwo(){
        return tokenTwo;
    }

}
