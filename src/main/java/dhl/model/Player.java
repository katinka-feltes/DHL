package dhl.model;

import dhl.model.tokens.Goblin;
import dhl.model.tokens.Mirror;
import dhl.model.tokens.Token;
import dhl.model.tokens.WishingStone;

import java.util.ArrayList;

/**
 * This Class represents a Player. A Player has a name, a symbol to recognize him by, a game (the current game he is playing),
 * DirectionalDiscardingPiles to discard his colored cards, a hand (8 cards drawn from the games drawing pile),
 * a list of figures and the last played figure and finaly a list of tokens.
 */
public class Player {

    private String name;
    private final char symbol;
    private final Game game;
    private final DirectionDiscardPile playedCardsRed;
    private final DirectionDiscardPile playedCardsBlue;
    private final DirectionDiscardPile playedCardsGreen;
    private final DirectionDiscardPile playedCardsPurple;
    private final DirectionDiscardPile playedCardsOrange;

    private Card lastTrashed;

    private final ArrayList<Card> hand;

    private int victoryPoints;
    private Figure lastMovedFigure;
    private boolean goblinSpecialPlayed;

    private final Figure[] figures = new Figure[3];

    private final ArrayList<Token> tokens;


    // collected tokens and color need to be added

    /**
     * the constructor of the player
     * @param name the players name
     * @param symbol the players symbol
     * @param game the current game status
     */
    public Player(String name, char symbol, Game game){
        this.name = name;
        this.symbol = symbol;
        this.game = game;
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
        tokens = new ArrayList<>();
    }

    /**
     * adds top card from drawing pile to hand
     */
    public void drawFromDrawingPile() {
        if (!game.getDrawingPile().isEmpty()){
            hand.add(game.getDrawingPile().draw()); //draws one card and adds it to the hand
        }
    }


    /**
     * adds card from discarding pile to active player's hand
     * @param pile discarding pile from which to draw
     * @throws Exception if chosen pile is empty or top is last-trashed card
     */
    public void drawFromDiscardingPile(DiscardPile pile) throws Exception {
        if(pile.isEmpty()){
            throw new Exception("You fool: this pile is empty!");
        } else if(pile.getTop() == lastTrashed){
            throw new Exception("You can't draw a card you just trashed!");
        }
        hand.add(pile.getAndRemoveTop());
    }

    /**
     * adds the card to the correctly colored directionDiscardPile and removes it from hand
     *
     * @param card card the player wants to play
     * @throws Exception from add(card)
     */
    public void addCardToPlayedCards(Card card) throws Exception {

        switch (card.getColor()) {
            case 'r':
                playedCardsRed.add(card);
                break;
            case 'g':
                playedCardsGreen.add(card);
                break;
            case 'b':
                playedCardsBlue.add(card);
                break;
            case 'p':
                playedCardsPurple.add(card);
                break;
            case 'o':
                playedCardsOrange.add(card);
                break;
            default:
                // do nothing
        }
        hand.remove(card);
    }

    /**
     * places the figure to the next correctly colored field
     *
     * @param fieldColor color of the field to place the figure on
     * @param figure the figure to move
     */
    public void placeFigure(char fieldColor, Figure figure) {
        victoryPoints -= Game.FIELDS[figure.getPos()].getPoints();
        figure.move(fieldColor);
        lastMovedFigure = figure;
        victoryPoints += Game.FIELDS[figure.getPos()].getPoints();
    }

