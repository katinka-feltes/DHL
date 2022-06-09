package dhl.controller.player_logic;

import dhl.model.Card;
import dhl.model.Figure;

import java.util.List;

/**
 * An attribute of a Player (Human or AI) that determines how an action is executed
 */
public interface PlayerLogic {
    //TODO: Not always the same choice

    /**
     * asks the logic to choose (yes or no) for the question asked
     * @param question the question to confirm
     */
    boolean choose(String question); //Computer always yes

    /**
     * asks the logic to choose a card
     * @return the chosen card as a string
     */
    Card chooseCard(String question, List<Card> hand);

    /**
     * asks the logic to choose a figure
     * @return the chosen figure as an int corresponding to the position on the playingfield. 1 being the furthest away
     * from the start.
     */
    Figure chooseFigure(String question, List<Figure> figures);

    /**
     * asks the logic to choose a pile
     * @param question the reason to choose the pile
     * @return a teh color of the chosen pile as a char
     */
    char choosePileColor (String question);
}