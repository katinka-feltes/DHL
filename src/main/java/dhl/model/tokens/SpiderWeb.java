package dhl.model.tokens;

import dhl.model.Game;
import dhl.model.Player;

public class SpiderWeb implements Token {
    // Kleeblatt

    private char color;

    public static final char SYMBOL = '\u25A9';
    private char fieldColor;
    private int chosenPos;

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
    public void action(Game game, Player player) throws Exception{
        fieldColor = player.getLastMovedFigure().getField(player.getLastMovedFigure().getPos()).getColor();

            if (player.getLastMovedFigure().getField(chosenPos).getColor() == fieldColor && chosenPos <= 39 && chosenPos >= 0) {
                player.getLastMovedFigure().setPos(chosenPos);
            } else {
                throw new Exception("The color does not fit");
            }
    }

    @Override
    public void setChosenPos(int chosenPos) {
        chosenPos = chosenPos;
    }

    @Override
    public char getPileChoice() {
        return 0;
    }

    @Override
    public void setPileChoice(char pileChoice) {

    }

    @Override
    public void setCardChoice(String card) {

    }

    @Override
    public char getSymbol() {
        return SYMBOL;
    }

}
