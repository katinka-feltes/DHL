package dhl.controller;

import dhl.model.Game;
import dhl.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ControllerTest {

    private Controller controller;

    @BeforeEach
    public void setUp() {
        controller = new Controller();
    }

    @Test
    void startGame() {
        /**
        String input = "2 p1\np2";
        InputStream input2 = new ByteArrayInputStream(input.getBytes());
        System.setIn(input2);
        controller.setView(new Cli());
        controller.startGame(); **/
    }

    @Test
    public void takeTurn() {
    }

    @Test
    public void playCard() {
        //player wants to play wrong card (card not in hand)
        Game game = new Game(new String[]{"Player 1", "Player 2"});
        Player player = game.getPlayers().get(0);
        String input = "b4\nr5";
        InputStream input2 = new ByteArrayInputStream(input.getBytes());
        System.setIn(input2);
        assertThrows(Exception.class, () -> controller.playCard(player), "Card is not in Hand.");
        // player wants to play correct card
        /** Card card = new Card(5, 'r');
        player.getHand().add(card);
        assertEquals(1, player.getHand().size());
        controller.playCard(player);
        assertEquals(0, player.getHand().size());
        assertTrue(player.getPlayedCardsRed().getTop() == card); */
    }

    @Test
    public void setView() {
    }
}