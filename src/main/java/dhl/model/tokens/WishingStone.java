package dhl.model.tokens;

import dhl.model.Player;

public class WishingStone implements Token{

    private static final char SYMBOL = 'W';

    public static int getValue(int amountStones){
         int[] value = {-4, -3, 2, 3, 6, 10};
        return value[amountStones];
    }

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
