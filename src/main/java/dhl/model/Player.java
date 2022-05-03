package dhl.model;

import java.util.ArrayList;
import java.util.List;

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

    private Figure[] figures = new Figure[3];


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
        figures[0] = new Figure(symbol);
        figures[1] = new Figure(symbol);
        figures[2] = new Figure(symbol);
    }

    /**
     * fills the hand up to eight cards
     * @param drawingPile the global drawingPile
     */
    public void drawCardsUpToEight(DrawingPile drawingPile) {
        if (drawingPile != null) {
            while (hand.size() < 8 && !drawingPile.isEmpty()) {
                hand.add(drawingPile.draw()); //draws one card and adds it to the hand
            }
        }
    }

    /**
     * adds the card to the correctly colored directionDiscardPile and removes it from hand
     *
     * @param card card the player wants to play
     */
    public void addCardToPlayedCards(Card card) throws Exception {
        hand.remove(card);

        switch (card.getColor()) {
            case ('r'):
                playedCardsRed.add(card);
                break;
            case ('g'):
                playedCardsGreen.add(card);
                break;
            case ('b'):
                playedCardsBlue.add(card);
                break;
            case ('p'):
                playedCardsPurple.add(card);
                break;
            case ('o'):
                playedCardsOrange.add(card);
                break;
            default:
                throw new Exception("Card color does not exist");
        }
    }

    /**
     * places the figure to the next correctly colored field
     *
     * @param cardColor color of the played card
     * @param figurePos the position of the figure to move
     */
    public void placeFigure(char cardColor, int figurePos) throws Exception {
        getFigureOnField(figurePos).move(cardColor);
    }

    /**
     * @param fieldIndex the index of the field to get the figure from
     * @return the figure on the field or null if there is none
     */
    public Figure getFigureOnField(int fieldIndex) throws Exception {
        for (Figure f : figures) {
            if (f.getPos() == fieldIndex) {
                return f;
            }
        }
        throw new Exception("No figure on this field.");
    }

    /**
     * Get a card from hand
     * @param cardAsString the card to get from the hand as a string
     * @return the Card
     * @throws Exception if card is not in Hand
     */
    public Card getCardFromHand(String cardAsString) throws Exception {

        char[] cardValue = cardAsString.toCharArray();
        for (Card handCard : hand) {
            // if number and color match current card from hand (if the length of value is 3, the number always is 10)
            if ((cardValue.length == 2 && handCard.getColor() == cardValue[0] && handCard.getNumber() == Character.getNumericValue(cardValue[1])) ||
                    (cardValue.length == 3 && handCard.getNumber() == 10)) {
                return handCard;
            }
        }
        throw new Exception("Card is not in Hand.");
    }

    /**
     * @param fieldIndex the index of the field to get the figure-amount from
     * @return the amount of figures on the field as an int
     */
    public int getFigureAmountOnField(int fieldIndex) {
        int amount = 0;
        for (Figure f : figures) {
            if (f.getPos() == fieldIndex) {
                amount++;
            }
        }
        return amount;
    }

    public Figure[] getFigures() {
        return figures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public DirectionDiscardPile getPlayedCardsRed() {
        return playedCardsRed;
    }
    public DirectionDiscardPile getPlayedCardsBlue() {
        return playedCardsBlue;
    }
    public DirectionDiscardPile getPlayedCardsGreen() {
        return playedCardsGreen;
    }
    public DirectionDiscardPile getPlayedCardsPurple() {
        return playedCardsPurple;
    }
    public DirectionDiscardPile getPlayedCardsOrange() {
        return playedCardsOrange;
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public boolean isGoblinSpecialPlayed() {
        return goblinSpecialPlayed;
    }
}
