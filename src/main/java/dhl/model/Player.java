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

    private Figure figure1;
    private Figure figure2;
    private Figure figure3;


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
        figure1 = new Figure('b'); //muss noch irgendwie farbe übergeben werden
        figure2 = new Figure('b'); //muss noch irgendwie farbe übergeben werden
        figure3 = new Figure('b'); //muss noch irgendwie farbe übergeben werden

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

    public Figure getFigure1() {
        return figure1;
    }

    public Figure getFigure2() {
        return figure2;
    }

    public Figure getFigure3() {
        return figure3;
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
