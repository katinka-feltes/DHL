package dhl.model;

import dhl.model.tokens.Goblin;
import dhl.model.tokens.Mirror;
import dhl.model.tokens.WishingStone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests the class Player
 */
public class PlayerTest {

    Game game;
    Player player;

    @BeforeEach
    /**
     * creates a new game with 2 players
     */
    public void setup() {
        game = new Game(new String[]{"Player 1", "Player 2"});
        player = game.getPlayers().get(0);
    }

    @Test
    /**
     * tests if method placeFigure works
     */
    public void placeFigure() {
        Figure fig = FigureFunction.getFigureByPos(1, player.getFigures());
        player.placeFigure('r', fig);
        assertEquals(3, fig.getPos());
        player.placeFigure('g', fig);
        assertEquals(4, fig.getPos());
        fig = FigureFunction.getFigureByPos(3, player.getFigures());
        player.placeFigure('g', fig);
        assertEquals(4, fig.getPos());
    }

    @Test
    /**
     * tests if method getFigureAmountOnField from class FigureFunction works
     */
    public void getFigureAmountOnField() {
        player.getFigures().get(0).setPos(5);
        player.getFigures().get(1).setPos(7);
        player.getFigures().get(2).setPos(8);
        assertEquals(1, FigureFunction.getFigureAmountOnField(5, player.getFigures()));
        player.getFigures().get(0).setPos(7);
        assertEquals(2, FigureFunction.getFigureAmountOnField(7, player.getFigures()));
        player.getFigures().get(2).setPos(7);
        assertEquals(3, FigureFunction.getFigureAmountOnField(7, player.getFigures()));
    }

    @Test
    /**
     * tests if method getName works
     */
    public void getName() {
        assertEquals("Player 1", player.getName());
    }

    @Test
    /**
     * tests if method setName works
     */
    public void setName() {
        player.setName("Test");
        assertEquals("Test", player.getName());
    }

    @Test
    /**
     * tests if method getHand works
     */
    public void getHand() {
        game.createDecks();
        assertEquals(8, player.getHand().size());
        for (Card card : player.getHand()) {
            assertEquals(Card.class, card.getClass());
        }
    }

    @Test
    /**
     * tests if method getVictoryPoints works
     */
    public void getVictoryPoints() {
        assertEquals(0, player.getVictoryPoints());
    }

    @Test
    /**
     * tests if method setVictoryPoints works
     */
    public void setVictoryPoints() {
        player.setVictoryPoints(10);
        assertEquals(10, player.getVictoryPoints());
    }

    @Test
    /**
     * tests if method getFigureByPos in class FigureFunction works
     */
    public void getFigureByPos() {
        player.getFigures().get(0).setPos(3);
        player.getFigures().get(1).setPos(5);
        player.getFigures().get(2).setPos(6);
        assertEquals(FigureFunction.getFigureByPos(3, player.getFigures()), player.getFigures().get(0));
        assertEquals(FigureFunction.getFigureByPos(2, player.getFigures()), player.getFigures().get(1));
        assertEquals(FigureFunction.getFigureByPos(1, player.getFigures()), player.getFigures().get(2));
        player.getFigures().get(0).setPos(5);
        assertEquals(FigureFunction.getFigureByPos(3, player.getFigures()), player.getFigures().get(0));
        assertEquals(FigureFunction.getFigureByPos(2, player.getFigures()), player.getFigures().get(1));
    }

    @Test
    /**
     * tests if method getLastMovedFigure works
     */
    public void getLastMovedFigure() {
        player.placeFigure('r', FigureFunction.getFigureByPos(1, player.getFigures()));
        assertEquals(FigureFunction.getFigureByPos(1, player.getFigures()), player.getLastMovedFigure());
    }

    @Test
    /**
     * tests if method drawFromDiscardingPile works
     */
    public void drawFromDiscardingPile() throws Exception {
        // add card to wrong pile
        assertThrows(Exception.class, () -> game.getDiscardPile('p').add(new Card(3, 'o')), "Card is null or a different color than the pile.");
        // try to draw from empty pile
        assertThrows(Exception.class, () -> player.drawFromDiscardingPile(game.getDiscardPile('b')), "You fool: this pile is empty!");
        game.getDiscardPile('b').getPile().add(new Card(4, 'b'));
        player.setLastTrashed(game.getDiscardPile('b').getPile().get(0));
        assertThrows(Exception.class, () -> player.drawFromDiscardingPile(game.getDiscardPile('b')), "You can't draw a card you just trashed!");
        game.getDiscardPile('r').add(new Card(2, 'r'));
        player.drawFromDiscardingPile(game.getDiscardPile('r'));
        assertEquals('r', player.getHand().get(0).getColor());
        assertEquals(2, player.getHand().get(0).getNumber());
    }

    @Test
    /**
     * tests if method drawFromDrawingPile works
     */
    void drawFromDrawingPile() {
        player.drawFromDrawingPile();
        assertEquals(1, player.getHand().size());
    }

    @Test
    /**
     * tests if method canPlay works
     */
    public void canPlay() {
        player.getHand().add(new Card(2, 'r'));
        assertTrue(player.canPlay());
        player.getHand().remove(0);
        assertFalse(player.canPlay());
    }

    @Test
    /**
     * tests if method allFiguresGoblin works
     */
    public void allFiguresGoblin(){
        assertFalse(player.allFiguresGoblin());
        Game.FIELDS[0].setToken(new Goblin());
        assertTrue(player.allFiguresGoblin());
        Game.FIELDS[5].setToken(new Goblin());
        player.getFigures().get(0).setPos(5);
        assertTrue(player.allFiguresGoblin());
        Game.FIELDS[10].setToken(new Goblin());
        player.getFigures().get(1).setPos(10);
        assertTrue(player.allFiguresGoblin());
        Game.FIELDS[10].setToken(null);
        assertFalse(player.allFiguresGoblin());
    }

    @Test
    /**
     * tests if method goblinSpecialPoints works
     */
    public void goblinSpecialPoints(){
        Game.FIELDS[0].setToken(new Goblin());
        assertEquals(5 , player.goblinSpecialPoints());
        Game.FIELDS[5].setToken(new Goblin());
        player.getFigures().get(0).setPos(5);
        assertEquals(10 , player.goblinSpecialPoints());
        Game.FIELDS[10].setToken(new Goblin());
        player.getFigures().get(1).setPos(10);
        assertEquals(15 , player.goblinSpecialPoints());
        Game.FIELDS[10].setToken(null);
        assertEquals(0 , player.goblinSpecialPoints());
    }

    @Test
    /**
     * tests if method playGoblinSpecial works
     */
    public void playGoblinSpecial(){
        assertFalse(player.isGoblinSpecialPlayed());
        Game.FIELDS[0].setToken(new Goblin());
        Game.FIELDS[5].setToken(new Goblin());
        player.getFigures().get(0).setPos(5);
        player.playGoblinSpecial();
        assertEquals(10 , player.getVictoryPoints());
        assertTrue(player.isGoblinSpecialPlayed());

    }

    @Test
    /**
     * tests if method cardFitsToAnyPile works
     */
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
    /**
     * tests if method calcTokenPoints works
     */
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