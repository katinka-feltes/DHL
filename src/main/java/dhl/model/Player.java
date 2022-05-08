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

    private Figure[] figures = new Figure[3];


    // collected tokens and color need to be added

    public Player(String name, char symbol){
        this.name = name;
        this.symbol = symbol;
        goblinSpecialPlayed = false;
        victoryPoints = 0;
        playedCardsRed = new DirectionDiscardPile('r');
        playedCardsBlue = new DirectionDiscardPile('b');
        playedCardsGreen = new DirectionDiscardPile('g');
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

    public void drawFromDiscardingPile(DiscardPile pile, Card lastTrash) throws Exception {
        if(pile.isEmpty()){
            throw new Exception("You fool: this pile is empty!");
        } else if(pile.getTop() == lastTrash){
            throw new Exception("You can't draw a card you just trashed!");
        }
        hand.add(pile.getAndRemoveTop());
    }

    /**
     * adds the card to the correctly colored directionDiscardPile and removes it from hand
     *
     * @param card card the player wants to play
     */
    public void addCardToPlayedCards(Card card) throws Exception {

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
        hand.remove(card);
    }

    /**
     * places the figure to the next correctly colored field
     *
     * @param cardColor color of the played card
     * @param figure the position of the figure to move (1, 2 or 3)
     */
    public void placeFigure(char cardColor, int figure) {
        victoryPoints -= Game.FIELDS[getFigureByPos(figure).getPos()].getPoints();
        getFigureByPos(figure).move(cardColor);
        victoryPoints += Game.FIELDS[getFigureByPos(figure).getPos()].getPoints();
    }

    /**
     * @param figurePos 1 for the figure first on the board, 2 or 3
     * @return the figure on the field or null if there is none
     */
    public Figure getFigureByPos(int figurePos) {
        Figure chosen = figures[0];
        switch(figurePos){
            case(1):
                for(Figure f : figures){
                    if (f.getPos() > chosen.getPos()){
                        chosen = f;
                    }
                }
                break;
            case(2):
                for(Figure f : figures){
                    if (f != getFigureByPos(1) && f != getFigureByPos(3)){
                        chosen = f;
                    }
                }
                break;
            case(3):
                for(Figure f : figures){
                    if (f.getPos() < chosen.getPos()){
                        chosen = f;
                    }
                }
                break;
        }
        return chosen;
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
     * sorts the hand of the player by number and color
     * @return hand sorted by number and color as an ArrayList
     */
    public ArrayList<Card> getSortedHand() {
        ArrayList<Card> unsortedHand = getHand();
        ArrayList<Card> blueCards = new ArrayList<>();
        ArrayList<Card> greenCards = new ArrayList<>();
        ArrayList<Card> orangeCards = new ArrayList<>();
        ArrayList<Card> purpleCards = new ArrayList<>();
        ArrayList<Card> redCards = new ArrayList<>();
        ArrayList<Card> sortedHand = new ArrayList<>();

        for(Card card: unsortedHand) {
            if(card.getColor() == 'b') {
                blueCards.add(card);
            } else if(card.getColor() == 'g') {
                greenCards.add(card);
            } else if(card.getColor() == 'o') {
                orangeCards.add(card);
            } else if(card.getColor() == 'p') {
                purpleCards.add(card);
            } else if(card.getColor() == 'r') {
                redCards.add(card);
            }
        }
        sortedHand.addAll(sortColorList(blueCards));
        sortedHand.addAll(sortColorList(greenCards));
        sortedHand.addAll(sortColorList(orangeCards));
        sortedHand.addAll(sortColorList(purpleCards));
        sortedHand.addAll(sortColorList(redCards));

        return sortedHand;
    }

    /**
     * sorts given ArrayList by number
     * @param colorList list of cards already sorted by color
     * @return cards of a specific color sorted by number as an ArrayList
     */
    public ArrayList<Card> sortColorList(ArrayList<Card> colorList) {
        ArrayList<Card> sortedColorList = new ArrayList<>();
        for(int i=0; i<=10; i++) {
            for(Card card: colorList) {
                if (card.getNumber() == i) {
                    sortedColorList.add(card);
                }
            }
        }
        return sortedColorList;
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
    public int getFigureAmountInFinishArea(){
        int amount = 0;
        for(int field = 4; field <= 35; field++){ //should be 22 but for better testing 4
            amount += getFigureAmountOnField(field);
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

    public ArrayList<Card> getHand() {
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


