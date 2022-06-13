package dhl.model.tokens;

import dhl.model.Player;

/**
 * This Class represents a Mirror Token. If you find a Mirror you collect it and it doubles your Wishing-Stone points.
 */
public class Mirror implements Token {

    public static final char SYMBOL = 'M';

    /**
     * says if the token is collectable
     * @return true if the token will be collected, false otherwise
     */
    @Override
    public boolean isCollectable() {
        return true;
    }

    @Override
    public String getName() {
        String[] temp = this.getClass().toString().split("\\.");
        return temp[temp.length-1];
    }

    /**
     * The found Mirror gets added to the players token list.
     * @param player the player that will execute the action
     */
    @Override
    public void action(Player player) {
        player.increaseMirrorAmount();
    }
    @Override
    public char getSymbol() {
        return SYMBOL;
    }
}
