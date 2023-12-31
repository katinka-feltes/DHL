package dhl.model;

import java.util.Comparator;
import java.util.List;

/**
 * this class takes over some card and pile functions, like sorting and validating.
 */
public class CardFunction {

    /**
     * sorts the hand of the player by number and color
     *
     * @param hand the list with cars to sort
     * @return hand sorted by number and color as an ArrayList
     */
    public static List<Card> sortHand(List<Card> hand) {
        hand.sort(Comparator.comparing(Card::getNumber));
        hand.sort(Comparator.comparing(Card::getColor));
        return hand;
    }

    /**
     * Get a card from hand
     *
     * @param cardAsString the card to get from the hand as a string
     * @param hand the list of cards to get card from
     * @return the Card
     * @throws Exception if card is not in Hand
     */
    public static Card getCardFromHand(String cardAsString, List<Card> hand) throws Exception {

        char[] cardValue = cardAsString.toCharArray();
        for (Card handCard : hand) {
            // if number and color match current card from hand (if the length of value is 3, the number always is 10)
            if (handCard.getColor() == cardValue[0] &&
                    (cardValue.length == 2 && handCard.getNumber() == Character.getNumericValue(cardValue[1]) ||
                    cardValue.length == 3 && handCard.getNumber() == 10)) {
                return handCard;
            }
        }
        throw new Exception("Card is not in Hand.");
    }

    /**
     * checks if card fits to player's played cards piles
     * @param card one card from the player's hand
     * @param correctPile the player's played cards (with color of the card)
     * @return true if card fits to appropriately colored pile
     */
    public static boolean cardFitsToPlayersPiles(Card card, DirectionDiscardPile correctPile) {
        if (correctPile != null) {
            return correctPile.cardFitsToPile(card);
        } else {
            return false;
        }
    }

    /**
     * calculates how many cards in player's hand are of given color
     * @param cardSet the set of card to count
     * @param color color the cards should have
     * @return amount of cards of given color
     */
    public static int amountOfColoredCards(List<Card> cardSet, char color) {
        int amount = 0;
        for(Card card: cardSet) {
            if(card.getColor() == color) {
                amount++;
            }
        }
        return amount;
    }
}