package dhl.model;

import dhl.controller.player_logic.Human;
import dhl.model.tokens.Goblin;
import dhl.model.tokens.Mirror;
import dhl.model.tokens.WishingStone;
import dhl.view.Cli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests the class Player
 */
public class PlayerTest {

    dhl.model.Game game;
    dhl.model.Player player;

    @BeforeEach
    /**
     * creates a new game with 2 players
     */
    public void setup() {
        List<dhl.model.Player> players = new ArrayList<>();
        players.add(new dhl.model.Player("test1", 'v', new Human(new Cli())));
        players.add(new dhl.model.Player("test2", 'b', new Human(new Cli())));
        game = new dhl.model.Game(players);
        player = game.getPlayers().get(0);
    }

    @Test
    /**
     * tests if method placeFigure works
     */
    public void placeFigure() {
        dhl.model.Figure fig = dhl.model.FigureFunction.getFigureByPos(1, player.getFigures());
        player.placeFigure('r', fig);
        assertEquals(3, fig.getPos());
        player.placeFigure('g', fig);
        assertEquals(4, fig.getPos());
        fig = dhl.model.FigureFunction.getFigureByPos(3, player.getFigures());
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
        assertEquals(1, dhl.model.FigureFunction.getFigureAmountOnField(5, player.getFigures()));
        player.getFigures().get(0).setPos(7);
        assertEquals(2, dhl.model.FigureFunction.getFigureAmountOnField(7, player.getFigures()));
        player.getFigures().get(2).setPos(7);
        assertEquals(3, dhl.model.FigureFunction.getFigureAmountOnField(7, player.getFigures()));
    }

