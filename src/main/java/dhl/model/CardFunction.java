package dhl.model;

import java.util.ArrayList;
import java.util.List;

public class CardFunction {

    /**
     * sorts the hand of the player by number and color
     * @return hand sorted by number and color as an ArrayList
     */
    public static List<Card> sortHand(List<Card> hand) {
        List<Card> blueCards = new ArrayList<>();
        List<Card> greenCards = new ArrayList<>();
        List<Card> orangeCards = new ArrayList<>();
        List<Card> purpleCards = new ArrayList<>();
        List<Card> redCards = new ArrayList<>();
        List<Card> sortedHand = new ArrayList<>();

        for(Card card: hand) {
            if(card.getColor() == 'b') {
                blueCards.add(card);
            } else if(card.getColor() == 'g') {
                greenCards.add(card);
            } else if(card.getColor() == 'o') {
                orangeCards.add(card);
            } else if(card.getColor() == 'p') {
                purpleCards.add(card);
            } else if(card.getColor() == 'r') {
                redCards.add(card);
            }
        }
        sortedHand.addAll(sortColorList((ArrayList<Card>) blueCards));
        sortedHand.addAll(sortColorList((ArrayList<Card>) greenCards));
        sortedHand.addAll(sortColorList((ArrayList<Card>) orangeCards));
        sortedHand.addAll(sortColorList((ArrayList<Card>) purpleCards));
        sortedHand.addAll(sortColorList((ArrayList<Card>) redCards));

        return sortedHand;
    }


    /**
     * sorts given ArrayList by number
     * @param colorList list of cards already sorted by color
     * @return cards of a specific color sorted by number as an ArrayList
     */
    public static List<Card> sortColorList(ArrayList<Card> colorList) {
        List<Card> sortedColorList = new ArrayList<>();
        for(int i=0; i<=10; i++) {
            for(Card card: colorList) {
                if (card.getNumber() == i) {
                    sortedColorList.add(card);
                }
            }
        }
        return sortedColorList;
    }

    /**
     * Get a card from hand
     * @param cardAsString the card to get from the hand as a string
     * @return the Card
     * @throws Exception if card is not in Hand
     */
    public static Card getCardFromHand(String cardAsString, List<Card> hand) throws Exception {

        char[] cardValue = cardAsString.toCharArray();
        for (Card handCard : hand) {
            // if number and color match current card from hand (if the length of value is 3, the number always is 10)
            if (cardValue.length == 2 && handCard.getColor() == cardValue[0] && handCard.getNumber() == Character.getNumericValue(cardValue[1]) ||
                    cardValue.length == 3 && handCard.getNumber() == 10) {
                return handCard;
            }
        }
        throw new Exception("Card is not in Hand.");
    }

    /**
     * checks if card fits to any of the player's piles
     * @param card one card from the player's hand
     * @return true if card fits to appropriately colored pile
     */
    public static boolean cardFitsToAnyPile(Card card, DirectionDiscardPile playedCards){
        if(playedCards != null){
            return playedCards.cardFitsToPile(card);
        }
        else {
            return false;
        }
    }
}
