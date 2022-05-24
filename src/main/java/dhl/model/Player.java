package dhl.model;

import dhl.model.tokens.Goblin;
import dhl.model.tokens.Mirror;
import dhl.model.tokens.Token;
import dhl.model.tokens.WishingStone;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This Class represents a Player. A Player has a name, a symbol to recognize him by, a game (the current game he is playing),
 * DirectionalDiscardingPiles to discard his colored cards, a hand (8 cards drawn from the games drawing pile),
 * a list of figures and the last played figure and finally a list of tokens.
 */
public class Player {

    private String name;
    private final char symbol;
    private final Game game;
    private final DirectionDiscardPile[] playedCards = new DirectionDiscardPile[5];

    private Card lastTrashed;

    private final List<Card> hand;

    private int victoryPoints;
    private Figure lastMovedFigure;
    private boolean goblinSpecialPlayed;

    private final List<Figure> figures = new ArrayList<>();

    private final List<Token> tokens;

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
        playedCards[0] = new DirectionDiscardPile('r');
        playedCards[1] = new DirectionDiscardPile('b');
        playedCards[2] = new DirectionDiscardPile('g');
        playedCards[3] = new DirectionDiscardPile('p');
        playedCards[4] = new DirectionDiscardPile('o');
        hand = new ArrayList<>();
        figures.add(new Figure(symbol));
        figures.add(new Figure(symbol));
        figures.add(new Figure(symbol));
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
        figures.sort(Comparator.comparing(Figure::getPos));
        return figures.get(figures.size() - figurePos);
    }

    /**
     * Allows to place a card on the discarding pile.
     * The card gets sorted to the correct color discarding pile.
     * @param card the card that gets discarded.
     * @throws Exception if the color of the card does not exist.
     */
    public void putCardOnDiscardingPile(Card card) throws Exception {
        hand.remove(card);
        game.getDiscardPile(card.getColor()).add(card);
        //game.getDiscardPile(card.getColor()).getPile().add(card);
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
        victoryPoints += WishingStone.getValue(stones) * (int)Math.pow(2, mirrors);
    }

    /**
     * checks if player is able to play at least one card from his hand
     * @return true if he can play at least one card
     */
    public boolean canPlay() {
        for (Card card : hand){
            if(CardFunction.cardFitsToAnyPile(card, getPlayedCards(card.getColor()))){
                return true;
            }
        }
        return false;
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
            int fieldOfFig0 = figures.get(0).getPos();
            if(getFigureAmountOnField(fieldOfFig0) == 3){
                //  all 3 on the same field
                return 5;
            } else if(getFigureAmountOnField(fieldOfFig0) == 2 || figures.get(1).getPos() == figures.get(2).getPos()){
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
        for (DirectionDiscardPile pile : playedCards){
            if(pile.getColor() == color){
                return pile;
            }
        }
        return null;
    }

    public Figure getLastMovedFigure(){
        return lastMovedFigure;
    }
    public List<Figure> getFigures() {
        return figures;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Card> getHand() {
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
    public List<Token> getTokens() {
        return tokens;
    }
    public boolean isGoblinSpecialPlayed() {
        return goblinSpecialPlayed;
    }
    public char getSymbol() {
        return symbol;
    }
}