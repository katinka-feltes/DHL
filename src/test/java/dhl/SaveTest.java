package dhl;

import dhl.model.Game;
import dhl.model.Player;
import dhl.player_logic.Human;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * tests the class save
 */
class SaveTest {

    @Test
    public void save() throws Exception {
        Game previouslySaved = Save.serializeDataIn();

        // create game
        List<Player> players = new ArrayList<>();
        players.add(new Player("test1", 'v', new Human()));
        players.add(new Player("test2", 'b', new Human()));
        Game game = new Game(players);

        game.moveOracle(5);
        Save.serializeDataOut(game);
        Game justSaved = Save.serializeDataIn();
        assertEquals(5, justSaved.getOracle());

        if(previouslySaved != null){
            Save.serializeDataOut(previouslySaved);
        }
    }

}