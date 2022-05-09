package dhl.model.tokens;

import dhl.model.Game;
import dhl.model.Player;

public class Spiral implements Token {

        public static final char SYMBOL = '\u058E';
        private int fieldIndex;
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
        public void action(Game game, Player player) {
                        if (player.getLastMovedFigure().getPos() != player.getLastMovedFigure().getLatestPos()){
                                player.getLastMovedFigure().setPos(chosenPos);
                        }
        }

        @Override
        public char getSymbol() {
                return SYMBOL;
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

}
