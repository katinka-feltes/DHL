package dhl.model;

import dhl.Constants;
import dhl.controller.player_logic.AI;
import dhl.controller.player_logic.PlayerLogic;
import dhl.model.tokens.Goblin;
import dhl.model.tokens.WishingStone;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static dhl.Constants.figureAmount;

/**
 * This Class represents a Player. A Player has a name, a symbol to recognize him by, a game (the current game he is playing),
 * DirectionalDiscardingPiles to discard his colored cards, a hand (8 cards drawn from the games drawing pile),
 * a list of figures and the last played figure and finally a list of tokens.
 */
public class Player implements Serializable {
    private final PlayerLogic playerLogic;
    private final String name;
    private final char symbol;
    private Game game;
    private final DirectionDiscardPile[] playedCards = new DirectionDiscardPile[5];
    private Card lastTrashed;
    private final List<Card> hand;
    private int victoryPoints;
    private Figure lastMovedFigure;
    private boolean goblinSpecialPlayed;
    private final List<Figure> figures = new ArrayList<>();
    private final int[] tokens = {0, 0}; //amount WishingStones, amount Mirrors

    /**
     * the constructor of the player
     *
     * @param name   the players name
     * @param symbol the players symbol
     * @param logic ai or human
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
        for (int i = 0; i< figureAmount; i++) {
            figures.add(new Figure());
        }
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
    public void placeFigure(char fieldColor, Figure figure) throws Exception{
        victoryPoints -= Constants.BASIC_FIELD[figure.getPos()].getPoints();
        figure.move(fieldColor);
        lastMovedFigure = figure;
        victoryPoints += Constants.BASIC_FIELD[figure.getPos()].getPoints();
    }

    /**
     * moves the game's oracle figure forward
     * and adds 5 points to the victory points if a figure is on the new field
     * @param steps the amount of steps the oracle shall move
     * @throws Exception if the figure would leave the field
     */
    public void placeOracle(int steps) throws Exception{
        game.moveOracle(steps);
        if (FigureFunction.getFigureAmountOnField(game.getOracle(), figures) > 0){
            victoryPoints+=5;
        }
    }

    /**
     * Allows to place a card on the discarding pile.
     * Removes the card from hand and sets last trashed card.
     * The card gets sorted to the correct color discarding pile.
     * ignores if the color does not exist
     * @param card the card that gets discarded.
     */
    public void putCardOnDiscardingPile(Card card) {
        try {
            hand.remove(card);
            game.getDiscardPile(card.getColor()).add(card);
            lastTrashed = card;
        } catch (Exception e){
            //do nothing
        }
    }

    /**
     * The points from the collected tokens are calculated
     * @return the calculated points to be gained from the token
     */
    public int calcTokenPoints(){
        int stones = tokens[0];
        int mirrors = tokens[1];

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
            if(game.getFields()[f.getPos()].getToken() instanceof Goblin){
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


    /**
     * method increases the players amount of collected wishing stones by one
     */
    public void increaseStoneAmount(){
        tokens[0]++;
    }
    /**
     * method increases the players amount of collected mirrors by one
     */
    public void increaseMirrorAmount(){
        tokens[1]++;
    }
    public int[] getTokens() {return tokens.clone();}
    public DirectionDiscardPile[] getPlayedCards() {return playedCards.clone();}
    public Figure getLastMovedFigure(){return lastMovedFigure;}
    public List<Figure> getFigures() {return figures;}
    public String getName() {
        return name;
    }
    public List<Card> getHand() {return hand;}
    public Game getGame() {return game;}
    public void setLastTrashed(Card lastTrashed) {
        this.lastTrashed = lastTrashed;
    }
    public Card getLastTrashed() {return lastTrashed;}
    public int getVictoryPoints() {
        return victoryPoints;
    }
    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
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