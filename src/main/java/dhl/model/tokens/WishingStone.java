package dhl.model.tokens;

import dhl.model.Game;
import dhl.model.Player;

public class WishingStone implements Token{

    //public static final char SYMBOL =

    @Override
    public boolean isCollectable() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void action(Game game, Player player) {

    }

    @Override
    public void setFieldIndex(int fieldIndex) {

    }

    @Override
    public int getFieldIndex() {
        return 0;
    }

}
