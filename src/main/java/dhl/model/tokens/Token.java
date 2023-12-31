package dhl.model.tokens;

import dhl.model.Player;

import java.io.Serializable;

/**
 * This Interface provides the basic functionality of a Token.
 */
public interface Token extends Serializable{

    /**
     * @return true if the token will be collected, false otherwise
     */
    boolean isCollectable();

    /**
     * @return the token's name as a string
     */
    String getName();

    /**
     * executes the token's action
     * @param player the player that will execute the action
     */
    void action(Player player);

    /**
     * gets the symbol of the Token
     * @return the symbol of the token
     */
    char getSymbol();

}