    /**
     * @param figurePos 1 for the figure first on the board, 2 or 3
     * @return the figure on the field or null if there is none
     */
    public Figure getFigureByPos(int figurePos) {
        Figure chosen = figures[0];
        switch(figurePos){
            case 1:
                for(Figure f : figures){
                    if (f.getPos() > chosen.getPos()){
                        chosen = f;
                    }
                }
                break;
            case 2:
                for(Figure f : figures){
                    if (f != getFigureByPos(1) && f != getFigureByPos(3)){
                        chosen = f;
                    }
                }
                break;
            case 3:
                for(Figure f : figures){
                    if (f.getPos() < chosen.getPos()){
                        chosen = f;
                    }
                }
                break;
            default:
                // do nothing
        }
        return chosen;
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
            if (cardValue.length == 2 && handCard.getColor() == cardValue[0] && handCard.getNumber() == Character.getNumericValue(cardValue[1]) ||
                    cardValue.length == 3 && handCard.getNumber() == 10) {
                return handCard;
            }
        }
        throw new Exception("Card is not in Hand.");
    }

    /**
     * Allows to place a card on the discarding pile.
     * The card gets sorted to the correct color discarding pile.
     * @param card the card that gets discarded.
     * @throws Exception if the color of the card does not exist.
     */
    public void putCardOnDiscardingPile(Card card) throws Exception {
        hand.remove(card);
        switch (card.getColor()) {
            case 'r':
                game.getDiscardingPileRed().getPile().add(card);
                break;
            case 'g':
                game.getDiscardingPileGreen().add(card);
                break;
            case 'b':
                game.getDiscardingPileBlue().add(card);
                break;
            case 'p':
                game.getDiscardingPilePurple().add(card);
                break;
            case 'o':
                game.getDiscardingPileOrange().add(card);
                break;
            default:
                // do nothing
        }
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
     * The points from the collected tokens are calculated and added to the victory points
     */
    public void calcTokenPoints(){
        int stones = 0;
        int mirrors = 0;
        for (Token token : tokens){
            if(token instanceof WishingStone){
                stones++;
            } else if(token instanceof Mirror){
                mirrors++;
            }
        }
        victoryPoints += WishingStone.getValue(stones) * (int)Math.pow(2, mirors;
    }

    /**
     * checks if player is able to play at least one card from his hand
     * @return true if he can play at least one card
     */
    public boolean canPlay() {
        for (Card card : hand){
            if(cardFitsToAnyPile(card)){
                return true;
            }
        }
        return false;
    }

    /**
     * checks if card fits to any of the player's piles
     * @param card one card from the player's hand
     * @return true if card fits to appropriately colored pile
     */
    private boolean cardFitsToAnyPile(Card card){
        DirectionDiscardPile playedCards = getPlayedCards(card.getColor());
        if(playedCards != null){
            return playedCards.cardFitsToPile(card);
        }
        else {
            return false;
        }
    }

    /**
     * method to check if goblin special could be played
     * (checking figures, not if already played)
     * @return true if all figures are on a goblin field
     */
    public boolean allFiguresGoblin(){
        boolean result = true;
        for(Figure f : figures){
            // if the figure is not on a goblin field
            if(! (Game.FIELDS[f.getPos()].getToken() instanceof Goblin)){
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * calculate how many goblin points you would get
     * @return points as int (0, 5, 10 or 15)
     */
    public int goblinSpecialPoints (){
        if(allFiguresGoblin() && !goblinSpecialPlayed){
            int fieldOfFig0 = figures[0].getPos();
            if(getFigureAmountOnField(fieldOfFig0) == 3){
                //  all 3 on the same field
                return 5;
            } else if(getFigureAmountOnField(fieldOfFig0) == 2 || figures[1].getPos() == figures[2].getPos()){
                // 2 figures on the field from figure0 or the other 2 figures on the same field
                return 10;
            }
            else{
                // all figures on different fields
                return 15;
            }
        }
        return 0;
    }

    /**
     * play goblin special: add points to victory points
     */
    public void playGoblinSpecial(){
        victoryPoints+=goblinSpecialPoints();
        goblinSpecialPlayed = true;
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

    /**
     * counts the figures in the finish area.
     * @return the amount of figures in the finish area.
     */
    public int getFigureAmountInFinishArea(){
        int amount = 0;
        for(int field = 22; field <= 35; field++){ //should be 22 but for better testing 4
            amount += getFigureAmountOnField(field);
        }
        return amount;
    }

    /**
     * getter for the played card piles
     * @param color the color of the pile to return
     * @return the direction-discard-pile in the given color
     */
    public DirectionDiscardPile getPlayedCards(char color){
            switch (color) {
                case 'r':
                    return playedCardsRed;
                case 'g':
                    return playedCardsGreen;
                case 'b':
                    return playedCardsBlue;
                case 'p':
                    return playedCardsPurple;
                case 'o':
                    return playedCardsOrange;
                default:
                    return null;
        }
    }

    public Figure getLastMovedFigure(){
        return lastMovedFigure;
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

    public Game getGame() {
        return game;
    }

    public void setLastTrashed(Card lastTrashed) {
        this.lastTrashed = lastTrashed;
    }

    public Card getLastTrashed() {
        return lastTrashed;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public boolean isGoblinSpecialPlayed() {
        return goblinSpecialPlayed;
    }

    public char getSymbol() {
        return symbol;
    }
}


