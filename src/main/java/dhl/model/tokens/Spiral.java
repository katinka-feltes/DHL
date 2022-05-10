package dhl.model.tokens;

import dhl.model.Player;

public class Spiral implements Token {

        public static final char SYMBOL = '\u058E';
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
        public void action( Player player) {
                        if (player.getLastMovedFigure().getPos() != player.getLastMovedFigure().getLatestPos()){
                                player.getLastMovedFigure().setPos(chosenPos);
                        }
        }

        @Override
        public char getSymbol() {
                return SYMBOL;
        }


        public void setChosenPos(int chosenPos) {
                this.chosenPos = chosenPos;
        }

}
