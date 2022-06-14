package dhl.controller.player_logic;

import dhl.model.Card;
import dhl.model.Figure;

import java.util.List;

/**
 * An attribute of a Player (Human or AI) that determines how an action is executed
 */
public interface PlayerLogic {

    /**
     * asks the logic to choose which field figure should be moved to (spiral action)
     * @param question the asked question
     * @param position the current position of the figure
     * @return int position the figure should move to
     */
    int chooseSpiralPosition(String question, int position);

    /**
     * asks the logic to choose (yes or no) for the question asked
     * @param question the question to confirm
     * @return true if the logic chooses so
     */
    boolean choose(String question);

    /**
     * asks the logic to choose a card
     * @param question The reason to choose a card
     * @param hand the hand to choose a card from
     * @return the chosen card as a string
     */
    Card chooseCard(String question, List<Card> hand);

    /**
     * asks the logic to choose a figure
     * @param question the reason to choose the figure
     * @param figures the figures to choose from
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