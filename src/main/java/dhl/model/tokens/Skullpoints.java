package dhl.model.tokens;

import dhl.model.Player;

public class Skullpoints implements Token{
    // ☠ is the symbol of the token
    public static final char SYMBOL = '\u2620';

    private final int points;

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
     * @param player the current player
     */
    @Override
    public void action(Player player) {
        player.setVictoryPoints(player.getVictoryPoints() + points);
    }


    @Override
    public char getSymbol() {
        return SYMBOL;
    }
}
