package dhl.controller.player_logic;

import dhl.model.*;
import dhl.model.tokens.Skullpoint;
import dhl.model.tokens.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * The logic for the AI agent
 */
public class AI implements PlayerLogic {

    Card chosenCard;
    Figure chosenFigure;
    Player self;

    @Override
    public boolean choose(String question) {
        if (question.endsWith("Are you ready to play?")) {
            return true;
        } else if (question.equals("Are you done with your turn?")) {
            return true;
        } else if (question.startsWith("Do you want to play a card?")) {
            return playableCards().size() > 2; // play a card if there are more than two cards to play
        } else if (question.startsWith("Do you want to draw your card from one of the discarding piles?")) {
            return false; // always draws from the drawing pile
        } else if (question.startsWith("Your Figure gets moved to figure to the next field with the same color")){
            return true; // always use spiderweb
        } else if (question.endsWith("Do you want to proceed with your action?")) {
            return false; // don't use all other token because they would need further choices //TODO
        } else if (question.startsWith("Do you want to trash one from your hand?")) {
            return playableCards().size() <= 4; // trash a hand card if at least 4 of them cannot be played
        } else if (question.startsWith("Do you want to play your goblin-special?")) {
            return true; // always play if it is possible, because it doesn't occur often
        } else {
            return false;
        }
    }

    @Override
    public Card chooseCard(String question, List<Card> hand) {
        if (question.equals("What card do you want to play?")) {
            calculateNextMove();
            return chosenCard;
        } else if (question.equals("What card do you want to trash?")) {
            return bestHandCardToTrash();
        }
        return hand.get(0);
    }

    /**
     * checks which figure moves the furthest according to the color of the played card
     * @param question question to display if it was a human
     * @param figures the figures to choose from
     * @return the figure that will move the furthest with the last played card
     */
    @Override
    public Figure chooseFigure(String question, List<Figure> figures) {
        return chosenFigure;
    }

    /**
     * @param question the reason to choose the pile
     * @return
     */
    @Override
    public char choosePileColor(String question) {
        if (question.equals("")) {

        } else if (question.equals("From what colored pile do you want to draw?")) {
            // never occurs yet
        }
        return 'r';
    }

    /**
     * checks which playable hand card is the best option according to the AI's played cards
     */
    private void calculateNextMove(){
        int bestOption = -100; //best calculated points by combination of figure and card
        for (Figure figure : self.getFigures()){ //for every figure
            for (Card card : self.getHand()) { //for every card from hand
                int steps = steps(figure, card.getColor()); //how far the figure would move with the current card TODO: dont leave the field
                int pointDifference = Game.FIELDS[figure.getPos()+steps].getPoints() - Game.FIELDS[figure.getPos()].getPoints();

                int points = steps + pointDifference; //steps plus the gained points due to the steps
                points -= difference(card, self.getPlayedCards(card.getColor())); // plus how good the card would fit to fit the played cards pile
                points += tokenWorth(Game.FIELDS[figure.getPos()+steps].getToken()); // plus how good the token on the field is
                if (points > bestOption) { // if combo of figure and card is better than the current one
                    chosenCard = card;
                    chosenFigure = figure;
                    bestOption = points;
                }
            }
        }
    }

    /**
     * calculates how far the given figure would move to the given color
     * @param f the figure to move
     * @param color the color to move to
     * @return the amount of steps the figure would make (int)
     */
    private int steps(Figure f, char color){
        int steps = 1;
        while (Game.FIELDS[f.getPos() + steps].getColor() != color) {
            steps++;
        }
        return steps;
    }

    /**
     * the smaller the difference between last played card and (playable) hand card, the better
     * if the pile is empty, the card is better the higher/lower it is
     *
     * @param card the card to see the difference with
     * @param pile the correctly colored pile to put the card on
     * @return the difference from top card of the pile to the new one
     */
    private int difference (Card card, DirectionDiscardPile pile){
        if(CardFunction.cardFitsToPlayersPiles(card, pile)) {
            if(!pile.isEmpty()) {
                return Math.abs(pile.getTop().getNumber() - card.getNumber());
            } else if(card.getNumber() > 5) { //if pile is empty and newNumber is high
                return 10-card.getNumber();
            } else if(card.getNumber() <= 5) {
                return card.getNumber(); //if pile is empty and newNumber is low
            }
        }
        return 100; //if card doesn't fit
    }

    private int tokenWorth(Token token){
        if (token != null) {
            switch (token.getName()) {
                case "Goblin":
                    return 3; //TODO: more complex?
                case "Mirror":
                    return self.calcTokenPoints(); // return current token points because they would be doubled
                case "Skullpoint":
                    return ((Skullpoint) token).getPoints();
                case "Spiderweb":
                    return 4; // on average, you move about 4 steps forward
                case "Spiral":
                    return 1;
                case "WishingStone":
                    return 3; // on average 2,8 points //TODO: more complex? * amount mirrors
            }
        }
        return 0;
    }

    private Card bestHandCardToTrash () {
        List<Card> notPlayable = new ArrayList<>(self.getHand());
        notPlayable.removeAll(playableCards());
        if(!notPlayable.isEmpty()){
            return notPlayable.get(0);  //TODO: second factor to choose ?
        }
        return self.getHand().get(0); //why would it trash if all cards fit
    }

    /**
     * checks what cards from hand could be played
     * @return the cards in a list
     */
    private List<Card> playableCards () {
        List<Card> playable = new ArrayList<>();
        for (Card c : self.getHand()){
            if (CardFunction.cardFitsToPlayersPiles(c, self.getPlayedCards(c.getColor()))){
                playable.add(c);
            }
        }
        return playable;
    }

    public void setSelf(Player self) {
        this.self = self;
    }

}