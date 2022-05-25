package dhl.controller.player_logic;

import dhl.model.Card;

import java.util.List;

/**
 * The logic for the AI agent
 */
public class AI implements PlayerLogic {

    @Override
    public boolean choose(String question) {
        if (question.endsWith("Are you ready to play?")) {
            return true;
        } else if (question.equals("Are you done with your turn?")) {
            return true;
        } else if (question.startsWith("Do you want to play a card?")) {
            return true;
        } else if (question.startsWith("Do you want to draw your card from one of the discarding piles?")) {
            return false;
        } else if (question.endsWith("Do you want to proceed with your action?")) {
            return false;
        } else if (question.startsWith("Do you want to trash one from your hand?")) {
            return true;
        } else if (question.startsWith("Do you want to play your goblin-special?")) {
            return true;
        } else {
            return true;
        }
    }

    @Override
    public Card chooseCard(String question, List<Card> hand) {
        if (question.equals("What card do you want to play?")) {
            return hand.get(0);
        } else if (question.equals("What card do you want to trash?")) {
            return hand.get(0);
        } else {
            return hand.get(0);
        }
    }

    @Override
    public int chooseFigure(int start, int end, String question) {
        //which figure he wants to move
        return 1;
    }
}
