package dhl.model.player_logic;

import dhl.view.View;

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
    public void confirm(String question) {
        while (true) {
            if (view.promptPlayersChoice(question)) {
                break;
            }
        }
    }

    @Override
    public String chooseCard() {
        return null;
    }

    @Override
    public int chooseFigure() {
        return 0;
    }
}
