package dhl.model.tokens;

import dhl.Constants;
import dhl.model.Player;

/**
 * This Class represents a Spiral Token. If you collect it you are allowed to move back as far as you want just not thefield you just came from.
 */
public class Spiral implements Token {

        public static final char SYMBOL = '\u058E';
        private int chosenPos;

        /**
         * says if the token is collectable
         * @return true if the token will be collected, false otherwise
         */
        @Override
        public boolean isCollectable() {
                return Constants.singleUseToken;
        }

        @Override
        public String getName() {
                String[] temp = this.getClass().toString().split("\\.");
                return temp[temp.length-1];
        }

        /**
         * The last moved figure can be moved backwards to the chosen Position.
         * @param player the player that will execute the action
         */
        @Override
        public void action( Player player) {
                if (chosenPos != player.getLastMovedFigure().getLatestPos()){
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
