package dhl.model.tokens;

import dhl.model.Card;
import dhl.model.Game;
import dhl.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GoblinTest {

    private Token token;

    private Player player1;
    private Game game;

    @BeforeEach
    public void setup() {
        token = new Goblin();
        game = new Game(new String[]{"Test", "Test"});
        player1 = game.getPlayers().get(0);
    }

    @Test
    public void isCollectable() {
        assertFalse(token.isCollectable());
    }

    @Test
    public void getName() {
        assertEquals("Goblin", token.getName());
    }

    @Test
    public void action() throws Exception {
        actionForPile('r');
        actionForPile('g');
        actionForPile('b');
        actionForPile('p');
        actionForPile('o');
    }

    private void actionForPile(char pile) throws Exception {
        ((Goblin)token).setPileChoice(pile); //Set players choice
        switch (pile){
            case ('r'):
                player1.getPlayedCardsRed().add(new Card(2, pile));
                token.action(player1);
                assertEquals(1, game.getDiscardingPileRed().getPile().size());
                break;
            case ('g'):
                player1.getPlayedCardsGreen().add(new Card(2, pile));
                token.action(player1);
                assertEquals(1, game.getDiscardingPileGreen().getPile().size());
                break;
            case ('b'):
                player1.getPlayedCardsBlue().add(new Card(2, pile));
                token.action(player1);
                assertEquals(1, game.getDiscardingPileBlue().getPile().size());
                break;
            case ('p'):
                player1.getPlayedCardsPurple().add(new Card(2, pile));
                token.action(player1);
                assertEquals(1, game.getDiscardingPilePurple().getPile().size());
                break;
            case ('o'):
                player1.getPlayedCardsOrange().add(new Card(2, pile));
                token.action(player1);
                assertEquals(1, game.getDiscardingPileOrange().getPile().size());
                break;
        }
    }

    @Test
    public void setPileChoice() {
        ((Goblin)token).setPileChoice('r');
        assertEquals('r', ((Goblin) token).getPileChoice());
    }
}