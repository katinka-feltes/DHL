package dhl.player_logic;

import dhl.Constants;
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
    Card chosenCardOracle;
    Figure chosenFigure;
    int oracleSteps;
    char chosenDiscard;
    Player self;

    @Override
    public boolean choose(String question) {
        if (question.startsWith("Do you want to play a card?")) {
            return playableCards().size() > 2; // play a card if there are more than two cards to play
        } else if (question.startsWith("Do you want to draw your card from one of the discarding piles?")) {
            return chooseDrawing(); // decides which pile to draw from
        } else if (question.startsWith("Do you want to trash one from your hand?")) {
            return playableCards().size() < 3; // trash a hand card if only 2 of them can be played
        } else if (question.startsWith("Do you want to move a figure?")) { // true = figure; false = oracle
            return oracleSteps == 0; //if steps = 0, a figure shall be moved
        } else { //return true to the following questions, false to all others
            return question.endsWith("Are you ready to play?") || question.equals("Are you done with your turn?")
                    || question.startsWith("Do you want to play your goblin-special?") // always play if possible, because it doesn't occur often
                    || question.endsWith("Do you want to proceed with your action?"); // always wants to proceed
        }
    }

    @Override
    public int chooseSpiralPosition(String question, int position) {

        Field[] fields = self.getGame().getFields();

        int chosenPosition = position;
        int bestOption = 0;

        for(int i = 1; i < 6; i++){
            int thisOption = tokenWorth(fields, position-i);
            //if the option is better and not the figures last position
            if (thisOption > bestOption && position-i != self.getLastMovedFigure().getLatestPos()) {
                bestOption = thisOption;
                chosenPosition = position-i;
            }
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
     * decides if drawing from drawing or discarding pile is smarter
     * discarding is good if a top card fits good to played cards and not many of that color on hand
     * @return true to draw from discarding, false to draw from drawing pile
     */
    private boolean chooseDrawing() {
        int bestDrawOption = 10; //best pile to draw from
        char colorOfBestOption = 'b'; //color of best pile to draw from
        for(char color: Constants.COLORS) {
            int currentDrawOption = 10; //how good drawing from current pile would be
            Card topCard = self.getGame().getDiscardPile(color).getTop(); //top card of current pile
            if(topCard != null && topCard != self.getLastTrashed()) {
                //calculates how good drawing from discard pile would be:
                //small difference to (possible) played cards pile is good and if there aren't many of that color on hand
                currentDrawOption = difference(topCard, self.getPlayedCards(topCard.getColor()))
                        + CardFunction.amountOfColoredCards(self.getHand(), topCard.getColor());
            }
            if(currentDrawOption < bestDrawOption) { //if drawing from current pile is best option
                bestDrawOption = currentDrawOption;
                colorOfBestOption = color;
            }
        }
        if(bestDrawOption < 5) { //if <5 drawing from discarding is a good option and AI chooses this
            chosenDiscard = colorOfBestOption;
            return true;
        }
        return false;
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
        return chosenDiscard; // "From what colored pile do you want to draw?"
    }

    /**
     * checks which playable hand card and figure is the best option for the AI's next move or if it should
     * move the oracle (and how far)
     */
    private void calculateNextMove() {
        oracleSteps = 0;
        //best combination card + figure
        int bestOption = -100; //best calculated points by combination of figure and card
        for (Figure figure : self.getFigures()) { //for every figure
            for (Card card : self.getHand()) { //for every card from hand
                int points = valueOfMove(figure.getPos(), card.getColor());
                points -= difference(card, self.getPlayedCards(card.getColor())); // plus how good the card would fit to the played cards pile
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
            int points = -100; //points of current oracle + card combo
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
            chosenCard = chosenCardOracle;
            oracleSteps = steps;
        }
    }


    /**
     * calculates the worth of the simulated move
     * (not taking a difference to played cards into account)
     *
     * @param figurePos the position of the figure that would be moved
     * @param color     the color of the filed to goo to
     * @return the calculated points (int)
     */
    private int valueOfMove(int figurePos, char color) {
        Field[] fields = self.getGame().getFields();
        int steps;
        try {
            steps = FigureFunction.steps(figurePos, color);
            //how far the figure would move with the current card
        } catch (Exception e) {
            return -100;
        }
        int pointDifference = fields[figurePos + steps].getPoints() - fields[figurePos].getPoints();

        int points = steps + pointDifference; //steps plus the gained points due to the steps
        points += tokenWorth(fields, figurePos+steps); // plus how good the token on the field is
        return points;
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
     * @param fields the field where the token lays
     * @param tokenPos the pos of the token which worth to calculate
     * @return int worth of the token
     */
    private int tokenWorth(Field[]fields, int tokenPos) {
        Token token = fields[tokenPos].getToken();
        if (token != null) {
            switch (token.getName()) {
                case "Goblin":
                    return goblinWorth();
                case "Mirror":
                    return self.calcTokenPoints(); // return current token points because they would be doubled
                case "Skullpoint":
                    return ((Skullpoint) token).getPoints();
                case "Spiderweb":
                    return valueOfMove(tokenPos, fields[tokenPos].getColor());
                    // the value of the move the spiderweb would give
                case "Spiral":
                    return 1;
                    //returns the worth of the token the spiral would achieve
                case "WishingStone":
                    return (int)(2.8 * (self.getTokens()[1] + 1)); // on average 2,8 points * amount of mirrors owned
                default:
                    //do nothing
            }
        }
        return 0;
    }

    private int goblinWorth() {
        if (self.amountFiguresGoblin() == 2 || playableCards().size() < 5) {
            return 10;
        } else if (self.amountFiguresGoblin() == 1 || playableCards().size() < 4) {
            return 3;
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

    public void setSelf(Player self) {this.self = self;}
}