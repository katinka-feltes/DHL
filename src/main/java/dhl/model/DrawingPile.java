package dhl.model;

import java.util.Collections;
import java.util.Stack;

public class DrawingPile {

    private Stack<Card> cards;

    /**
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

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
