package dhl.controller.player_logic;

import dhl.model.Card;
import dhl.model.CardFunction;
import dhl.model.Figure;
import dhl.model.FigureFunction;
import dhl.view.View;

import java.util.List;

/**
 * The Logic for a Human
 */
public class Human implements PlayerLogic {

    private View view;

    /**
     * Creates a human playerlogic
     *
     * @param view the input for the logic
     */
    public Human(View view) {
        this.view = view;
    }

    @Override
    public int chooseSpiralPosition(String question, int position, int stonesAmount) {
        return view.promptInt(0, position, question);
    }

    @Override
    public boolean choose(String question) {
        return view.promptPlayersChoice(question);
    }

    @Override
    public Card chooseCard(String question, List<Card> hand) {
        try {
            String cardAsString = view.promptCardString(question);
            return CardFunction.getCardFromHand(cardAsString, hand);
        } catch (Exception e) {
            view.error(e.getMessage());
            return chooseCard(question, hand);
        }
    }

    @Override
    public Figure chooseFigure(String question, List<Figure> figures) {
        int figurePos = view.promptInt(1, 3, question);
        return FigureFunction.getFigureByPos(figurePos, figures);
    }

    /**
     * @param question the reason to choose the pile
     * @return the chat the human enters
     */
    @Override
    public char choosePileColor(String question) {
        return view.promptColor(question);
    }

}
