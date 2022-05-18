package dhl.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        game = new Game(new String[]{"Player 1", "Player 2", "Player 3"});
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
        Figure[] figures = player1.getFigures();
        figures[0].setPos(22);
        figures[1].setPos(22);
        figures[2].setPos(22);
        assertTrue(game.gameOver());
        figures[0].setPos(0);
        assertFalse(game.gameOver());
        //checks if game ends when 5 figures are in finish area
        Player player1 = game.getPlayers().get(1);
        Player player2 = game.getPlayers().get(2);
        Figure[] figures1 = player1.getFigures();
        Figure[] figures2 = player2.getFigures();
        figures1[0].setPos(22);
        figures1[1].setPos(22);
        figures2[0].setPos(22);
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
        player1.getHand().add(new Card(1, 'r'));
        player1.getHand().add(new Card(1, 'g'));
        player1.getHand().add(new Card(1, 'b'));
        player1.getHand().add(new Card(1, 'p'));
        player1.getHand().add(new Card(1, 'o'));
        ArrayList<Card> hand = new ArrayList<>(player1.getHand());
        for (Card card : hand) {
            player1.putCardOnDiscardingPile(card);
        }
        assertEquals(5, getSizeOfDiscardingPiles());
    }

    @Test
    /**
     * tests if method canDrawFromDiscarding works
     * too many asserts because we need to test all color options
     */
    public void canDrawFromDiscarding() {
        //false if all piles are empty
        player1.setLastTrashed(new Card(5, 'b'));
        assertFalse(game.canDrawFromDiscarding(player1.getLastTrashed()));
        //false if lastTrashed = top card of same colored pile, others empty
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
        //true if none of above
        player1.setLastTrashed(new Card(5, 'b'));
        assertTrue(game.canDrawFromDiscarding(player1.getLastTrashed()));
    }

    @Test
    /**
     * tests if method getDrawingPile works
     */
    public void getDrawingPile() {
        assertEquals(110, game.getDrawingPile().getCards().size());
        player1.getHand().removeAll(player1.getHand());
        player1.drawFromDrawingPile();
        player1.drawFromDrawingPile();
        assertEquals(108, game.getDrawingPile().getCards().size());
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
     * tests if method createDecks works
     */
    public void createDecks() {
        game.createDecks();
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
        assertEquals(player1, game.getWinningPlayer());
        player2.setVictoryPoints(30);
        assertEquals(player2, game.getWinningPlayer());
    }
}