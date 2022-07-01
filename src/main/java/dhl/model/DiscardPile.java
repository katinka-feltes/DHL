package dhl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class represents a Discard Pile. You can discard any card on it, you want.
 */
public class DiscardPile  implements Serializable {
    protected final List<Card> pile;
    protected final char color;

    /**
     * The constructor for DiscardPile
     * @param color the color that the pile will have
     */
    public DiscardPile(char color){
        pile = new ArrayList<>();
        this.color = color;
    }

    /**
     * adds card to top (end) of the pile if the color is the same
     * @param card the card that is added
     * @throws Exception if card is null or a different color than the pile
     */
    public void add(Card card) throws Exception {
        if (card == null || card.getColor()!=color){
            throw new Exception("Card is null or a different color than the pile.");
        }
        pile.add(card);
    }

    /**
     * returns the top card (at the end of the list)
     * @return the Card at the top of the Pile
     */
    public Card getTop() {
        if (pile.isEmpty()){
            return null;
        }
        return pile.get(pile.size() - 1);
    }

    /**
     * returns and removes the top card (from the end of the list)
     * @return the Card that was removed from the top of the Pile
     */
    public Card getAndRemoveTop(){
        return pile.remove(pile.size()-1);
    }

    public char getColor() {
        return color;
    }

    public List<Card> getPile (){
        return pile;
    }

    /**
     * @return true if the pile is empty
     */
    public boolean isEmpty(){
        return pile.isEmpty();
    }
}
