package dhl.model.tokens;

import dhl.model.Player;

/**
 * This Class represents a Skullpoint Token. If you collect it you get victory points depending on the Skullpoint you found.
 */
public class Skullpoint implements Token{
    // ☠ is the symbol of the token
    public static final char SYMBOL = '\u2620';

    private final int points;

    /**
     * Creates a Skullpoint
     * @param points the Points the Skullpoint will have
     */
    public Skullpoint(int points){
        this.points = points;
    }

    /**
     * says if the token is collectable
     * @return true if the token will be collected, false otherwise
     */
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
     * @param player the current player
     */
    @Override
    public void action(Player player) {
        player.setVictoryPoints(player.getVictoryPoints() + this.points);
    }


    @Override
    public char getSymbol() {
        return SYMBOL;
    }
}
