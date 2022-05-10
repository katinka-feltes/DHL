package dhl.model.tokens;

import dhl.model.Card;
import dhl.model.Game;
import dhl.model.Player;

public class WishingStone implements Token{

    public static final char SYMBOL = 'W';

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

    @Override
    public char getSymbol() {
        return SYMBOL;
    }

    @Override
    public void setChosenPos(int chosenPos) {


    }

    @Override
    public char getPileChoice() {
        return 0;
    }

    @Override
    public void setPileChoice(char pileChoice) {

    }

    @Override
    public void setCardChoice(Card card) {

    }

}
