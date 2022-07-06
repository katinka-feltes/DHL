package dhl.model.tokens;

import dhl.controller.player_logic.Human;
import dhl.model.Card;
import dhl.model.Figure;
import dhl.model.Game;
import dhl.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests all the token classes
 */
public class TokenTest {

    private Token goblin, mirror, skull, spider, spiral, stone;
    private Game game;
    private Player player;

    /**
     * creates one of each tokens and a game with 2 players
     */
    @BeforeEach
    public void setup(){
        goblin = new Goblin();
        mirror = new Mirror();
        skull = new Skullpoint(1);
        spider = new Spiderweb();
        spiral = new Spiral();
        stone = new WishingStone();

        List<Player> players = new ArrayList<>();
        players.add(new Player("test1", 'v', new Human()));
        players.add(new Player("test2", 'b', new Human()));
        game = new Game(players);

        player = game.getPlayers().get(0);
    }

    /**
     * tests if method isCollectable works for collectable tokens
     */
    @Test
    public void isCollectable() {
        assertTrue(mirror.isCollectable());
        assertTrue(stone.isCollectable());
    }

    /**
     * tests if method isCollectable works for not-collectable tokens
     */
    @Test
    public void isNotCollectable() {
        assertFalse(goblin.isCollectable());
        assertFalse(skull.isCollectable());
        assertFalse(spider.isCollectable());
        assertFalse(spiral.isCollectable());
    }

    /**
     * tests if method getName works
     */
    @Test
    public void getName() {
        assertEquals("Goblin", goblin.getName());
        assertEquals("Mirror", mirror.getName());
        assertEquals("Skullpoint", skull.getName());
        assertEquals("Spiderweb", spider.getName());
        assertEquals("WishingStone", stone.getName());
    }

    /**
     * tests if method getSymbol works
     */
    @Test
    public void getSymbol() {
        assertEquals('G', goblin.getSymbol());
        assertEquals('M', mirror.getSymbol());
        assertEquals('\u2620', skull.getSymbol());
        assertEquals('\u25A9', spider.getSymbol());
        assertEquals('\u058E', spiral.getSymbol());
    }

    /**
     * tests if action of collectable tokens (mirror + wishingstone) works
     */
    @Test
    public void actionCollectables(){
        // test mirror action
        assertEquals(0,player.getTokens()[0]);
        assertEquals(0,player.getTokens()[1]);
        mirror.action(player);
        assertEquals(1,player.getTokens()[1]);
        // test wishing stone
        stone.action(player);
        assertEquals(1,player.getTokens()[0]);
    }

    /**
     * tests if action of skullpoint token works
     */
    @Test
    public void actionSkullpoints(){
        assertEquals(0, player.getVictoryPoints());
        skull.action(player);
        assertEquals(1, player.getVictoryPoints());
    }

    /**
     * tests if action of spiderweb works
     */
    @Test
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

    /**
     * tests if action of spiral works
     */
    @Test
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

    /**
     * tests if action of goblin works when player chooses a pile
     */
    @Test
    public void goblinActionChosePile() throws Exception{

        goblin.action(player); // nothing should happen if no pile is set

        ((Goblin) goblin).setPileChoice('r');
        goblin.action(player); //wrong choice as pile is empty should change nothing
        player.getPlayedCards('r').add(new Card(2, 'r'));
        goblin.action(player);
        assertEquals(1, game.getDiscardPile('r').getPile().size());
        // assertEquals(0, player.getPlayedCards('r').getPile().size());

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
    }

    /**
     * tests the Goblin if Hand was chosen
     */
    @Test
    public void goblinActionChoseHand() {
        player.getHand().clear();
        ((Goblin) goblin).setPileChoice('h');
        Card card = new Card (5, 'p');
        player.getHand().add(card);
        ((Goblin) goblin).setCardChoice(card);
        goblin.action(player);
        assertEquals(0, player.getHand().size());
        assertEquals(card, game.getDiscardPile('p').getTop());
    }

    /**
     * tests if method setPileChoice works
     */
    @Test
    public void setPileChoice() {
        ((Goblin) goblin).setPileChoice('r');
        assertEquals('r', ((Goblin) goblin).getPileChoice());
    }
}