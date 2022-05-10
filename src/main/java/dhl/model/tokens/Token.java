package dhl.model.tokens;

import dhl.model.Player;

public interface Token {

    // symbol

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
     */
    void action(Player player);

    char getSymbol();

}

