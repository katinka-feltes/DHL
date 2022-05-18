package dhl.model;

import java.util.Collections;
import java.util.Stack;

/**
 * This Class represents a Drawing Pile. It is realised as a Stack. When you create it it gets shuffled.
 * You can draw cards from the Top
 */
public class DrawingPile {

    private final Stack<Card> cards;

    /**
     * Constructor of the class
     * creates a new drawing pile
     */
    public DrawingPile() {
        char[] colors2x = {'r', 'g', 'b', 'p', 'o', 'r', 'g', 'b', 'p', 'o'};
        cards = new Stack<>();
        for (char color : colors2x) {
            for (int i = 0; i <= 10; i++) {
                cards.add(new Card(i, color)) ;
            }
        }
        Collections.shuffle(cards);
    }

    /**
     * Gets the top card of the Drawing Pile and removes it
     * @return the top card from the drawing pile
     */
    public Card draw() {
        return cards.pop();
    }


    /**
     * Checks if the drawing pile is empty
     * @return true if the cards list is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Stack<Card> getCards() {
        return cards;
    }
}
