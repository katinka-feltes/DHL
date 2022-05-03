package dhl.model.tokens;

import dhl.model.Game;
import dhl.model.Player;

public class Spiral implements Token {

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
                String question = "You can move this figure backwards as far as you want. Except the Field where you come from." +
                        "Do you want to proceed with your action? (yes/no)";

        }

}
