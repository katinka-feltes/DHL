package dhl.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerTest {

    Game game;
    Player player;

    @BeforeEach
    void setup() {
        game = new Game(2);
        player = game.getPlayers().get(0);
    }

    @Test
    void drawCardsUpToEight() throws Exception {
        assertEquals(0, player.getHand().size());
        player.drawCardsUpToEight(game.getDrawingPile());
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
    void placeFigure() throws Exception {
        player.placeFigure('r', 1);
        assertEquals(3, player.getFigureByPos(1).getPos());
        player.placeFigure('g', 1);
        assertEquals(4, player.getFigureByPos(1).getPos());

    }

    @Test
    void getFigureOnField() throws Exception {
        player.getFigures()[0].setPos(4);
        assertEquals(player.getFigureOnField(4), player.getFigures()[0]);
        player.getFigures()[1].setPos(5);
        player.getFigures()[2].setPos(6);
        assertEquals(player.getFigureOnField(5), player.getFigures()[1]);
        assertEquals(player.getFigureOnField(6), player.getFigures()[2]);
        assertThrows(Exception.class, () -> player.getFigureOnField(10));
    }

    @Test
    void getCardFromHand() throws Exception {
        player.getHand().add(new Card(2, 'r'));
        assertEquals(player.getCardFromHand("r2"), player.getHand().get(0));
        player.getHand().remove(0);
        player.getHand().add(new Card(10, 'g'));
        assertEquals(player.getCardFromHand("g10"), player.getHand().get(0));


    }

    @Test
    void getFigureAmountOnField() {
        player.getFigures()[0].setPos(5);
        player.getFigures()[1].setPos(7);
        player.getFigures()[2].setPos(8);
        assertEquals(1, player.getFigureAmountOnField(5));
        player.getFigures()[0].setPos(7);
        assertEquals(2, player.getFigureAmountOnField(7));
        player.getFigures()[2].setPos(7);
        assertEquals(3, player.getFigureAmountOnField(7));
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

    @Test
    void getFigureByPos() {
        player.getFigures()[0].setPos(3);
        player.getFigures()[1].setPos(5);
        player.getFigures()[2].setPos(6);
        assertEquals(player.getFigureByPos(3), player.getFigures()[0]);
        assertEquals(player.getFigureByPos(2), player.getFigures()[1]);
        assertEquals(player.getFigureByPos(1), player.getFigures()[2]);
        player.getFigures()[0].setPos(5);
        assertEquals(player.getFigureByPos(3), player.getFigures()[0]);
        assertEquals(player.getFigureByPos(2), player.getFigures()[1]);
    }

    @Test
    //TODO: function has some weird dynamics
    void drawFromDiscardingPile() throws Exception {
        assertThrows(Exception.class, () -> player.drawFromDiscardingPile(game.getDiscardingPileBlue(), null), "You fool: this pile is empty!");
        game.getDiscardingPileBlue().getPile().add(new Card(4, 'r'));
        assertThrows(Exception.class, () -> player.drawFromDiscardingPile(game.getDiscardingPileBlue(), game.getDiscardingPileBlue().getPile().get(0)), "You can't draw a card you just trashed!");
        player.drawFromDiscardingPile(game.getDiscardingPileBlue(), null);
        assertEquals('r', player.getHand().get(0).getColor());
        assertEquals(4, player.getHand().get(0).getNumber());
    }
}