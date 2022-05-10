package dhl.model.tokens;

import dhl.model.Player;

public class WishingStone implements Token{

    public static final char SYMBOL = 'W';
    public static final int[] VALUE = {-4, -3, 2, 3, 6, 10};

    @Override
    public boolean isCollectable() {
        return true;
    }

    @Override
    public String getName() {
        String[] temp = this.getClass().toString().split("\\.");
        return temp[temp.length-1];
    }

    @Override
    public void action( Player player) {
        player.getTokens().add(this);
    }

    @Override
    public char getSymbol() {
        return SYMBOL;
    }

}
