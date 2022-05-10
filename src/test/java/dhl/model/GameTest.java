package dhl.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Player player;

    @BeforeEach
    void setup() {
        game = new Game(new String[]{"Player 1", "Player 2", "Player 3"});
        player = game.getPlayers().get(0);
    }
    private int getSizeOfDiscardingPiles() {
        return game.getDiscardingPileOrange().pile.size() +
                game.getDiscardingPileBlue().pile.size() +
                game.getDiscardingPileGreen().pile.size() +
                game.getDiscardingPilePurple().pile.size() +
                game.getDiscardingPileRed().pile.size();
    }

    @Test
    public void gameOver() {
        //checks if game ends when one player has all 3 in finish area
        Figure[] figures = player.getFigures();
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
    void putCardOnDiscardingPile() throws Exception {
        player.getHand().add(new Card(1, 'r'));
        player.getHand().add(new Card(1, 'g'));
        player.getHand().add(new Card(1, 'b'));
        player.getHand().add(new Card(1, 'p'));
        player.getHand().add(new Card(1, 'o'));
        ArrayList<Card> hand = new ArrayList<>(player.getHand());
        for (Card card : hand) {
            player.putCardOnDiscardingPile(card);
        }
        assertEquals(5, getSizeOfDiscardingPiles());
        assertThrows(Exception.class, () -> player.putCardOnDiscardingPile(new Card(1, 'f')), "Color of the card doesn't exist.");
    }

    @Test
    void canDrawFromDiscarding() {
        //false if all piles are empty
        player.setLastTrashed(new Card(5, 'b'));
        assertFalse(game.canDrawFromDiscarding(player.getLastTrashed()));
        //false if lastTrashed = top card of same colored pile, others empty
        game.getDiscardingPileBlue().pile.add(player.getLastTrashed());
        assertFalse(game.canDrawFromDiscarding(player.getLastTrashed()));
        game.getDiscardingPileBlue().getPile().clear();
        player.setLastTrashed(new Card(5, 'r'));
        game.getDiscardingPileRed().pile.add(player.getLastTrashed());
        assertFalse(game.canDrawFromDiscarding(player.getLastTrashed()));
        game.getDiscardingPileRed().getPile().clear();
        player.setLastTrashed(new Card(5, 'g'));
        game.getDiscardingPileGreen().pile.add(player.getLastTrashed());
        assertFalse(game.canDrawFromDiscarding(player.getLastTrashed()));
        game.getDiscardingPileGreen().getPile().clear();
        player.setLastTrashed(new Card(5, 'p'));
        game.getDiscardingPilePurple().pile.add(player.getLastTrashed());
        assertFalse(game.canDrawFromDiscarding(player.getLastTrashed()));
        game.getDiscardingPilePurple().getPile().clear();
        player.setLastTrashed(new Card(5, 'o'));
        game.getDiscardingPileOrange().pile.add(player.getLastTrashed());
        assertFalse(game.canDrawFromDiscarding(player.getLastTrashed()));
        //true if none of above
        player.setLastTrashed(new Card(5, 'b'));
        assertTrue(game.canDrawFromDiscarding(player.getLastTrashed()));
    }

    @Test
    void placeTokens() {
    }

    @Test
    void getDrawingPile() {
        assertEquals(110, game.getDrawingPile().getCards().size());
        player.getHand().removeAll(player.getHand());
        player.drawCardsUpToEight(game.getDrawingPile());
        assertEquals(102, game.getDrawingPile().getCards().size());
    }

    @Test
    void getDiscardingPileBlue() {
    }

    @Test
    void getDiscardingPileRed() {
    }

    @Test
    void getDiscardingPileGreen() {
    }

    @Test
    void getDiscardingPilePurple() {
    }

    @Test
    void getDiscardingPileOrange() {
    }

    @Test
    void getPlayers() {
        assertEquals(3, game.getPlayers().size());
        game.getPlayers().remove(player);
        assertEquals(2, game.getPlayers().size());
    }

    @Test
    void getPlayerAmount() {
        assertEquals(3, game.getPlayerAmount());
    }

    @Test
    void createDecks() {
        game.createDecks();
        for (Player player : game.getPlayers()) {
            assertEquals(8, player.getHand().size());
        }
    }
}