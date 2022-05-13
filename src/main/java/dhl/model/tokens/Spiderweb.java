package dhl.model.tokens;

import dhl.model.Figure;
import dhl.model.Game;
import dhl.model.Player;

public class Spiderweb implements Token {
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
        Figure lastMoved = player.getLastMovedFigure();
        player.placeFigure(Game.FIELDS[lastMoved.getPos()].getColor(), lastMoved);
    }

    @Override
    public char getSymbol() {
        return SYMBOL;
    }

}
