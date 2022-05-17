package dhl.model;

import dhl.model.tokens.Goblin;
import dhl.model.tokens.Mirror;
import dhl.model.tokens.WishingStone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    Game game;
    Player player;

    @BeforeEach
    public void setup() {
        game = new Game(new String[]{"Player 1", "Player 2"});
        player = game.getPlayers().get(0);
    }

    @Test
    public void drawCardsUpToEight() throws Exception {
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
    public void addCardToPlayedCards() throws Exception {
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
    public void placeFigure() {
        player.placeFigure('r', player.getFigureByPos(1));
        assertEquals(3, player.getFigureByPos(1).getPos());
        player.placeFigure('g', player.getFigureByPos(1));
        assertEquals(4, player.getFigureByPos(1).getPos());
    }

    @Test
    public void getCardFromHand() throws Exception {
        player.getHand().add(new Card(2, 'r'));
        assertEquals(player.getCardFromHand("r2"), player.getHand().get(0));
        player.getHand().remove(0);
        player.getHand().add(new Card(10, 'g'));
        assertEquals(player.getCardFromHand("g10"), player.getHand().get(0));
        assertThrows(Exception.class, () -> player.getCardFromHand("p2"), "Card is not in Hand.");
    }

    @Test
    public void getFigureAmountOnField() {
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
    public void getName() {
        assertEquals("Player 1", player.getName());
    }

    @Test
    public void setName() {
        player.setName("Test");
        assertEquals("Test", player.getName());
    }

    @Test
    public void getHand() {
        player.drawCardsUpToEight(game.getDrawingPile());
        assertEquals(8, player.getHand().size());
        for (Card card : player.getHand()) {
            assertEquals(Card.class, card.getClass());
        }
    }

    @Test
    public void getVictoryPoints() {
        assertEquals(0, player.getVictoryPoints());
    }

    @Test
    public void setVictoryPoints() {
        player.setVictoryPoints(10);
        assertEquals(10, player.getVictoryPoints());
    }

    @Test
    public void getFigureByPos() {
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
    public void getLastMovedFigure() {
        player.placeFigure('r', player.getFigureByPos(1));
        assertEquals(player.getFigureByPos(1), player.getLastMovedFigure());
    }

    @Test
    public void drawFromDiscardingPile() throws Exception {
        assertThrows(Exception.class, () -> player.drawFromDiscardingPile(game.getDiscardingPileBlue()), "You fool: this pile is empty!");
        game.getDiscardingPileBlue().getPile().add(new Card(4, 'b'));
        player.setLastTrashed(game.getDiscardingPileBlue().getPile().get(0));
        assertThrows(Exception.class, () -> player.drawFromDiscardingPile(game.getDiscardingPileBlue()), "You can't draw a card you just trashed!");
        game.getDiscardingPileRed().add(new Card(2, 'r'));
        player.drawFromDiscardingPile(game.getDiscardingPileRed());
        assertEquals('r', player.getHand().get(0).getColor());
        assertEquals(2, player.getHand().get(0).getNumber());
    }

    @Test
    void drawFromDrawingPile() {
        player.drawFromDrawingPile(game.getDrawingPile());
        assertEquals(1, player.getHand().size());
    }

    @Test
    public void canPlay() {
        player.getHand().add(new Card(2, 'r'));
        assertTrue(player.canPlay());
        player.getHand().remove(0);
        assertFalse(player.canPlay());
    }

    @Test
    public void allFiguresGoblin(){
        assertFalse(player.allFiguresGoblin());
        Game.FIELDS[0].setToken(new Goblin());
        assertTrue(player.allFiguresGoblin());
        Game.FIELDS[5].setToken(new Goblin());
        player.getFigures()[0].setPos(5);
        assertTrue(player.allFiguresGoblin());
        Game.FIELDS[10].setToken(new Goblin());
        player.getFigures()[1].setPos(10);
        assertTrue(player.allFiguresGoblin());
        Game.FIELDS[10].setToken(null);
        assertFalse(player.allFiguresGoblin());
    }

    @Test
    public void goblinSpecialPoints(){
        Game.FIELDS[0].setToken(new Goblin());
        assertEquals(5 , player.goblinSpecialPoints());
        Game.FIELDS[5].setToken(new Goblin());
        player.getFigures()[0].setPos(5);
        assertEquals(10 , player.goblinSpecialPoints());
        Game.FIELDS[10].setToken(new Goblin());
        player.getFigures()[1].setPos(10);
        assertEquals(15 , player.goblinSpecialPoints());
        Game.FIELDS[10].setToken(null);
        assertEquals(0 , player.goblinSpecialPoints());
    }
    @Test
    public void playGoblinSpecial(){
        assertFalse(player.isGoblinSpecialPlayed());
        Game.FIELDS[0].setToken(new Goblin());
        Game.FIELDS[5].setToken(new Goblin());
        player.getFigures()[0].setPos(5);
        player.playGoblinSpecial();
        assertEquals(10 , player.getVictoryPoints());
        assertTrue(player.isGoblinSpecialPlayed());

    }

    @Test
    public void cardFitsToAnyPile() {
        player.getHand().add(new Card(0, 't'));
        assertFalse(player.canPlay());
        player.getHand().add(new Card(1, 'g'));
        assertTrue(player.canPlay());
        player.getHand().clear();
        player.getHand().add(new Card(1, 'b'));
        assertTrue(player.canPlay());
        player.getHand().clear();
        player.getHand().add(new Card(1, 'p'));
        assertTrue(player.canPlay());
        player.getHand().clear();
        player.getHand().add(new Card(1, 'o'));
        assertTrue(player.canPlay());
        player.getHand().clear();
    }

    @Test
    public void calcTokenPoints() {
        player.getTokens().add(new WishingStone());
        player.getTokens().add(new Mirror());
        player.calcTokenPoints();
        assertEquals(-6, player.getVictoryPoints());
        player.setVictoryPoints(0);
        player.getTokens().add(new Mirror());
        player.calcTokenPoints();
        assertEquals(-12, player.getVictoryPoints());
    }
}