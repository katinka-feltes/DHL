package dhl.controller;

import dhl.model.Game;
import dhl.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * tests the class Controller
 * not really used since many methods can't be tested due to infinity loops
 */
public class ControllerTest {

    private Controller controller;

    @BeforeEach
    /**
     * creates a controller before each test
     */
    public void setUp() {
        controller = new Controller();
    }

    @Test
    /**
     * tests the playCard method but only if input is wrong since two inputs didn't work
     */
    public void playCard() {
        //player wants to play wrong card (card not in hand)
        Game game = new Game(new String[]{"Player 1", "Player 2"});
        Player player = game.getPlayers().get(0);
        String input = "b4";
        InputStream input2 = new ByteArrayInputStream(input.getBytes());
        System.setIn(input2);
        assertThrows(Exception.class, () -> controller.playCard(player), "Card is not in Hand.");
    }
}