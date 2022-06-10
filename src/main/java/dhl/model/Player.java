package dhl.model;

import dhl.controller.player_logic.AI;
import dhl.controller.player_logic.PlayerLogic;
import dhl.model.tokens.Goblin;
import dhl.model.tokens.Mirror;
import dhl.model.tokens.Token;
import dhl.model.tokens.WishingStone;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class represents a Player. A Player has a name, a symbol to recognize him by, a game (the current game he is playing),
 * DirectionalDiscardingPiles to discard his colored cards, a hand (8 cards drawn from the games drawing pile),
 * a list of figures and the last played figure and finally a list of tokens.
 */
public class Player {

    private final PlayerLogic playerLogic;
    private String name;
    private final char symbol;
    private Game game;
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
     *
     * @param name   the players name
     * @param symbol the players symbol
     */
    public Player(String name, char symbol, PlayerLogic logic){
        this.playerLogic = logic;
        if (logic instanceof AI){
            ((AI) logic).setSelf(this);
        }
        this.name = name;
        this.symbol = symbol;
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
     * Allows to place a card on the discarding pile.
     * The card gets sorted to the correct color discarding pile.
     * @param card the card that gets discarded.
     * @throws Exception if the color of the card does not exist.
     */
    public void putCardOnDiscardingPile(Card card) throws Exception {
        hand.remove(card);
        game.getDiscardPile(card.getColor()).add(card);
    }

    /**
     * The points from the collected tokens are calculated and added to the victory points
     */
    public int calcTokenPoints(){
        int stones = 0;
        int mirrors = 0;
        for (Token token : tokens){
            if(token instanceof WishingStone){
                stones++;
            } else if(token instanceof Mirror){
                mirrors++;
            }
        }
        return WishingStone.getValue(stones) * (int)Math.pow(2, mirrors);
    }

    /**
     * checks if player is able to play at least one card from his hand
     * @return true if he can play at least one card
     */
    public boolean canPlay() {
        for (Card card : hand){
            if(CardFunction.cardFitsToPlayersPiles(card, getPlayedCards(card.getColor()))){
                return true;
            }
        }
        return false;
    }

    /**
     * method to check how many figures stand on a field with a goblin
     *
     * @return the amount of figures on a goblin field as an int
     */
    public int amountFiguresGoblin(){
        int result = 0;
        for(Figure f : figures){
            // if the figure is not on a goblin field
            if(Game.FIELDS[f.getPos()].getToken() instanceof Goblin){
                result++;
            }
        }
        return result;
    }

    /**
     * calculate how many goblin points you would get
     * @return points as int (0, 5, 10 or 15)
     */
    public int goblinSpecialPoints (){
        if(amountFiguresGoblin()==3 && !goblinSpecialPlayed){
            int fieldOfFig0 = figures.get(0).getPos();
            if(FigureFunction.getFigureAmountOnField(fieldOfFig0, getFigures()) == 3){
                //  all 3 on the same field
                return 5;
            } else if(FigureFunction.getFigureAmountOnField(fieldOfFig0, getFigures()) == 2 || figures.get(1).getPos() == figures.get(2).getPos()){
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
    public DirectionDiscardPile[] getPlayedCards() {return playedCards;}

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
    public PlayerLogic getPlayerLogic() {return playerLogic;}

    public void setGame(Game game) {
        this.game = game;
    }
}