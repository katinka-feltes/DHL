package dhl.model.tokens;

import dhl.model.Card;
import dhl.model.Figure;
import dhl.model.Game;
import dhl.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests all the token classes
 */
public class TokenTest {

    private Token goblin, mirror, skull, spider, spiral, stone;
    private Game game;
    private Player player;

    @BeforeEach
    /**
     * creates one of each tokens and a game with 2 players
     */
    public void setup(){
        goblin = new Goblin();
        mirror = new Mirror();
        skull = new Skullpoint(1);
        spider = new Spiderweb();
        spiral = new Spiral();
        stone = new WishingStone();

        game = new Game(new String[]{"Player 1", "Player 2"});
        player = game.getPlayers().get(0);
    }

    @Test
    /**
     * tests if method isCollectable works for collectable tokens
     */
    public void isCollectable() {
        assertTrue(mirror.isCollectable());
        assertTrue(stone.isCollectable());
    }

    @Test
    /**
     * tests if method isCollectable works for not-collectable tokens
     */
    public void isNotCollectable() {
        assertFalse(goblin.isCollectable());
        assertFalse(skull.isCollectable());
        assertFalse(spider.isCollectable());
        assertFalse(spiral.isCollectable());
    }

    @Test
    /**
     * tests if method getName works
     */
    public void getName() {
        assertEquals("Goblin", goblin.getName());
        assertEquals("Mirror", mirror.getName());
        assertEquals("Skullpoint", skull.getName());
        assertEquals("Spiderweb", spider.getName());
        assertEquals("WishingStone", stone.getName());
    }

    @Test
    /**
     * tests if method getSymbol works
     */
    public void getSymbol() {
        assertEquals('G', goblin.getSymbol());
        assertEquals('M', mirror.getSymbol());
        assertEquals('\u2620', skull.getSymbol());
        assertEquals('\u25A9', spider.getSymbol());
        assertEquals('\u058E', spiral.getSymbol());
    }

    @Test
    /**
     * tests if action of collectable tokens (mirror + wishingstone) works
     */
    public void actionCollectables(){
        // test mirror action
        assertEquals(0,player.getTokens().size());
        mirror.action(player);
        assertEquals(1,player.getTokens().size());
        assertEquals(mirror, player.getTokens().get(0));
        // test wishingstone
        stone.action(player);
        assertEquals(2,player.getTokens().size());
        assertEquals(stone, player.getTokens().get(1));
    }

    @Test
    /**
     * tests if action of skullpoint token works
     */
    public void actionSkullpoints(){
        assertEquals(0, player.getVictoryPoints());
        skull.action(player);
        assertEquals(1, player.getVictoryPoints());
    }

    @Test
    /**
     * tests if action of spiderweb works
     */
    public void actionSpiderweb(){
        Figure figure1 = player.getFigures().get(0);
        Figure figure2 = player.getFigures().get(1);
        //move both figures to the first red field
        player.placeFigure('r', figure1);
        player.placeFigure('r', figure2);
        //move both figures to the next red field ny spider and normal
        spider.action(player); //last moved fig (2) is moved
        player.placeFigure('r', figure1);
        assertEquals(figure1.getPos(), figure2.getPos());
    }

    @Test
    /**
     * tests if action of spiral works
     */
    public void actionSpiral(){
        Figure figure = player.getFigures().get(0);
        player.placeFigure('p', figure); // move figure to the field with index 1
        player.placeFigure('b', figure);// move figure to the field with index 5
        ((Spiral)spiral).setChosenPos(1); // not allowed so no movement
        spiral.action(player);
        assertEquals(5, figure.getPos());
        ((Spiral)spiral).setChosenPos(3); // move back there
        spiral.action(player);
        assertEquals(3, figure.getPos());
    }

    @Test
    /**
     * tests if action of goblin works
     * too many asserts because we need to test all colors
     */
    public void actionGoblin() throws Exception{

        goblin.action(player); // nothing should happen if no pile is set

        ((Goblin) goblin).setPileChoice('r');
        goblin.action(player); //wrong choice as pile is empty should change nothing
        player.getPlayedCards('r').add(new Card(2, 'r'));
        goblin.action(player);
        assertEquals(1, game.getDiscardPile('r').getPile().size());
        assertEquals(0, player.getPlayedCards('r').getPile().size());

        ((Goblin) goblin).setPileChoice('g');
        player.getPlayedCards('g').add(new Card(2, 'g'));
        goblin.action(player);
        assertEquals(1, game.getDiscardPile('g').getPile().size());

        ((Goblin) goblin).setPileChoice('b');
        player.getPlayedCards('b').add(new Card(2, 'b'));
        goblin.action(player);
        assertEquals(1, game.getDiscardPile('b').getPile().size());

        ((Goblin) goblin).setPileChoice('p');
        player.getPlayedCards('p').add(new Card(2, 'p'));
        goblin.action(player);
        assertEquals(1, game.getDiscardPile('p').getPile().size());

        ((Goblin) goblin).setPileChoice('o');
        player.getPlayedCards('o').add(new Card(2, 'o'));
        goblin.action(player);
        assertEquals(1, game.getDiscardPile('o').getPile().size());

        ((Goblin) goblin).setPileChoice('h');
        Card card = new Card (5, 'p');
        player.getHand().add(card);
        ((Goblin) goblin).setCardChoice(card);
        goblin.action(player);
        assertEquals(0, player.getHand().size());
        assertEquals(card, game.getDiscardPile('p').getTop());
    }

    @Test
    /**
     * tests if method setPileChoice works
     */
    public void setPileChoice() {
        ((Goblin) goblin).setPileChoice('r');
        assertEquals('r', ((Goblin) goblin).getPileChoice());
    }
}