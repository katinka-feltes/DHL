package dhl.model.tokens;

import dhl.model.Player;

public class WishingStone implements Token{

    private static final char SYMBOL = 'W';

    public static int getValue(int amountStones){
         int[] value = {-4, -3, 2, 3, 6, 10};
        return value[amountStones];
    }

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
     * The found Wishing Stone gets added to the players token list.
     * @param player the player that will execute the action
     */
    @Override
    public void action( Player player) {
        player.getTokens().add(this);
    }

    @Override
    public char getSymbol() {
        return SYMBOL;
    }

}
