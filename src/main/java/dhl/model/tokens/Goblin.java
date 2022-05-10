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
    public void action(Game game, Player player) {

                    switch (pile){
                        case ('b') :
                            game.getDiscardingPileBlue().getPile().add(player.getPlayedCardsBlue().getTop());
                            break;
                        case ('g') :
                            game.getDiscardingPileGreen().getPile().add(player.getPlayedCardsGreen().getTop());
                            break;
                        case ('o') :
                            game.getDiscardingPileOrange().getPile().add(player.getPlayedCardsOrange().getTop());
                            break;
                        case ('p') :
                            game.getDiscardingPilePurple().getPile().add(player.getPlayedCardsPurple().getTop());
                            break;
                        case ('r') :
                            game.getDiscardingPileRed().getPile().add(player.getPlayedCardsRed().getTop());
                            break;
                        case ('h') :
                            try{ // just to clear the errors !!not the final implementation!!
                                player.putCardOnDiscardingPile(card);
                            } catch(Exception e) {
                                e.getMessage();
                            }

                            break;
                    }
    }

    @Override
    public char getPileChoice() {
        return pile;
    }

    @Override
    public void setPileChoice(char pileChoice) {
        this.pile = pileChoice;
    }

    @Override
    public void setCardChoice(Card card) {
        this.card = card;
    }

    @Override
    public char getSymbol() {
        return SYMBOL;
    }

    @Override
    public void setChosenPos(int chosenPos) {

    }

}
