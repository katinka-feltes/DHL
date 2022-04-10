package dhl.model;

import java.util.ArrayList;

public class Player {

    private String name;

    private DirectionDiscardPile playedCardsRed;
    private DirectionDiscardPile playedCardsBlue;
    private DirectionDiscardPile playedCardsGreen;
    private DirectionDiscardPile playedCardsPurple;
    private DirectionDiscardPile playedCardsOrange;

    private ArrayList<Card> hand;

    int victoryPoints;
    boolean goblinSpecialPlayed;


    // figures, collected tokens and color need to be added

    public Player(String name){
        this.name = name;
        goblinSpecialPlayed = false;
        victoryPoints = 0;
        playedCardsRed = new DirectionDiscardPile('r');
        playedCardsBlue = new DirectionDiscardPile('g');
        playedCardsGreen = new DirectionDiscardPile('b');
        playedCardsPurple = new DirectionDiscardPile('p');
        playedCardsOrange = new DirectionDiscardPile('o');
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
    public ArrayList<Card> getHand() {
        return hand;
    }
    public int getVictoryPoints(){
        return victoryPoints;
    }
    public void setVictoryPoints (int victoryPoints){
        this.victoryPoints = victoryPoints;
    }
    public boolean isGoblinSpecialPlayed(){
        return goblinSpecialPlayed;
    }
}
