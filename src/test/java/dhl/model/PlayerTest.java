package dhl.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    Game game = new Game(2);
    Player player = game.getPlayers().get(0);

    @Test
    void drawCardsUpToEight() throws Exception {
        assertEquals(8, player.getHand().size());
        player.addCardToPlayedCards(player.getHand().get(0));
        assertEquals(7, player.getHand().size());
        player.drawCardsUpToEight(game.getDrawingPile());
        assertEquals(8, player.getHand().size());
        player.getHand().removeAll(player.getHand());
        assertEquals(0, player.getHand().size());
        player.drawCardsUpToEight(game.getDrawingPile());
        assertEquals(8, player.getHand().size());
    }

    @Test
    void addCardToPlayedCards() throws Exception {
        player.getHand().removeAll(player.getHand());
        assertEquals(0, player.getHand().size());
        player.getHand().add(new Card(1, 'r'));
        player.getHand().add(new Card(1, 'g'));
        player.getHand().add(new Card(1, 'b'));
        player.getHand().add(new Card(1, 'p'));
        player.getHand().add(new Card(1, 'o'));
        assertEquals(5, player.getHand().size());
        player.drawCardsUpToEight(game.getDrawingPile());
        assertEquals(8, player.getHand().size());
        int size_r = 0;
        int size_g = 0;
        int size_b = 0;
        int size_p = 0;
        int size_o = 0;
        ArrayList<Card> sorted_hand = new ArrayList<>(player.getSortedHand());
        for (Card card : sorted_hand) {
            player.addCardToPlayedCards(card);
            switch (card.getColor()) {
                case ('r'):
                    size_r++;
                    assertEquals(size_r, player.getPlayedCardsRed().pile.size());
                    break;
                case ('g'):
                    size_g++;
                    assertEquals(size_g, player.getPlayedCardsGreen().pile.size());
                    break;
                case ('b'):
                    size_b++;
                    assertEquals(size_b, player.getPlayedCardsBlue().pile.size());
                    break;
                case ('p'):
                    size_p++;
                    assertEquals(size_p, player.getPlayedCardsPurple().pile.size());
                    break;
                case ('o'):
                    size_o++;
                    assertEquals(size_o, player.getPlayedCardsOrange().pile.size());
                    break;
            }
        }
    }

    @Test
    void placeFigure() {
    }

    @Test
    void getFigureOnField() {
    }

    @Test
    void getCardFromHand() {

    }

    @Test
    void getFigureAmountOnField() {
    }

    @Test
    void getFigures() {
    }

    @Test
    void getName() {
        assertEquals("P1", player.getName());
    }

    @Test
    void setName() {
        player.setName("Test");
        assertEquals("Test", player.getName());
    }

    @Test
    void getSymbol() {
    }

    @Test
    void setSymbol() {
    }

    @Test
    void getPlayedCardsRed() {
    }

    @Test
    void getPlayedCardsBlue() {
    }

    @Test
    void getPlayedCardsGreen() {
    }

    @Test
    void getPlayedCardsPurple() {
    }

    @Test
    void getPlayedCardsOrange() {
    }

    @Test
    void getHand() {
        player.drawCardsUpToEight(game.getDrawingPile());
        assertEquals(8, player.getHand().size());
        for (Card card : player.getHand()) {
            assertEquals(Card.class, card.getClass());
        }
    }

    @Test
    void getVictoryPoints() {
        assertEquals(0, player.getVictoryPoints());
    }

    @Test
    void setVictoryPoints() {
        player.setVictoryPoints(10);
        assertEquals(10, player.getVictoryPoints());
    }

    @Test
    void isGoblinSpecialPlayed() {
    }
}