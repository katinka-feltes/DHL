package dhl.model.tokens;

import dhl.model.Game;
import dhl.model.Player;

public class Skullpoints implements Token{

    private int points;

    public Skullpoints(int points){
        this.points = points;
    }

    @Override
    public boolean isCollectable() {
        return false;
    }

    @Override
    public String getName() {
        String[] temp = this.getClass().toString().split("\\.");
        return temp[temp.length-1];
    }

    /**the skullpoints are added to the current players' victory points
     * the counting stones position gets updated
     *
     * @param game the current game status
     * @param player the current player
     */
    @Override
    public void action(Game game, Player player) {
        player.setVictoryPoints(player.getVictoryPoints() + points);
        //game.counting stone...update()
    }

    public int getPoints(){
        return points;
    }
}
