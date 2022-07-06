package dhl.model;

import dhl.Constants;

import java.io.Serializable;
import java.util.*;

/**
 * This Class represents a Drawing Pile. It is realised as a Stack. When you create it it gets shuffled.
 * You can draw cards from the Top
 */
public class DrawingPile implements Serializable {

    private final List<Card> cards;

    /**
     * Constructor of the class
     * creates a new drawing pile
     */
    public DrawingPile() {
        cards = new ArrayList<>();
        for (int j = 0; j<2; j++) {
            for (char color : Constants.COLORS) {
                for (int i = 0; i <= 10; i++) {
                    cards.add(new Card(i, color));
                }
            }
        }
        Collections.shuffle(cards);
    }

    /**
     * Gets the top card of the Drawing Pile and removes it
     * @return the top card from the drawing pile
     */
    public Card draw() {
        return cards.remove(0);
    }


    /**
     * Checks if the drawing pile is empty
     * @return true if the cards list is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public List<Card> getCards() {
        return cards;
    }
}
