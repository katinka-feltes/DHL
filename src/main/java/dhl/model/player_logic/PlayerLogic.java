package dhl.model.player_logic;

/**
 * An attribute of a Player (Human or AI) that determines how an action is executed
 */
public interface PlayerLogic {
    //TODO: Not always the same choice

    /***
     * asks the logic to confirm the question asked
     * @param question the question to confirm
     */
    void confirm(String question); //Computer always yes

    /**
     * asks the logic to choose a card
     * @return the chosen card as a string
     */
    String chooseCard();

    /**
     * asks the logic to choose a figure
     * @return the chosen figure as an int corresponding to the position on the playingfield. 1 being the furthest away
     * from the start.
     */
    int chooseFigure();
}
