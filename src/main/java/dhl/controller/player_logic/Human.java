package dhl.controller.player_logic;

import dhl.model.Card;
import dhl.view.View;

import java.util.List;

/**
 * The Logic for a Human
 */
public class Human implements PlayerLogic{

    private View view;

    /**
     * Creates a human playerlogic
     * @param view the input for the logic
     */
    public Human (View view){
        this.view=view;
    }

    @Override
    public boolean choose(String question) {
        return view.promptPlayersChoice(question);
    }

    @Override
    public String chooseCard(String question, List<Card> hand) {
        return view.promptCardString(question);
    }

    @Override
    public int chooseFigure(int start, int end, String question) {
        return view.promptInt(start, end, question);
    }
}
