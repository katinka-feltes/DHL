package dhl.controller.player_logic;

import dhl.model.Card;
import dhl.model.Game;
import dhl.model.Player;
import dhl.model.tokens.*;
import dhl.player_logic.AI;
import dhl.player_logic.Human;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the methods of the class AI
 */
class AITest {

    private AI ai;
    private Player p0;


    /**
     * called before each test to initialize players and a game.
     */
    @BeforeEach
    public void setUp() {
        ai = new AI();
        Human h = new Human();
        p0 = new Player("p0", 'x', ai);
        Player p1 = new Player("p1", 'y', h);

        List<Player> players = new ArrayList<>();
        players.add(p0);
        players.add(p1);

        new Game(players);
    }

    /**
     * this method tests if the AI method "choose" works correct.
     * TODO: The input String 6 still needs to be tested
     */
    @Test
    void choose() {
        String [] input = {"Are you ready to play?", "Are you done with your turn?", "Do you want to proceed with your action?", "Do you want to play your goblin-special?"};
        String input4 = "Do you want to play a card?";
        String input5 = "Do you want to trash one from your hand?";
        String input7 = "Do you want to draw your card from one of the discarding piles?";
        for (int i = 0; i < 4; i++) {
            assertTrue(ai.choose(input[i]));
        }

        try {
            p0.getPlayedCards('r').add(new Card(3, 'r'));
            p0.getGame().getDiscardPile('r').add(new Card(4, 'r'));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        p0.getHand().clear();
        p0.getHand().add(new Card(0, 'b' ));
        assertTrue(ai.choose(input7));

        // initializing some played cards
        try {
            p0.getPlayedCards('r').add(new Card(6, 'r'));
            p0.getPlayedCards('o').add(new Card(3, 'o'));
            p0.getPlayedCards('g').add(new Card(3, 'g'));
            p0.getPlayedCards('b').add(new Card(1, 'b'));
            p0.getPlayedCards('p').add(new Card(2, 'p'));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // creating a scenario where all hand cards are playable
        p0.getHand().clear();
        p0.getHand().add(new Card(7, 'r'));
        p0.getHand().add(new Card(4, 'o'));
        p0.getHand().add(new Card(4, 'g'));
        p0.getHand().add(new Card(3, 'b'));
        p0.getHand().add(new Card(3, 'p'));
        //playable cards need to be greater than 2
        assertTrue(ai.choose(input4));

        // creating a scenario where no hand cards are playable
        p0.getHand().clear();
        p0.getHand().add(new Card(5, 'r'));
        p0.getHand().add(new Card(4, 'o'));
        p0.getHand().add(new Card(4, 'g'));
        p0.getHand().add(new Card(3, 'b'));
        //playable cards need to be less than 3
        assertFalse(ai.choose(input5));

    }
    /**
     * this method tests if the AI method "bestHandCardToTrash" works correct
     */
    @Test
    void bestHandCardToTrash() {
        // initializing some played cards
        try {
            p0.getPlayedCards('r').add(new Card(3, 'r'));
            p0.getPlayedCards('r').add(new Card(6, 'r'));
            p0.getPlayedCards('o').add(new Card(3, 'o'));
            p0.getPlayedCards('g').add(new Card(3, 'g'));
            p0.getPlayedCards('b').add(new Card(1, 'b'));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        p0.getHand().clear();
        p0.getHand().add(new Card(4, 'o'));
        p0.getHand().add(new Card(4, 'g'));
        p0.getHand().add(new Card(3, 'b'));
        p0.getHand().add(new Card(5, 'r'));

        assertEquals(p0.getHand().get(3), ai.bestHandCardToTrash());
        // creates a set of played  and hand cards where every hand card fits
        p0.getPlayedCards('o').getAndRemoveTop();
        p0.getPlayedCards('g').getAndRemoveTop();
        p0.getPlayedCards('b').getAndRemoveTop();
        p0.getHand().clear();
        p0.getHand().add(new Card(0, 'r'));
        p0.getHand().add(new Card(2, 'r'));

        assertEquals(p0.getHand().get(0), ai.bestHandCardToTrash());

    }
    /**
     * this method tests if the AI method "choosePileColor" works correct
     */
    @Test
    void choosePileColor() {
        String input = "From which pile do you want to trash the top card?";
        String input1 = "From what colored pile do you want to draw?";
        // initializing a played card pile and a discard pile
        try {
            p0.getPlayedCards('r').add(new Card(3, 'r'));
            p0.getGame().getDiscardPile('r').add(new Card(4, 'r'));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // initializing a hand
        p0.getHand().clear();
        p0.getHand().add(new Card(0, 'b' ));
        // this method needs to run to set the chosenDiscard Variable
        ai.choose("Do you want to draw your card from one of the discarding piles?");
        assertEquals('r', ai.choosePileColor(input1));
        //clearing the played card piles
        p0.getPlayedCards('r').getAndRemoveTop();

        // initializing some played cards without directions
        try {
            p0.getPlayedCards('p').add(new Card(5, 'p'));
            p0.getPlayedCards('g').add(new Card(1, 'p'));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals('g', ai.choosePileColor(input));
        //clearing the played card piles
        p0.getPlayedCards('p').getAndRemoveTop();
        p0.getPlayedCards('g').getAndRemoveTop();

        // initializing some played cards with decreasing numbers
        try {
            p0.getPlayedCards('p').add(new Card(9, 'p'));
            p0.getPlayedCards('p').add(new Card(3, 'p'));
            p0.getPlayedCards('b').add(new Card(3, 'b'));
            p0.getPlayedCards('b').add(new Card(1, 'b'));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals('b', ai.choosePileColor(input));

        //clearing the played card piles
        boolean empty = false;
        while(!empty) {
            p0.getPlayedCards('p').getAndRemoveTop();
            p0.getPlayedCards('b').getAndRemoveTop();
            if (p0.getPlayedCards('p').isEmpty() && p0.getPlayedCards('b').isEmpty()){
                empty = true;
            }
        }
        // initializing some played cards with rising numbers
        try {
            p0.getPlayedCards('o').add(new Card(3, 'o'));
            p0.getPlayedCards('o').add(new Card(9, 'o'));
            p0.getPlayedCards('g').add(new Card(1, 'g'));
            p0.getPlayedCards('g').add(new Card(3, 'g'));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertEquals('o', ai.choosePileColor(input));
    }
    /**
     * this method tests if the AI method "chooseSpiralPosition" partially works correct.
     * It only checks for positions with no tokens, spirals, mirrors, skullpoints and wishing stones.
     */
    @Test
    void chooseSpiralPositionOne() throws Exception {
        // initializing tokens
        Token spiral = new Spiral();
        Token mirror = new Mirror();
        Token skullPoint4 = new Skullpoint(4);

        // checks if the AI makes the default action
        p0.getGame().getFields()[6].setToken(spiral);
        p0.getGame().getFields()[5].setToken(null);
        p0.getGame().getFields()[4].setToken(null);
        p0.getGame().getFields()[3].setToken(null);
        p0.getGame().getFields()[2].setToken(null);
        p0.getGame().getFields()[1].setToken(null);
        p0.placeFigure(p0.getGame().getFields()[1].getColor(), p0.getFigures().get(0));
        p0.placeFigure(p0.getGame().getFields()[6].getColor(), p0.getFigures().get(0));

        assertEquals(6, ai.chooseSpiralPosition("", 6));
        // checks if the AI chooses the wishing stone
        p0.getGame().getFields()[9].setToken(spiral);
        p0.getGame().getFields()[8].setToken(null);
        p0.placeFigure(p0.getGame().getFields()[5].getColor(), p0.getFigures().get(0));
        p0.placeFigure(p0.getGame().getFields()[9].getColor(), p0.getFigures().get(0));

        assertEquals(7, ai.chooseSpiralPosition("", 9));
        // checks if the AI chooses the spiral
        p0.getGame().getFields()[10].setToken(spiral);
        p0.getGame().getFields()[7].setToken(null);
        p0.placeFigure(p0.getGame().getFields()[7].getColor(), p0.getFigures().get(0));
        p0.placeFigure(p0.getGame().getFields()[10].getColor(), p0.getFigures().get(0));

        assertEquals(9, ai.chooseSpiralPosition("", 10));
        // checks if the AI chooses the skullpoint
        p0.getGame().getFields()[10].setToken(spiral);
        p0.getGame().getFields()[8].setToken(skullPoint4);
        p0.placeFigure(p0.getGame().getFields()[9].getColor(), p0.getFigures().get(0));
        p0.placeFigure(p0.getGame().getFields()[10].getColor(), p0.getFigures().get(0));

        assertEquals(8, ai.chooseSpiralPosition("", 10));
        // checks if the AI chooses the mirror
        p0.getGame().getFields()[4].setToken(mirror);
        for (int i = 0; i < 3; i++) {
            p0.increaseStoneAmount();
        }
        p0.getFigures().get(0).setPos(0);
        p0.placeFigure(p0.getGame().getFields()[1].getColor(), p0.getFigures().get(0));
        p0.placeFigure(p0.getGame().getFields()[6].getColor(), p0.getFigures().get(0));

        assertEquals(4, ai.chooseSpiralPosition("", 6));
    }

    /**
     * this method tests if the AI method "chooseSpiralPosition" works correct.
     * It only checks for positions with goblins and spiderwebs.
     */
    @Test
    void chooseSpiralPositionTwo() throws Exception {
        // initializing the tokens
        Token spiral = new Spiral();
        Token goblin = new Goblin();
        Token spiderweb = new Spiderweb();

        // checks if the AI chooses the goblin and the spiderweb
        p0.getGame().getFields()[4].setToken(null);
        p0.getGame().getFields()[5].setToken(null);
        p0.getGame().getFields()[6].setToken(null);
        p0.getGame().getFields()[7].setToken(null);
        p0.getGame().getFields()[9].setToken(spiral);
        p0.getGame().getFields()[8].setToken(spiderweb);
        p0.getGame().getFields()[13].setToken(goblin);
        p0.getGame().getFields()[11].setToken(null);
        p0.getGame().getFields()[12].setToken(null);
        p0.getGame().getFields()[14].setToken(null);
        p0.getGame().getFields()[15].setToken(null);
        p0.getGame().getFields()[16].setToken(spiral);

        p0.getFigures().get(1).setPos(13);
        p0.getFigures().get(0).setPos(11);
        p0.placeFigure(p0.getGame().getFields()[12].getColor(), p0.getFigures().get(0));
        p0.placeFigure(p0.getGame().getFields()[16].getColor(), p0.getFigures().get(0));

        assertEquals(13, ai.chooseSpiralPosition("", 16));
        p0.getGame().getFields()[8].setToken(spiderweb);
        p0.getFigures().get(2).setPos(5);
        p0.placeFigure(p0.getGame().getFields()[6].getColor(), p0.getFigures().get(2));
        p0.placeFigure(p0.getGame().getFields()[9].getColor(), p0.getFigures().get(2));

        assertEquals(8, ai.chooseSpiralPosition("", 9));
    }
    /**
     * this method tests if the AI method "chooseCard" works correct
     */
    @Test
    void chooseCard() throws Exception{
        p0.getHand().clear();
        p0.getHand().add(new Card(10, 'p'));
        p0.getHand().add(new Card(0, 'b'));
        p0.getPlayedCards('p').add(new Card(0, 'p'));
        p0.getPlayedCards('p').add(new Card(1, 'p'));
        p0.getGame().getFields()[1].setToken(null);

        assertEquals(p0.getHand().get(1), ai.chooseCard("What card do you want to play?", p0.getHand()));
        assertEquals(p0.getHand().get(0), ai.chooseCard("What card do you want to trash?", p0.getHand()));
        assertEquals(p0.getHand().get(0), ai.chooseCard("", p0.getHand()));

    }

}
