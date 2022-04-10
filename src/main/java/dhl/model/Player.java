package dhl.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;

    private List<Card> playedCardsRed;
    private List<Card> playedCardsBlue;
    private List<Card> playedCardsGreen;
    private List<Card> playedCardsPurple;
    private List<Card> playedCardsOrange;

    private List<Card> hand;

    // figures, collected tokens and color need to be added

    public Player(String name){
        this.name = name;
        boolean goblinSpecialPlayed = false;
        int victoryPoints = 0;
        playedCardsRed = new ArrayList<>();
        playedCardsBlue = new ArrayList<>();
        playedCardsGreen = new ArrayList<>();
        playedCardsPurple = new ArrayList<>();
        playedCardsOrange = new ArrayList<>();
        hand = new ArrayList<>();
        int position = 0;
        //need to draw cards here and maybe initialize the lists here as well

    }

    /**
     * fills the hand up to eight cards
     * @param drawingPile the global drawingPile
     */
    public void drawCardsUpToEight (DrawingPile drawingPile) {
        if (drawingPile != null){
            while (hand.size() < 8 && !drawingPile.isEmpty()) {
                hand.add(drawingPile.draw()); //draws one card and adds it to the hand
            }
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public List<Card> getHand() {
        return hand;
    }
}
