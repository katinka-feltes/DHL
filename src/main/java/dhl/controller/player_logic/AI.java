package dhl.controller.player_logic;

import dhl.model.*;
import dhl.model.tokens.Mirror;
import dhl.model.tokens.Skullpoint;
import dhl.model.tokens.Token;
import dhl.model.tokens.WishingStone;

import java.util.ArrayList;
import java.util.List;

import static dhl.model.Game.FIELDS;

/**
 * The logic for the AI agent
 */
public class AI implements PlayerLogic {

    Card chosenCard;
    Card chosenCardOracle;
    Figure chosenFigure;
    boolean playOracle;
    int oracleSteps;
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
        } else if (question.endsWith("Do you want to proceed with your action?")) {
            return true; // always wants to proceed TODO: spiral no if spiral is on first field or so
        } else if (question.startsWith("Do you want to trash one from your hand?")) {
            return playableCards().size() < 3; // trash a hand card if only 2 of them be played
        } else if (question.startsWith("Do you want to move a figure?")) { // true = figure; false = oracle
            return chooseIfOracle();
        } else {
            return question.startsWith("Do you want to play your goblin-special?");// always play if it is possible, because it doesn't occur often
        }
    }

    @Override
    public int chooseSpiralPosition(String question, int position) {

        int stonesAmount = self.getTokens()[0];

        int chosenPosition = position;
        int originalPosition = self.getLastMovedFigure().getLatestPos();
        if(stonesAmount < 3) {
            for(int pos = position; pos > position - 5; pos--) {
                if(FIELDS[pos].getToken() instanceof WishingStone && pos != originalPosition) {
                    chosenPosition = pos;
                }
            }
        } else {
            for(int pos = position; pos > 0; pos--) {
                if(FIELDS[pos].getToken() instanceof Mirror && pos != originalPosition) {
                    chosenPosition = pos;
                }
            }
        }
        while(chosenPosition == position || chosenPosition == originalPosition) {
            chosenPosition--;
        }
        return chosenPosition;
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
     * decides if moving figure or moving oracle is smarter
     * @return true if figure should be moved, false if oracle
     */
    private boolean chooseIfOracle() {
        return !playOracle;
    }

    /**
     * checks which figure moves the furthest according to the color of the played card
     *
     * @param question question to display if it was a human
     * @param figures  the figures to choose from
     * @return the figure that will move the furthest with the last played card
     */
    @Override
    public Figure chooseFigure(String question, List<Figure> figures) {
        return chosenFigure;
    }

    @Override
    public int chooseOracleSteps(String question, int oracleNumber) {
        return oracleSteps;
    }

    /**
     * @param question the reason to choose the pile
     * @return char color of the chosen pile
     */
    @Override
    public char choosePileColor(String question) {
        if (question.equals("From which pile do you want to trash the top card?")) {
            char chosenPile = '\u0000'; // set to default value
            int worstDiff = 0;
            for(DirectionDiscardPile pile: self.getPlayedCards()) {
                if(pile.getDirection() == 1 && pile.getTop().getNumber() > worstDiff) {
                    chosenPile = pile.getColor();
                    worstDiff = pile.getTop().getNumber();
                } else if(pile.getDirection() == -1 && 10-pile.getTop().getNumber() > worstDiff) {
                    chosenPile = pile.getColor();
                    worstDiff = 10-pile.getTop().getNumber();
                } else if (!pile.isEmpty() && chosenPile == '\u0000'){
                    chosenPile = pile.getColor();
                }
            }
            return chosenPile;
        }
        /* else if (question.equals("From what colored pile do you want to draw?")) {
            // never occurs yet
        } */
        return 'r';
    }

    /**
     * checks which playable hand card and figure is the best option for the AI's next move or if it should
     * move the oracle (and how far)
     */
    private void calculateNextMove() {
        //best combination card + figure
        int bestOption = -100; //best calculated points by combination of figure and card
        for (Figure figure : self.getFigures()) { //for every figure
            for (Card card : self.getHand()) { //for every card from hand
                int steps = steps(figure, card.getColor()); //how far the figure would move with the current card
                if(steps == -100) {break;} // if the card would move the figure too far
                int pointDifference = FIELDS[figure.getPos() + steps].getPoints() - FIELDS[figure.getPos()].getPoints();

                int points = steps + pointDifference; //steps plus the gained points due to the steps
                points -= difference(card, self.getPlayedCards(card.getColor())); // plus how good the card would fit to the played cards pile
                points += tokenWorth(FIELDS[figure.getPos() + steps].getToken()); // plus how good the token on the field is
                if (points > bestOption) { // if combo of figure and card is better than the current one
                    chosenCard = card;
                    chosenFigure = figure;
                    bestOption = points;
                }
            }
        }
        //best combination card + oracle
        int bestOptionOracle = -10; //best calculated points by combination of oracle and card
        int steps = 0; //steps the oracle has to move to get to one of AI's figures
        for (Card card: self.getHand()) { //for every card from hand
            int points = 0; //points of current oracle + card combo
            steps = figureReachable(self.getGame().getOracle(), card.getOracleNumber());
            if(steps != 0) {
                points = 5 - difference(card, self.getPlayedCards(card.getColor())); //5 points if oracle can reach figure and how good card fits
            }
            if(points > bestOptionOracle) { //if card is better than current for oracle
                chosenCardOracle = card;
                bestOptionOracle = bestOption;
            }
        }
        if(bestOptionOracle > bestOption) { //if playing oracle is better than former figure+card combo
            playOracle = true;
            chosenCard = chosenCardOracle;
            oracleSteps = steps;
        }
    }

    /**
     * checks if oracle can reach at least one of AI's figures
     * @param oraclePos current position of oracle
     * @param possibleSteps furthest steps the oracle could move forward
     * @return int 0: if no figure is reachable, 1-5: how many steps needed
     */
    private int figureReachable(int oraclePos, int possibleSteps) {
        int steps = 0;
        for(Figure figure: self.getFigures()) {
            for(int i=1; i<=possibleSteps; i++) {
                if(figure.getPos() == oraclePos+i && oraclePos+i <= 35) { //if oracle can reach figure (and doesn't move out of bounds)
                    steps = i; //steps the oracle has to go to reach figure
                }
            }
        }
        return steps;
    }

    /**
     * calculates how far the given figure would move to the given color
     *
     * @param f     the figure to move
     * @param color the color to move to
     * @return the amount of steps the figure would make (int)
     */
    private int steps(Figure f, char color) {
        int steps = 1;
        try {
            while (FIELDS[f.getPos() + steps].getColor() != color) {
                steps++;
            }
        } catch (Exception e){
            return -100;
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
    private int difference(Card card, DirectionDiscardPile pile) {
        if (CardFunction.cardFitsToPlayersPiles(card, pile)) {
            if (!pile.isEmpty()) {
                return Math.abs(pile.getTop().getNumber() - card.getNumber());
            } else if (card.getNumber() > 5) { //if pile is empty and newNumber is high
                return 10 - card.getNumber();
            } else if (card.getNumber() <= 5) {
                return card.getNumber(); //if pile is empty and newNumber is low
            }
        }
        return 100; //if card doesn't fit
    }

    /**
     * calculates how important the token is
     * @param token the token which worth to calculate
     * @return int worth of the token
     */
    private int tokenWorth(Token token) {
        if (token != null) {
            switch (token.getName()) {
                case "Goblin":
                    if (self.amountFiguresGoblin() == 2 || playableCards().size() < 5) {
                        return 10; //if both other figures are on a goblin field already or hand cards are very bad
                    } else if (self.amountFiguresGoblin() == 1 || playableCards().size() < 4) {
                        return 3; //if one figure already is on a goblin or hand cards are bad
                    }
                    break;
                case "Mirror":
                    return self.calcTokenPoints(); // return current token points because they would be doubled
                case "Skullpoint":
                    return ((Skullpoint) token).getPoints();
                case "Spiderweb":
                    return 4; // on average, you move about 4 steps forward
                case "Spiral":
                    return 1;
                case "WishingStone":
                    return 3; // on average 2,8 points
                default:
                    //do nothing
            }
        }
        return 0;
    }

    /**
     * chooses which card from hand should be trashed
     * @return card which should be trashed
     */
    public Card bestHandCardToTrash() {
        List<Card> notPlayable = new ArrayList<>(self.getHand());
        notPlayable.removeAll(playableCards());
        if (!notPlayable.isEmpty()) {
            return notPlayable.get(0);
        }
        return self.getHand().get(0); //why would it trash if all cards fit
    }

    /**
     * checks what cards from hand could be played
     *
     * @return the cards in a list
     */
    private List<Card> playableCards() {
        List<Card> playable = new ArrayList<>();
        for (Card c : self.getHand()) {
            if (CardFunction.cardFitsToPlayersPiles(c, self.getPlayedCards(c.getColor()))) {
                playable.add(c);
            }
        }
        return playable;
    }

    public void setSelf(Player self) {
        this.self = self;
    }
}