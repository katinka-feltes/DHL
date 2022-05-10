package dhl.model.tokens;

import dhl.model.Game;
import dhl.model.Player;

public class SpiderWeb implements Token {
    // Kleeblatt


    public static final char SYMBOL = '\u25A9';

    @Override
    public boolean isCollectable() {
        return false;
    }

    @Override
    public String getName() {
        String[] temp = this.getClass().toString().split("\\.");
        return temp[temp.length-1];
    }

    @Override
    public void action( Player player){
       player.getLastMovedFigure().move(Game.FIELDS[player.getLastMovedFigure().getPos()].getColor());
    }

    @Override
    public char getSymbol() {
        return SYMBOL;
    }

}
