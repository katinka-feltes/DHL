package dhl.model.tokens;

import dhl.Constants;
import dhl.model.Figure;
import dhl.model.Player;

/**
 * This Class represents a Spiderweb Token. If you collect it you are allowed to move to the next field of the color of the Spiderweb.
 */
public class Spiderweb implements Token {

    public static final char SYMBOL = '\u25A9';

    /**
     * says if the token is collectable
     * @return true if the token will be collected, false otherwise
     */
    @Override
    public boolean isCollectable() {
        return Constants.singleUseToken;
    }

    @Override
    public String getName() {
        String[] temp = this.getClass().toString().split("\\.");
        return temp[temp.length-1];
    }

    /**
     * the last moved figure of the player is moved to the next field with thew same color it stands on.
     *
     * The CatchBlock is necessary for the case that the spiderweb would move the figure too far
     * that case can be simply ignored. Another options would be to throw the Exception here and catch it elsewhere,
     * but then the interface would need to throw the exception as well. Thus, it is easier to ignore the rule once.
     *
     * @param player the player that will execute the action
     */
    @SuppressWarnings("PMD.EmptyCatchBlock")
    @Override
    public void action(Player player) {
        Figure lastMoved = player.getLastMovedFigure();
        try {
            player.placeFigure(player.getGame().getFields()[lastMoved.getPos()].getColor(), lastMoved);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public char getSymbol() {
        return SYMBOL;
    }
}