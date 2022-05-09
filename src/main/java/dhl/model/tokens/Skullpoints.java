package dhl.model.tokens;

import dhl.model.Game;
import dhl.model.Player;

public class Skullpoints implements Token{
    // ☠ is the symbol of the token
    public static final char SYMBOL = '\u2620';

    private int points;

    public Skullpoints(int points){
        this.points = points;
    }

    @Override
    public boolean isCollectable() {
        return false;
    }

    @Override
    public String getName() {
        String[] temp = this.getClass().toString().split("\\.");
        return temp[temp.length-1];
    }

    /**the skullpoints are added to the current players' victory points
     * the counting stones position gets updated
     *
     * @param game the current game status !!!!not needed!!!!
     * @param player the current player
     */
    @Override
    public void action(Game game, Player player) {
        player.setVictoryPoints(player.getVictoryPoints() + points);
    }


    @Override
    public char getSymbol() {
        return SYMBOL;
    }

    @Override
    public void setChosenPos(int chosenPos) {

    }

    @Override
    public char getPileChoice() {
        return 0;
    }

    @Override
    public void setPileChoice(char pileChoice) {

    }

    @Override
    public void setCardChoice(String card) {

    }

    public int getPoints(){
        return points;
    }
}
