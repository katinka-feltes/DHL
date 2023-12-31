package dhl.player_logic;

import dhl.model.Card;
import dhl.model.CardFunction;
import dhl.model.Figure;
import dhl.model.FigureFunction;
import dhl.view.Cli;

import java.util.List;

/**
 * The Logic for a Human
 */
public class Human implements PlayerLogic {

    private Cli view = new Cli();

    @Override
    public int chooseSpiralPosition(String question, int position) {
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

    @Override
    public int chooseOracleSteps(String question, int oracleNumber) {
        return view.promptInt(1, oracleNumber, question);
    }

    @Override
    public char choosePileColor(String question) {
        return view.promptColor(question);
    }
}
