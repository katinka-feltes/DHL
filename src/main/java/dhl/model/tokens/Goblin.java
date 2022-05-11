package dhl.model.tokens;

import dhl.model.Card;
import dhl.model.Game;
import dhl.model.Player;

public class Goblin implements Token {

    public static final char SYMBOL = 'G';
    private char pile;
    private Card card;

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
    public void action(Player player) {
        Game game = player.getGame();
        // check if card is not null
        try {
            switch (pile) {
                case ('b'):
                    game.getDiscardingPileBlue().add(player.getPlayedCardsBlue().getAndRemoveTop());
                    player.setLastTrashed(game.getDiscardingPileGreen().getTop());
                    break;
                case ('g'):
                    game.getDiscardingPileGreen().add(player.getPlayedCardsGreen().getAndRemoveTop());
                    player.setLastTrashed(game.getDiscardingPileGreen().getTop());
                    break;
                case ('o'):
                    game.getDiscardingPileOrange().add(player.getPlayedCardsOrange().getAndRemoveTop());
                    player.setLastTrashed(game.getDiscardingPileOrange().getTop());
                    break;
                case ('p'):
                    game.getDiscardingPilePurple().add(player.getPlayedCardsPurple().getAndRemoveTop());
                    player.setLastTrashed(game.getDiscardingPilePurple().getTop());
                    break;
                case ('r'):
                    game.getDiscardingPileRed().add(player.getPlayedCardsRed().getAndRemoveTop());
                    player.setLastTrashed(game.getDiscardingPileRed().getTop());
                    break;
                case ('h'):
                    player.putCardOnDiscardingPile(card);
                    player.setLastTrashed(card);
                    break;
                default:
                    break;
            }
        } catch(Exception ignored) {
            // do nothing
        }
    }

    public char getPileChoice() {
        return pile;
    }

    public void setPileChoice(char pileChoice) {
        this.pile = pileChoice;
    }

    public void setCardChoice(Card card) {
        this.card = card;
    }

    @Override
    public char getSymbol() {
        return SYMBOL;
    }

}
