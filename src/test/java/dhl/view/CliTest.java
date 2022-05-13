package dhl.view;

import dhl.model.Card;
import dhl.model.Field;
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

        input = "e\n5";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        i = c.promptInt(3, 7, "test");
        assertEquals("Input must be an integer!\n", errContent.toString());
        assertEquals(5, i);

        input = "9 3";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        i = c.promptInt(3, 7, "test");
        assertEquals(3, i);
    }

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

    @Test
    public void error() {
        c.error("test");
        assertEquals("test\n", errContent.toString());
    }

    @Test
    public void printHand() {
        player.getHand().add(new Card(5, 'r'));
        c.printHand(player);
        assertEquals("Player 1's Hand Cards:\nr5   \n\n", outContent.toString());
    }

    @Test
    public void printTopCards() throws Exception {
        player.getPlayedCardsOrange().add(new Card(1, 'o'));
        player.getPlayedCardsOrange().add(new Card(2, '0'));
        player.getPlayedCardsRed().add(new Card(3, 'r'));
        c.printTopCards(player);

        assertEquals("Player 1's Top Card:\nb+-    g+-    o2+   p+-    r3+-   \n\n", outContent.toString());

    }

    @Test
    public void printResults() {
        player.setVictoryPoints(12);
        c.printResults(game);
        assertEquals("\nGAME OVER! The winner is Player 1.\n" +
                "Points: Player 1: 12   Player 2: 0   \n", outContent.toString());
    }

    @Test
    public void printDiscardingPiles() throws Exception {
        game.getDiscardingPileBlue().add(new Card(1, 'b'));
        game.getDiscardingPileRed().add(new Card(4, 'r'));
        game.getDiscardingPileRed().add(new Card(5, 'r'));
        game.getDiscardingPilePurple().add(new Card(10, 'p'));
        c.printDiscardingPiles(game);

        assertEquals("\nTop Cards of all Discarding Piles: \n" +
                "b1    g     o     p10    r5    \n", outContent.toString());
    }

    @Test
    public void out() {
        c.out("test");
        assertEquals("test\n", outContent.toString());
    }

    @Test
    public void inputPlayersNames() {
        String input = "p1\ntestwithnamelongerthat16\np2";
        InputStream bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);

        String[] expected = {"p1", "p2"};
        String[] actual = c.inputPlayersNames(2);

        assertEquals(expected[0], actual[0]);
        assertEquals("Name can't be empty and only be 16 characters long. Try again!\n", errContent.toString());
        assertEquals(expected[1], actual[1]);
    }

    @Test
    public void promptPlayersChoice() {
        String input;
        InputStream bytes;

        input = "y";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        assertTrue(c.promptPlayersChoice("test"));

        input = "w\nn";
        bytes = new ByteArrayInputStream(input.getBytes());
        System.setIn(bytes);
        assertFalse(c.promptPlayersChoice("test"));
        //check if the error was thrown
        assertEquals("Please enter either y or n.\n", errContent.toString());
    }

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

    @Test
    public void promptColorAndHand() {
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
    public void printCurrentBoard(){
        //remove token
        for (Field field: game.FIELDS){
            field.setToken(null);
        }
        c.printCurrentBoard(game);
        assertEquals("Player 1:♠    Player 2:♣    \n" +
                "(0)    (1)    (2)    (3)    (4)    (5)    (6)    (7)    (8)    (9)    (10)   (11)   \n" +
                "0      -4     -4     -4     -4     -4     -4     -3     -3     -3     -2     -2     \n" +
                "w      p      o      r      g      b      p      r      o      r      g      b      \n" +
                "                                                                                    \n" +
                "♠ ♠ ♠                                                                               \n" +
                "♣ ♣ ♣                                                                               \n" +
                "(12)   (13)   (14)   (15)   (16)   (17)   (18)   (19)   (20)   (21)   (22)   (23)   \n" +
                "-2     -2     1      1      1      2      2      2      2      3      3      3      \n" +
                "p      o      o      r      g      b      p      o      r      r      b      p      \n" +
                "                                                                                    \n" +
                "                                                                                    \n" +
                "                                                                                    \n" +
                "(24)   (25)   (26)   (27)   (28)   (29)   (30)   (31)   (32)   (33)   (34)   (35)   \n" +
                "3      5      5      5      5      6      6      6      7      7      7      10     \n" +
                "g      o      r      g      g      b      r      o      r      g      b      b      \n" +
                "                                                                                    \n" +
                "                                                                                    \n" +
                "                                                                                    \n" +
                "Points: Player 1: 0   Player 2: 0   \n\n", outContent.toString());
    }


}