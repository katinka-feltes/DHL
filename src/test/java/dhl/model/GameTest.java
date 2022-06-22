package dhl.model;

import dhl.controller.player_logic.Human;
import dhl.view.Cli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests the class Game
 */
public class GameTest {

    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    /**
     * creates a new game with 3 players
     */
    public void setup() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("test1", 'v', new Human(new Cli())));
        players.add(new Player("test2", 'b', new Human(new Cli())));
        players.add(new Player("test3", 'g', new Human(new Cli())));
        game = new Game(players);
        player1 = game.getPlayers().get(0);
        player2 = game.getPlayers().get(1);
    }
    private int getSizeOfDiscardingPiles() {
        return game.getDiscardPile('o').pile.size() +
                game.getDiscardPile('b').pile.size() +
                game.getDiscardPile('g').pile.size() +
                game.getDiscardPile('p').pile.size() +
                game.getDiscardPile('r').pile.size();
    }

    @Test
    /**
     * tests if method gameOver works
     */
    public void gameOver() {
        //checks if game ends when one player has all 3 in finish area
        List<Figure> figures = player1.getFigures();
        figures.get(0).setPos(22);
        figures.get(1).setPos(22);
        figures.get(2).setPos(22);
        assertTrue(game.gameOver());
        figures.get(0).setPos(0);
        assertFalse(game.gameOver());
        //checks if game ends when 5 figures are in finish area
        Player player1 = game.getPlayers().get(1);
        Player player2 = game.getPlayers().get(2);
        List<Figure> figures1 = player1.getFigures();
        List<Figure> figures2 = player2.getFigures();
        figures1.get(0).setPos(22);
        figures1.get(1).setPos(22);
        figures2.get(0).setPos(22);
        assertTrue(game.gameOver());
        //checks if game ends when drawing pile is empty
        game.getDrawingPile().getCards().clear();
        assertTrue(game.gameOver());
    }

    @Test
    /**
     * tests if method putCardOnDiscardingPile works
     */
    public void putCardOnDiscardingPile() throws Exception {
        player1.getHand().clear();
        player1.getHand().add(new Card(1, 'r'));
        player1.getHand().add(new Card(1, 'g'));
        player1.getHand().add(new Card(1, 'b'));
        player1.getHand().add(new Card(1, 'p'));
        player1.getHand().add(new Card(1, 'o'));
        List<Card> hand = new ArrayList<>(player1.getHand());
        for (Card card : hand) {
            player1.putCardOnDiscardingPile(card);
        }
        assertEquals(5, getSizeOfDiscardingPiles());
    }

    @Test
    /**
     * tests if method canDrawFromDiscarding works
     * false when lastTrashed = top card of same colored pile and others empty
     */
    public void canDrawFromDiscardingLastTrashed() {
        player1.setLastTrashed(new Card(5, 'b'));
        game.getDiscardPile('b').pile.add(player1.getLastTrashed());
        assertFalse(game.canDrawFromDiscarding(player1.getLastTrashed()));
        game.getDiscardPile('b').getPile().clear();
        player1.setLastTrashed(new Card(5, 'r'));
        game.getDiscardPile('r').pile.add(player1.getLastTrashed());
        assertFalse(game.canDrawFromDiscarding(player1.getLastTrashed()));
        game.getDiscardPile('r').getPile().clear();
        player1.setLastTrashed(new Card(5, 'g'));
        game.getDiscardPile('g').pile.add(player1.getLastTrashed());
        assertFalse(game.canDrawFromDiscarding(player1.getLastTrashed()));
        game.getDiscardPile('g').getPile().clear();
        player1.setLastTrashed(new Card(5, 'p'));
        game.getDiscardPile('p').pile.add(player1.getLastTrashed());
        assertFalse(game.canDrawFromDiscarding(player1.getLastTrashed()));
        game.getDiscardPile('p').getPile().clear();
        player1.setLastTrashed(new Card(5, 'o'));
        game.getDiscardPile('o').pile.add(player1.getLastTrashed());
        assertFalse(game.canDrawFromDiscarding(player1.getLastTrashed()));
    }

    @Test
    /**
     * also tests if method canDrawFromDiscarding works
     * false when all piles empty and true if one card different to last trashed exists
     */
    public void canDrawFromDiscardingOtherOptions() {
        //false if all piles are empty
        assertFalse(game.canDrawFromDiscarding(player1.getLastTrashed()));
        //true if lastTrashed different from at least one pile's top card
        player1.setLastTrashed(new Card(5, 'o'));
        game.getDiscardPile('o').pile.add(player1.getLastTrashed());
        player1.setLastTrashed(new Card(5, 'b'));
        assertTrue(game.canDrawFromDiscarding(player1.getLastTrashed()));
    }

    @Test
    /**
     * tests if method getDrawingPile works
     */
    public void getDrawingPile() {
        assertEquals(86, game.getDrawingPile().getCards().size());
        player1.getHand().removeAll(player1.getHand());
        player1.drawFromDrawingPile();
        player1.drawFromDrawingPile();
        assertEquals(84, game.getDrawingPile().getCards().size());
    }

    @Test
    /**
     * tests if method getPlayers works
     */
    public void getPlayers() {
        assertEquals(3, game.getPlayers().size());
        game.getPlayers().remove(player1);
        assertEquals(2, game.getPlayers().size());
    }

    @Test
    /**
     * tests if method getPlayerAmount works
     */
    public void getPlayerAmount() {
        assertEquals(3, game.getPlayerAmount());
    }

    @Test
    /**
     * tests if method setup works
     */
    public void createDecks() {
        game.setup();
        for (Player player1 : game.getPlayers()) {
            assertEquals(8, player1.getHand().size());
        }
    }

    @Test
    /**
     * tests if method getWinningPlayer works
     */
    public void getWinningPlayer() {
        player1.setVictoryPoints(20);
        player2.setVictoryPoints(10);
        assertEquals(player1, game.getSortedPlayers().get(0));
        player2.setVictoryPoints(30);
        assertEquals(player2, game.getSortedPlayers().get(0));
    }
}