    @Test
    /**
     * tests if method getName works
     */
    public void getName() {
        assertEquals("test1", player.getName());
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
        game.setup();
        assertEquals(8, player.getHand().size());
        for (dhl.model.Card card : player.getHand()) {
            assertEquals(dhl.model.Card.class, card.getClass());
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
        assertEquals(dhl.model.FigureFunction.getFigureByPos(3, player.getFigures()), player.getFigures().get(0));
        assertEquals(dhl.model.FigureFunction.getFigureByPos(2, player.getFigures()), player.getFigures().get(1));
        assertEquals(dhl.model.FigureFunction.getFigureByPos(1, player.getFigures()), player.getFigures().get(2));
        player.getFigures().get(0).setPos(5);
        assertEquals(dhl.model.FigureFunction.getFigureByPos(3, player.getFigures()), player.getFigures().get(0));
        assertEquals(dhl.model.FigureFunction.getFigureByPos(2, player.getFigures()), player.getFigures().get(1));
    }

    @Test
    /**
     * tests if method getLastMovedFigure works
     */
    public void getLastMovedFigure() {
        player.placeFigure('r', dhl.model.FigureFunction.getFigureByPos(1, player.getFigures()));
        assertEquals(dhl.model.FigureFunction.getFigureByPos(1, player.getFigures()), player.getLastMovedFigure());
    }

    @Test
    /**
     * tests if method drawFromDiscardingPile works
     */
    public void drawFromDiscardingPile() throws Exception {
        player.getHand().clear();
        // add card to wrong pile
        assertThrows(Exception.class, () -> game.getDiscardPile('p').add(new dhl.model.Card(3, 'o')), "Card is null or a different color than the pile.");
        // try to draw from empty pile
        assertThrows(Exception.class, () -> player.drawFromDiscardingPile(game.getDiscardPile('b')), "You fool: this pile is empty!");
        game.getDiscardPile('b').getPile().add(new dhl.model.Card(4, 'b'));
        player.setLastTrashed(game.getDiscardPile('b').getPile().get(0));
        assertThrows(Exception.class, () -> player.drawFromDiscardingPile(game.getDiscardPile('b')), "You can't draw a card you just trashed!");
        game.getDiscardPile('r').add(new dhl.model.Card(2, 'r'));
        player.drawFromDiscardingPile(game.getDiscardPile('r'));
        assertEquals('r', player.getHand().get(0).getColor());
        assertEquals(2, player.getHand().get(0).getNumber());
    }

    @Test
    /**
     * tests if method drawFromDrawingPile works
     */
    public void drawFromDrawingPile() {
        player.getHand().clear();
        player.drawFromDrawingPile();
        assertEquals(1, player.getHand().size());
    }

    @Test
    /**
     * tests if method canPlay works
     */
    public void canPlay() {
        player.getHand().clear();
        player.getHand().add(new dhl.model.Card(2, 'r'));
        assertTrue(player.canPlay());
        player.getHand().remove(0);
        assertFalse(player.canPlay());
    }

    @Test
    /**
     * tests if method amountFiguresGoblin works
     */
    public void amountFiguresGoblin(){
        assertEquals(0, player.amountFiguresGoblin());
        dhl.model.Game.FIELDS[0].setToken(new Goblin());
        assertEquals(3, player.amountFiguresGoblin());
        dhl.model.Game.FIELDS[5].setToken(new Goblin());
        player.getFigures().get(0).setPos(5);
        assertEquals(3, player.amountFiguresGoblin());
        dhl.model.Game.FIELDS[10].setToken(new Goblin());
        player.getFigures().get(1).setPos(10);
        assertEquals(3,player.amountFiguresGoblin());
        dhl.model.Game.FIELDS[10].setToken(null);
        assertEquals(2, player.amountFiguresGoblin());
    }

    @Test
    /**
     * tests if method goblinSpecialPoints works
     */
    public void goblinSpecialPoints(){
        dhl.model.Game.FIELDS[0].setToken(new Goblin());
        assertEquals(5 , player.goblinSpecialPoints());
        dhl.model.Game.FIELDS[5].setToken(new Goblin());
        player.getFigures().get(0).setPos(5);
        assertEquals(10 , player.goblinSpecialPoints());
        dhl.model.Game.FIELDS[10].setToken(new Goblin());
        player.getFigures().get(1).setPos(10);
        assertEquals(15 , player.goblinSpecialPoints());
        dhl.model.Game.FIELDS[10].setToken(null);
        assertEquals(0 , player.goblinSpecialPoints());
    }

    @Test
    /**
     * tests if method playGoblinSpecial works
     */
    public void playGoblinSpecial(){
        assertFalse(player.isGoblinSpecialPlayed());
        dhl.model.Game.FIELDS[0].setToken(new Goblin());
        dhl.model.Game.FIELDS[5].setToken(new Goblin());
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
        player.getHand().clear();
        player.getHand().add(new dhl.model.Card(0, 't'));
        assertFalse(player.canPlay());
        player.getHand().add(new dhl.model.Card(1, 'g'));
        assertTrue(player.canPlay());
        player.getHand().clear();
        player.getHand().add(new dhl.model.Card(1, 'b'));
        assertTrue(player.canPlay());
        player.getHand().clear();
        player.getHand().add(new dhl.model.Card(1, 'p'));
        assertTrue(player.canPlay());
        player.getHand().clear();
        player.getHand().add(new dhl.model.Card(1, 'o'));
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
        int calculatedPoints = player.calcTokenPoints();
        assertEquals(-6, calculatedPoints);
        player.getTokens().add(new Mirror());
        calculatedPoints = player.calcTokenPoints();
        assertEquals(-12, calculatedPoints);
    }

    @Test
    /**
     * tests if method getSortedHand works
     */
    public void getSortedHand() {
        player.getHand().removeAll(player.getHand());
        player.getHand().add(new dhl.model.Card(1, 'r'));
        player.getHand().add(new dhl.model.Card(1, 'g'));
        player.getHand().add(new dhl.model.Card(1, 'b'));
        player.getHand().add(new dhl.model.Card(1, 'p'));
        player.getHand().add(new dhl.model.Card(2, 'r'));
        player.getHand().add(new dhl.model.Card(1, 'g'));
        player.getHand().add(new dhl.model.Card(0, 'b'));
        player.getHand().add(new dhl.model.Card(0, 'o'));

        List<dhl.model.Card> sortedHand = new ArrayList<dhl.model.Card>(dhl.model.CardFunction.sortHand(player.getHand()));
        assertEquals('b', sortedHand.get(0).getColor());
        assertEquals(0, sortedHand.get(0).getNumber());
        assertEquals('b', sortedHand.get(1).getColor());
        assertEquals('r', sortedHand.get(sortedHand.size()-1).getColor());
        assertEquals(2, sortedHand.get(sortedHand.size()-1).getNumber());
    }

    @Test
    /**
     * tests if method getCardFromHand works
     */
    public void getCardFromHand() throws Exception {
        player.getHand().clear();
        player.getHand().add(new dhl.model.Card(2, 'r'));
        assertEquals(dhl.model.CardFunction.getCardFromHand("r2", player.getHand()), player.getHand().get(0));
        player.getHand().remove(0);
        player.getHand().add(new dhl.model.Card(10, 'g'));
        assertEquals(dhl.model.CardFunction.getCardFromHand("g10", player.getHand()), player.getHand().get(0));
        assertThrows(Exception.class, () -> dhl.model.CardFunction.getCardFromHand("p2", player.getHand()), "Card is not in Hand.");
    }

}