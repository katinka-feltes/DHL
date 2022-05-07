package dhl.model.tokens;

import dhl.model.Game;
import dhl.model.Player;

public class SpiderWeb implements Token {

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
    public void action(Game game, Player player) {

    }

    @Override
    public void setFieldIndex(int fieldIndex) {


    }

    @Override
    public int getFieldIndex() {
        return 0;
    }

    @Override
    public char getSymbol() {
        return SYMBOL;
    }

}
