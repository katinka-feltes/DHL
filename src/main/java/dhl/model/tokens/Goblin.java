package dhl.model.tokens;

import dhl.Constants;
import dhl.model.Card;
import dhl.model.Game;
import dhl.model.Player;

/**
 * This Class represents a Goblin Token. If you find a Goblin you are allowed to either remove a card from your hand or from one of your discarding piles.
 */
public class Goblin implements Token {

    public static final char SYMBOL = 'G';
    private char pile;
    private Card card;

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
     * Depending on the chosen color the player can discard a card from the directional discard pile or from his hand.
     * If a card from the players hand gets discarded the color has to be chosen again.
     * @param player the player that will execute the action
     */
    @Override
    public void action(Player player) {
        Game game = player.getGame();
        // check if card is not null
        try {
            if(pile == 'h') {
                player.putCardOnDiscardingPile(card);
                player.setLastTrashed(card);
            } else {
                Card trashCard = player.getPlayedCards(pile).getAndRemoveTop();
                game.getDiscardPile(pile).add(trashCard);
                player.setLastTrashed(game.getDiscardPile(pile).getTop());
            }
        } catch(Exception ignored) {
            // do nothing
        }
    }
    
    /**
     * returns the chosen pile
     * @return the chosen pile
     */
    public char getPileChoice() {
        return pile;
    }

    /**
     * sets the players pile choice
     * @param pileChoice the chosen pile
     */
    public void setPileChoice(char pileChoice) {
        this.pile = pileChoice;
    }

    /**
     * sets the players card choice
     * @param card the chosen card
     */
    public void setCardChoice(Card card) {
        this.card = card;
    }

    @Override
    public char getSymbol() {
        return SYMBOL;
    }

}
