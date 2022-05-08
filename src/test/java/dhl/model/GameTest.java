package dhl.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Game game2;
    private Player player;

    @BeforeEach
    void setup() {
        game = new Game(2);
        game2 = new Game(new String[]{"Player 1", "Player 2"});
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
    void gameOver() {
        game.getDrawingPile().getCards().clear();
        assertTrue(game.gameOver());
        game2.getDrawingPile().getCards().clear();
        assertTrue(game2.gameOver());
    }

    @Test
    //TODO: Why card and Player?, Default case needs work.
    void putCardOnDiscardingPile() throws Exception {
        player.getHand().add(new Card(1, 'r'));
        player.getHand().add(new Card(1, 'g'));
        player.getHand().add(new Card(1, 'b'));
        player.getHand().add(new Card(1, 'p'));
        player.getHand().add(new Card(1, 'o'));
        ArrayList<Card> hand = new ArrayList<>(player.getHand());
        for (Card card : hand) {
            game.putCardOnDiscardingPile(card, player);
        }
        assertEquals(5, getSizeOfDiscardingPiles());
        assertThrows(Exception.class, () -> game.putCardOnDiscardingPile(new Card(1, 'f'), player), "Color of the card doesn't exist.");
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
        assertEquals(2, game.getPlayers().size());
        game.getPlayers().remove(player);
        assertEquals(1, game.getPlayers().size());
    }

    @Test
    void getPlayerAmount() {
        assertEquals(2, game.getPlayerAmount());
    }

    @Test
    void createDecks() {
        game.createDecks();
        for (Player player : game.getPlayers()) {
            assertEquals(8, player.getHand().size());
        }
    }
}