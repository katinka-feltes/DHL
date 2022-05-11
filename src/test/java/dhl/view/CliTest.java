package dhl.view;

import dhl.model.Card;
import dhl.model.Game;
import dhl.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class CliTest {

    private Cli c;

    private Game game;
    private Player player;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;


    @BeforeEach
    public void setUp() {
        c = new Cli();
        game = new Game(new String[]{"Player 1", "Player 2"});
        player = game.getPlayers().get(0);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void promptInt() {
        String input;
        InputStream bytes;
        int i;

        input = "5";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        i = c.promptInt(3,7, "test");
        assertEquals(5, i);

        input = "9 3";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        i = c.promptInt(3,7, "test");
        assertEquals(3, i);
    }

    @Test
    void promptCardString() {
        String input;
        InputStream bytes;
        String s;

        input = "r5";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        s = c.promptCardString("test");
        assertEquals("r5", s);
    }

    @Test
    void error() {
    }

    @Test
    void printHand() {
        player.getHand().add(new Card(5, 'r'));
        c.printHand(player);
        assertEquals("Player 1's Hand Cards:\nr5   \n\n", outContent.toString());
    }

    @Test
    void printTopCards() {
    }

    @Test
    void printResults() {
    }

    @Test
    void printDiscardingPiles() {
    }

    @Test
    void out() {
    }

    @Test
    void inputPlayersNames() {

    }

    @Test
    void promptPlayersChoice() {
        String input;
        InputStream bytes;

        input = "y";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        assertTrue(c.promptPlayersChoice("test"));

        input = "n";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        assertFalse(c.promptPlayersChoice("test"));
    }

    @Test
    void promptColor() {
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
    void promptColorAndHand() {
        String input;
        InputStream bytes;
        char ch;

        input = "h";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        ch = c.promptColorAndHand("test");
        assertEquals('h', ch);
    }

    @Test
    void printCurrentBoard() {
    }
}