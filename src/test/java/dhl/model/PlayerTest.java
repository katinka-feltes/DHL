package dhl.model;

import dhl.controller.player_logic.Human;
import dhl.model.tokens.Goblin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests the class Player
 */
public class PlayerTest {

    Game game;
    Player player;

    /**
     * creates a new game with 2 players
     */
    @BeforeEach
    public void setup() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("test1", 'v', new Human()));
        players.add(new Player("test2", 'b', new Human()));
        game = new Game(players);
        player = game.getPlayers().get(0);
    }

    /**
     * tests if method placeFigure works
     */
    @Test
    public void placeFigure() throws Exception {
        Figure fig = FigureFunction.getFigureByPos(1, player.getFigures());
        player.placeFigure('r', fig);
        assertEquals(3, fig.getPos());
        player.placeFigure('g', fig);
        assertEquals(4, fig.getPos());
        fig = FigureFunction.getFigureByPos(3, player.getFigures());
        player.placeFigure('g', fig);
        assertEquals(4, fig.getPos());
    }


    /**
     * tests if method getFigureAmountOnField from class FigureFunction works
     */
    @Test
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

    /**
     * tests if method getName works
     */
    @Test
    public void getName() {
        assertEquals("test1", player.getName());
    }

    /**
     * tests if method getHand works
     */
    @Test
    public void getHand() {
        game.setup();
        assertEquals(8, player.getHand().size());
        for (Card card : player.getHand()) {
            assertEquals(Card.class, card.getClass());
        }
    }

    /**
     * tests if method getVictoryPoints works
     */
    @Test
    public void getVictoryPoints() {
        assertEquals(0, player.getVictoryPoints());
    }

    /**
     * tests if method setVictoryPoints works
     */
    @Test
    public void setVictoryPoints() {
        player.setVictoryPoints(10);
        assertEquals(10, player.getVictoryPoints());
    }

    /**
     * tests if method getFigureByPos in class FigureFunction works
     */
    @Test
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

    /**
     * tests if method getLastMovedFigure works
     */
    @Test
    public void getLastMovedFigure() throws Exception {
        player.placeFigure('r', FigureFunction.getFigureByPos(1, player.getFigures()));
        assertEquals(FigureFunction.getFigureByPos(1, player.getFigures()), player.getLastMovedFigure());
    }

    /**
     * tests if method drawFromDiscardingPile works
     */
    @Test
    public void drawFromDiscardingPile() throws Exception {
        player.getHand().clear();
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


    /**
     * tests if method drawFromDrawingPile works
     */
    @Test
    public void drawFromDrawingPile() {
        player.getHand().clear();
        player.drawFromDrawingPile();
        assertEquals(1, player.getHand().size());
    }


    /**
     * tests if method canPlay works
     */
    @Test
    public void canPlay() {
        player.getHand().clear();
        player.getHand().add(new Card(2, 'r'));
        assertTrue(player.canPlay());
        player.getHand().remove(0);
        assertFalse(player.canPlay());
    }

    /**
     * tests if method amountFiguresGoblin works
     */
    @Test
    public void amountFiguresGoblin(){
        assertEquals(0, player.amountFiguresGoblin());
        game.getFields()[0].setToken(new Goblin());
        assertEquals(3, player.amountFiguresGoblin());
        game.getFields()[5].setToken(new Goblin());
        player.getFigures().get(0).setPos(5);
        assertEquals(3, player.amountFiguresGoblin());
        game.getFields()[10].setToken(new Goblin());
        player.getFigures().get(1).setPos(10);
        assertEquals(3,player.amountFiguresGoblin());
        game.getFields()[10].setToken(null);
        assertEquals(2, player.amountFiguresGoblin());
    }


    /**
     * tests if method goblinSpecialPoints works
     */
    @Test
    public void goblinSpecialPoints(){
        game.getFields()[0].setToken(new Goblin());
        assertEquals(5 , player.goblinSpecialPoints());
        game.getFields()[5].setToken(new Goblin());
        player.getFigures().get(0).setPos(5);
        assertEquals(10 , player.goblinSpecialPoints());
        game.getFields()[10].setToken(new Goblin());
        player.getFigures().get(1).setPos(10);
        assertEquals(15 , player.goblinSpecialPoints());
        game.getFields()[10].setToken(null);
        assertEquals(0 , player.goblinSpecialPoints());
    }

    /**
     * tests if method playGoblinSpecial works
     */
    @Test
    public void playGoblinSpecial(){
        assertFalse(player.isGoblinSpecialPlayed());
        game.getFields()[0].setToken(new Goblin());
        game.getFields()[5].setToken(new Goblin());
        player.getFigures().get(0).setPos(5);
        player.playGoblinSpecial();
        assertEquals(10 , player.getVictoryPoints());
        assertTrue(player.isGoblinSpecialPlayed());

    }

    /**
     * tests if method cardFitsToAnyPile works
     */
    @Test
    public void cardFitsToAnyPile() {
        player.getHand().clear();
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

    /**
     * tests if method calcTokenPoints works
     */
    @Test
    public void calcTokenPoints() {
        player.increaseStoneAmount();
        player.increaseMirrorAmount();
        int calculatedPoints = player.calcTokenPoints();
        assertEquals(-6, calculatedPoints);
        player.increaseMirrorAmount();
        calculatedPoints = player.calcTokenPoints();
        assertEquals(-12, calculatedPoints);
    }


    /**
     * tests if method getSortedHand works
     */
    @Test
    public void getSortedHand() {
        player.getHand().removeAll(player.getHand());
        player.getHand().add(new Card(1, 'r'));
        player.getHand().add(new Card(1, 'g'));
        player.getHand().add(new Card(1, 'b'));
        player.getHand().add(new Card(1, 'p'));
        player.getHand().add(new Card(2, 'r'));
        player.getHand().add(new Card(1, 'g'));
        player.getHand().add(new Card(0, 'b'));
        player.getHand().add(new Card(0, 'o'));

        List<Card> sortedHand = new ArrayList<>(CardFunction.sortHand(player.getHand()));
        assertEquals('b', sortedHand.get(0).getColor());
        assertEquals(0, sortedHand.get(0).getNumber());
        assertEquals('b', sortedHand.get(1).getColor());
        assertEquals('r', sortedHand.get(sortedHand.size()-1).getColor());
        assertEquals(2, sortedHand.get(sortedHand.size()-1).getNumber());
    }

    /**
     * tests if method getCardFromHand works
     */
    @Test
    public void getCardFromHand() throws Exception {
        player.getHand().clear();
        player.getHand().add(new Card(2, 'r'));
        assertEquals(CardFunction.getCardFromHand("r2", player.getHand()), player.getHand().get(0));
        player.getHand().remove(0);
        player.getHand().add(new Card(10, 'g'));
        assertEquals(CardFunction.getCardFromHand("g10", player.getHand()), player.getHand().get(0));
        assertThrows(Exception.class, () -> CardFunction.getCardFromHand("p2", player.getHand()), "Card is not in Hand.");
    }

}