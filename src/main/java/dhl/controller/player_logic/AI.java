package dhl.controller.player_logic;

import dhl.model.Card;
import dhl.model.CardFunction;
import dhl.model.DirectionDiscardPile;

import java.util.List;

/**
 * The logic for the AI agent
 */
public class AI implements PlayerLogic {

    @Override
    public boolean choose(String question) {
        if (question.endsWith("Are you ready to play?")) {
            return true;
        } else if (question.equals("Are you done with your turn?")) {
            return true;
        } else if (question.startsWith("Do you want to play a card?")) {
            return true;
        } else if (question.startsWith("Do you want to draw your card from one of the discarding piles?")) {
            return false;
        } else if (question.endsWith("Do you want to proceed with your action?")) {
            return false;
        } else if (question.startsWith("Do you want to trash one from your hand?")) {
            return true;
        } else if (question.startsWith("Do you want to play your goblin-special?")) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public Card chooseCard(String question, List<Card> hand, DirectionDiscardPile[] playedCards) {
        if (question.equals("What card do you want to play?")) {
            return bestCardOption(hand, playedCards);
        } else if (question.equals("What card do you want to trash?")) {
            return hand.get(0);
        } else {
            return hand.get(0);
        }
    }

    @Override
    public int chooseFigure(int start, int end, String question) {
        //which figure he wants to move
        return 1;
    }

    /**
     * checks which playable hand card is the best option according to the AI's played cards
     * the smaller the difference between last played card and (playable) hand card, the better
     * if the pile is empty, the card is better the higher/lower it is
     * @param hand the player's current hand
     * @param playedCards all the player's played cards
     * @return the hand card most fitting to the played cards
     */
    public Card bestCardOption(List<Card> hand, DirectionDiscardPile[] playedCards) {
        int smallestDifference = 11;
        Card bestCard = null;
        for(Card card: hand) {
            DirectionDiscardPile pile = getCorrectPile(card, playedCards);
            if(CardFunction.cardFitsToPlayersPiles(card, pile)) {
                if(pile.isEmpty()) {
                    if(card.getNumber() > 5) {
                        smallestDifference = 10-card.getNumber();
                        bestCard = card;
                    } else if(card.getNumber() < 5) {
                        smallestDifference = card.getNumber();
                        bestCard = card;
                    } else {
                        smallestDifference = 5;
                        bestCard = card;
                    }
                } else {
                    int diff = Math.abs(pile.getTop().getNumber() - card.getNumber());
                    if(diff < smallestDifference) {
                        smallestDifference = diff;
                        bestCard = card;
                    }
                }
            }
        }
        return bestCard;
    }

    /**
     * to get the correctly colored played cards pile for a card
     * @param card one of the player's hand cards
     * @param playedCards all the player's played cards
     * @return the correctly colored played cards pile
     */
    public DirectionDiscardPile getCorrectPile(Card card, DirectionDiscardPile[] playedCards) {
        for (DirectionDiscardPile pile : playedCards) {
            if (pile.getColor() == card.getColor()) {
                return pile;
            }
        }
        return null;
    }
}