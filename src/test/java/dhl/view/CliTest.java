package dhl.view;

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

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    /**
     * creates a new Cli and a Game with 2 players
     */
    @BeforeEach
    public void setUp() {
        c = new Cli();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    /**
     * tests if method promptColor works
     */
    @Test
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

    /**
     * tests if method promptCardString works
     */
    @Test
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