package dhl.model;

import java.util.ArrayList;

public class DiscardPile {
    private ArrayList<Card> pile;
    private char color;


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
     */
    public void add(Card card){
        if (card == null || card.getColor()!=color){
            System.out.println("Card is null or a different color than the pile (for your info carl");
            return;
        }
        pile.add(card);
    }

    /**
     * returns the top card (at the end of the list)
     * @return the Card at the top of the Pile
     */
    public Card getTop(){
        return pile.get(pile.size()-1);
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

    public ArrayList<Card> getPile (){
        return pile;
    }
}
