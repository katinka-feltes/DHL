package dhl.model.tokens;

import dhl.model.Game;
import dhl.model.Player;

public class Mirror implements Token {

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
    public void action(Game game, Player player) {

    }
}
