package dhl.model.tokens;

import dhl.model.Game;
import dhl.model.Player;

public class Spiral implements Token {

        private int fieldIndex;
        private boolean playersChoice;
        //public static final char SYMBOL =

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
                String question = "You can move this figure backwards as far as you want. Except the field where you come from." +
                        "Do you want to proceed with your action? (yes/no)";
                if (playersChoice) {
                        try {
                                player.getFigureOnField(fieldIndex).setPos(player.getFigureOnField(fieldIndex).getLatestPos());
                        } catch (Exception e) {
                                e.getMessage();
                        }
                }
        }

        @Override
        public void setFieldIndex(int fieldIndex) {
                this.fieldIndex = fieldIndex;
        }

        @Override
        public int getFieldIndex() {
                return fieldIndex;
        }

        public void setPlayersChoice(boolean playersChoice){
                this.playersChoice = playersChoice;
        }


}
