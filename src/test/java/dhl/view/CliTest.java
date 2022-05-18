package dhl.view;

import dhl.model.Game;
import dhl.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * tests the class Cli
 */
public class CliTest {

    private Cli c;

    private Game game;
    private Player player;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    /**
     * creates a new Cli and a Game with 2 players
     */
    public void setUp() {
        c = new Cli();
        game = new Game(new String[]{"Player 1", "Player 2"});
        player = game.getPlayers().get(0);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    /**
     * tests if method promptColor works
     */
    public void promptColor() {
        String input;
        InputStream bytes;
        char ch;

        input = "b";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        ch = c.promptColor("test");
        assertEquals('b', ch);
    }

    @Test
    /**
     * tests if method promptCardString works
     */
    public void promptCardString() {
        String input;
        InputStream bytes;
        String s;

        input = "r5";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        s = c.promptCardString("test");
        assertEquals("r5", s);
    }
}