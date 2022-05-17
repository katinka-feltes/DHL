package dhl.model;

import dhl.model.tokens.*;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    public static final Field[] FIELDS = {
            new LargeField(0, 'w', 0),
            new Field(-4, 'p'),
            new Field(-4, 'o'),
            new Field(-4, 'r'),
            new Field(-4, 'g'),
            new Field(-4, 'b'),
            new Field(-4, 'p'),
            new LargeField(-3, 'r', 2),
            new Field(-3, 'o'),
            new Field(-3, 'r'),
            new Field(-2, 'g'),
            new Field(-2, 'b'),
            new Field(-2, 'p'),
            new Field(-2, 'o'),
            new LargeField(1, 'o', 2),
            new Field(1, 'r'),
            new Field(1, 'g'),
            new Field(2, 'b'),
            new Field(2, 'p'),
            new Field(2, 'o'),
            new Field(2, 'r'),
            new LargeField(3, 'r', 2),
            new Field(3, 'b'),
            new Field(3, 'p'),
            new Field(3, 'g'),
            new Field(5, 'o'),
            new Field(5, 'r'),
            new Field(5, 'g'),
            new LargeField(5, 'g', 2),
            new Field(6, 'b'),
            new Field(6, 'r'),
            new Field(6, 'o'),
            new Field(7, 'r'),
            new Field(7, 'g'),
            new Field(7, 'b'),
            new LargeField(10, 'b', 1)
    };


    private final DiscardPile discardingPileRed;
    private final DiscardPile discardingPileBlue;
    private final DiscardPile discardingPileGreen;
    private final DiscardPile discardingPilePurple;
    private final DiscardPile discardingPileOrange;

    private final DrawingPile drawingPile;
    private final ArrayList<Player> players;

    private final ArrayList<Token> tokens;

    /**
     * This method creates the deck for each player.
     */
    public void createDecks() {
        for (Player p : players) {
            p.drawCardsUpToEight(drawingPile);
        }
    }

    /**
     * Constructor for Game with given amount of players (2-4)
     * @param playerNames the List of all player Names
     */
    public Game(String[] playerNames) {

        //initializing discard piles and the players list
        discardingPileRed = new DiscardPile('r');
        discardingPileBlue = new DiscardPile('b');
        discardingPileGreen = new DiscardPile('g');
        discardingPilePurple = new DiscardPile('p');
        discardingPileOrange = new DiscardPile('o');
        drawingPile = new DrawingPile();
        players = new ArrayList<>();

        tokens = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            tokens.add(new Goblin());
            tokens.add(new Spiral());
            tokens.add(new Mirror());
        }
        for (int i = 1; i <= 5; i++) {
            tokens.add(new Spiderweb());
        }
        for (int i = 1; i <= 16; i++) {
            if (i <= 4) {
                tokens.add(new Skullpoint(1));
            } else if (i <= 10) {
                tokens.add(new Skullpoint(2));
            } else if (i <= 13) {
                tokens.add(new Skullpoint(3));
            } else {
                tokens.add(new Skullpoint(4));
            }
        }

        placeTokens();

        //add player to the players-list
        //other symbols
        //♛ \u265B BLACK CHESS QUEEN
        //♜ \u265C BLACK CHESS ROOK
        //♝ \u265D BLACK CHESS BISHOP
        //♞ \u265E BLACK CHESS KNIGHT
        //our symbols
        //♠ \u2660 BLACK SPADE SUIT
        //♣ \u2663 BLACK CLUB SUIT
        //♥ \u2665 BLACK HEART SUIT
        //♦ \u2666 BLACK DIAMOND SUIT
        char[] symbols = {'\u2660', '\u2663', '\u2665', '\u2666'};
        for (int i = 0; i < playerNames.length; i++) {
            players.add(new Player(playerNames[i], symbols[i], this));
        }
    }

    /**
     * checks if drawing pile is empty
     * or if 5 figures are in the finish area
     * or if one player has all figures in the finish area
     *
     * @return true if one of the conditions to end the game true
     */
    public boolean gameOver() {
        return drawingPile.isEmpty()  // drawing pile is empty
                || figuresInFinishArea() >= 5 // 5 figures are in finish area
                || allFiguresOfOneInFinishArea(); // or one person has all figures in the finish area
    }

    /**
     * checks if drawing from any discarding pile is possible (can't draw if pile is empty or top card = in this turn trashed card)
     *
     * @param trashCard in this turn trashed card
     * @return true if drawing from any discarding pile is possible
     */
    public boolean canDrawFromDiscarding(Card trashCard) {
        // can't draw if all piles are empty
        boolean canDraw = !(getDiscardingPileRed().isEmpty() && getDiscardingPileGreen().isEmpty() && getDiscardingPileBlue().isEmpty() &&
                getDiscardingPilePurple().isEmpty() && getDiscardingPileOrange().isEmpty());
        // if a trashcard exists and not all piles are empty, we have to check if trashcard == top card of discarding pile
        // if yes, he can only draw if there is another pile which isn't empty
        if (trashCard != null && canDraw &&
                // red pile's top card
                ((!getDiscardingPileRed().isEmpty() && getDiscardingPileRed().getTop() == trashCard
                        && getDiscardingPileBlue().isEmpty() && getDiscardingPileGreen().isEmpty()
                        && getDiscardingPilePurple().isEmpty() && getDiscardingPileOrange().isEmpty())
                        // green pile's top card
                        || (!getDiscardingPileGreen().isEmpty() && getDiscardingPileGreen().getTop() == trashCard
                        && getDiscardingPileBlue().isEmpty() && getDiscardingPileRed().isEmpty()
                        && getDiscardingPilePurple().isEmpty() && getDiscardingPileOrange().isEmpty())
                        // blue pile's top card
                        || (!getDiscardingPileBlue().isEmpty() && getDiscardingPileBlue().getTop() == trashCard
                        && getDiscardingPileRed().isEmpty() && getDiscardingPileGreen().isEmpty()
                        && getDiscardingPilePurple().isEmpty() && getDiscardingPileOrange().isEmpty())
                        // purple's pile top card
                        || (!getDiscardingPilePurple().isEmpty() && getDiscardingPilePurple().getTop() == trashCard
                        && getDiscardingPileBlue().isEmpty() && getDiscardingPileGreen().isEmpty()
                        && getDiscardingPileRed().isEmpty() && getDiscardingPileOrange().isEmpty())
                        // orange's pile top card
                        || (!getDiscardingPileOrange().isEmpty() && getDiscardingPileOrange().getTop() == trashCard
                        && getDiscardingPileBlue().isEmpty() && getDiscardingPileGreen().isEmpty()
                        && getDiscardingPilePurple().isEmpty() && getDiscardingPileRed().isEmpty()))) {
            canDraw = false;
        }
        return canDraw;
    }

    /**
     * counts the amount of figures from every player in the finish area
     * @return the amount of figures in the finish area as an int
     */
    private int figuresInFinishArea() {
        int amount = 0;
        for (Player p : players) {
            amount += p.getFigureAmountInFinishArea();
        }
        return amount;
    }

    /**
     * checks if one player has three figures in the finish area.
     * @return true if one player has all his figures in the finish area
     */
    private boolean allFiguresOfOneInFinishArea() {
        for (Player p : players) {
            if (p.getFigureAmountInFinishArea() == 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * this method randomises the order of the tokens and places them on the fields
     */
    public void placeTokens() {
        Collections.shuffle(this.tokens);
        int i = 0;
        for (Field field : FIELDS) {
            if (!(field instanceof LargeField)) {
                field.setToken(this.tokens.get(i));
                i++;
            }
        }
    }

    /**
     * checks the victory points of every player to elect the winning player.
     * @return the player with the highest score.
     */
    public Player getWinningPlayer() {
        Player winningP = players.get(0);
        for (Player p : players) {
            if (p.getVictoryPoints() > winningP.getVictoryPoints()) {
                winningP = p;
            }
        }
        return winningP;
    }

    public DrawingPile getDrawingPile() {
        return drawingPile;
    }

    public DiscardPile getDiscardingPileBlue() {
        return discardingPileBlue;
    }

    public DiscardPile getDiscardingPileRed() {
        return discardingPileRed;
    }

    public DiscardPile getDiscardingPileGreen() {
        return discardingPileGreen;
    }

    public DiscardPile getDiscardingPilePurple() {
        return discardingPilePurple;
    }

    public DiscardPile getDiscardingPileOrange() {
        return discardingPileOrange;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getPlayerAmount() {
        return players.size();
    }

}
