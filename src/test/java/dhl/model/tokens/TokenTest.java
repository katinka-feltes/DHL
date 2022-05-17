package dhl.model.tokens;

import dhl.model.Card;
import dhl.model.Figure;
import dhl.model.Game;
import dhl.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {

    private Token goblin, mirror, skull, spider, spiral, stone;
    private Game game;
    private Player player;

    @BeforeEach
    public void setup(){
        goblin = new Goblin();
        mirror = new Mirror();
        skull = new Skullpoints(1);
        spider = new Spiderweb();
        spiral = new Spiral();
        stone = new WishingStone();

        game = new Game(new String[]{"Player 1", "Player 2"});
        player = game.getPlayers().get(0);
    }

    @Test
    public void isCollectable() {
        assertFalse(goblin.isCollectable());
        assertTrue(mirror.isCollectable());
        assertFalse(skull.isCollectable());
        assertFalse(spider.isCollectable());
        assertFalse(spiral.isCollectable());
        assertTrue(stone.isCollectable());
    }

    @Test
    public void getName() {
        assertEquals("Goblin", goblin.getName());
        assertEquals("Mirror", mirror.getName());
        assertEquals("Skullpoints", skull.getName());
        assertEquals("Spiderweb", spider.getName());
        assertEquals("Spiral", spiral.getName());
        assertEquals("WishingStone", stone.getName());
    }

    @Test
    public void getSymbol() {
        assertEquals('G', goblin.getSymbol());
        assertEquals('M', mirror.getSymbol());
        assertEquals('\u2620', skull.getSymbol());
        assertEquals('\u25A9', spider.getSymbol());
        assertEquals('\u058E', spiral.getSymbol());
        assertEquals('W', stone.getSymbol());
    }

    @Test
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
    public void actionSkullpoints(){
        assertEquals(0, player.getVictoryPoints());
        skull.action(player);
        assertEquals(1, player.getVictoryPoints());
    }

    @Test
    public void actionSpiderweb(){
        Figure figure1 = player.getFigures()[0];
        Figure figure2 = player.getFigures()[1];
        //move both figures to the first red field
        player.placeFigure('r', figure1);
        player.placeFigure('r', figure2);
        //move both figures to the next red field ny spider and normal
        spider.action(player); //last moved fig (2) is moved
        player.placeFigure('r', figure1);
        assertEquals(figure1.getPos(), figure2.getPos());
    }

    @Test
    public void actionSpiral(){
        Figure figure = player.getFigures()[0];
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
    public void actionGoblin() throws Exception{

        goblin.action(player); // nothing should happen if no pile is set

        ((Goblin) goblin).setPileChoice('r');
        goblin.action(player); //wrong choice as pile is empty should change nothing
        player.getPlayedCardsRed().add(new Card(2, 'r'));
        goblin.action(player);
        assertEquals(1, game.getDiscardingPileRed().getPile().size());
        assertEquals(0, player.getPlayedCardsRed().getPile().size());

        ((Goblin) goblin).setPileChoice('g');
        player.getPlayedCardsGreen().add(new Card(2, 'g'));
        goblin.action(player);
        assertEquals(1, game.getDiscardingPileGreen().getPile().size());

        ((Goblin) goblin).setPileChoice('b');
        player.getPlayedCardsBlue().add(new Card(2, 'b'));
        goblin.action(player);
        assertEquals(1, game.getDiscardingPileBlue().getPile().size());

        ((Goblin) goblin).setPileChoice('p');
        player.getPlayedCardsPurple().add(new Card(2, 'p'));
        goblin.action(player);
        assertEquals(1, game.getDiscardingPilePurple().getPile().size());

        ((Goblin) goblin).setPileChoice('o');
        player.getPlayedCardsOrange().add(new Card(2, 'o'));
        goblin.action(player);
        assertEquals(1, game.getDiscardingPileOrange().getPile().size());

        ((Goblin) goblin).setPileChoice('h');
        Card card = new Card (5, 'p');
        player.getHand().add(card);
        ((Goblin) goblin).setCardChoice(card);
        goblin.action(player);
        assertEquals(0, player.getHand().size());
        assertEquals(card, game.getDiscardingPilePurple().getTop());
    }
    @Test
    public void setPileChoice() {
        ((Goblin) goblin).setPileChoice('r');
        assertEquals('r', ((Goblin) goblin).getPileChoice());
    }
}