package dhl.model.tokens;

public interface Token {
    /**
     * @return true if the token will be collected, false otherwise
     */
    boolean collectable();

    /**
     * @return the token's name as a string
     */
    String getName();

    /**
     * executes the token's action
     */
    void action();
}
