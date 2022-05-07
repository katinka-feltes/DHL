package dhl.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    private Game game;
    private Player player;

    @BeforeEach
    void setup() {
        game = new Game(2);
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
        game.getDrawingPile().getCards().clear();
        assertTrue(game.gameOver());
    }

    @Test
    void putCardOnDiscardingPile() throws Exception {
        game.putCardOnDiscardingPile(game.getDrawingPile().draw(), player);
        assertEquals(1, getSizeOfDiscardingPiles());
    }

    @Test
    void placeTokens() {
    }

    @Test
    void getDrawingPile() {
        assertEquals(110, game.getDrawingPile().getCards().size());
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
    }

    @Test
    void getPlayerAmount() {
    }
}