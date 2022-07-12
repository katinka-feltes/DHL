package dhl.controller.player_logic;

import dhl.model.Card;
import dhl.model.Figure;
import dhl.model.Player;
import dhl.player_logic.Human;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for the methods in the class Human
 */
class HumanTest {

    private Human h;
    private Player p;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    /**
     * creates a new Cli and a Game with 2 players
     */
    public void setUp() {
        h = new Human();

        p = new Player("p1", 'x', h);

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }


    @Test
    void chooseSpiralPosition() {
        String input = "16 5";
        InputStream bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);

        Figure fig = p.getFigures().get(0);
        int pos = 15;
        fig.setPos(pos);

        int result = h.chooseSpiralPosition("", pos);
        assertEquals("Please enter a number between 0 and " + pos + ".\n", errContent.toString());
        assertEquals(5, result);
    }

    @Test
    void choose() {
        String input;
        InputStream bytes;

        input = "y";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        assertTrue(h.choose("test"));

        input = "w\nn";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        assertFalse(h.choose("test"));
        //check if the error was thrown
        assertEquals("Please enter either y or n.\n", errContent.toString());
    }

    @Test
    void chooseCard() {
        List<Card> hand= p.getHand();
        //add only one card to the hand
        hand.clear();
        Card card = new Card(5, 'r');
        hand.add(card);

        String input = "r5";
        InputStream bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        Card result = h.chooseCard("", hand);
        assertEquals(card, result);
    }

    @Test
    void chooseFigure() {
        String input;
        InputStream bytes;

        input = "e\n1";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);

        Figure first = p.getFigures().get(0);
        assertThrows(Exception.class, () -> first.move('b'));

        Figure result = h.chooseFigure("", p.getFigures());
        assertEquals("Input must be an integer!\n", errContent.toString());
        assertEquals(first, result);

        input = "3";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        result = h.chooseFigure("", p.getFigures());
        assertEquals(0, result.getPos());
    }


    @Test
    void choosePileColor() {
        String input = "b";
        InputStream bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        char result = h.choosePileColor("test");
        assertEquals('b', result);
    }
}