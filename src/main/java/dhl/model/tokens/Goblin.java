package dhl.model.tokens;

import dhl.model.Game;
import dhl.model.Player;

public class Goblin implements Token {

    public static final char SYMBOL = 'G';
    private int fieldIndex;
    private boolean playersChoice;
    private int chosenPos;
    private char pile;
    private String card;

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
                try {
                    switch (pile){
                        case ('b') :
                            game.getDiscardingPileBlue().add(player.getPlayedCardsBlue().getTop());
                            break;
                        case ('g') :
                            game.getDiscardingPileGreen().add(player.getPlayedCardsGreen().getTop());
                            break;
                        case ('o') :
                            game.getDiscardingPileOrange().add(player.getPlayedCardsOrange().getTop());
                            break;
                        case ('p') :
                            game.getDiscardingPilePurple().add(player.getPlayedCardsPurple().getTop());
                            break;
                        case ('r') :
                            game.getDiscardingPileRed().add(player.getPlayedCardsRed().getTop());
                            break;
                        case ('h') :
                            game.putCardOnDiscardingPile(player.getCardFromHand(card), player);
                            break;
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
    }

    @Override
    public void setChosenPos(int chosenPos) {
        this.chosenPos = chosenPos;
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
    public void setCardChoice(String card) {
        this.card = card;
    }

    @Override
    public char getSymbol() {
        return SYMBOL;
    }

}
