package dhl.model;

import java.util.ArrayList;

public class Player {

    private String name;
    private char symbol;

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


    // collected tokens and color need to be added

    public Player(String name, char symbol){
        this.name = name;
        this.symbol = symbol;
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

    /**
     * adds the card to the correctly colored directionDiscardPile
     * then player can place a figure or the oracle
     * @param card card the player wants to play
     */
    public void playCard (Card card) {
        switch(card.getColor()) {
            case('r'): playedCardsRed.add(card);
            case('g'): playedCardsGreen.add(card);
            case('b'): playedCardsBlue.add(card);
            case('p'): playedCardsPurple.add(card);
            case('o'): playedCardsOrange.add(card);
        }

        // choice between placing figure and placing oracle needs to be added
        // call placeFigure by getting chosenFigure (somehow)
    }

    /**
     * places the figure to the next correctly colored field
     * @param cardColor color of the played card
     * @param chosenFigure figure the player wants to move forward
     */
    public void placeFigure(char cardColor, Figure chosenFigure) {
        chosenFigure.move(cardColor);
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

    public char getSymbol() {
        return symbol;
    }
    public void setSymbol(char symbol) {
        this.symbol = symbol;
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
