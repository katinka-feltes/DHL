package dhl.model;

import dhl.controller.player_logic.PlayerLogic;
import dhl.model.tokens.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * This Class represents a Game. A Game has a list of players, a playingfield (list of Fields), a list of tokens,
 * a Drwaingpile and a list of discard piles. It is one of the main classes of the project.
 */
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

    //the best 3 scores (each one entry): first points, second name, third ai or human
    private List<String> highscores = new ArrayList<>();

    private final DiscardPile discardingPileRed;
    private final DiscardPile discardingPileBlue;
    private final DiscardPile discardingPileGreen;
    private final DiscardPile discardingPilePurple;
    private final DiscardPile discardingPileOrange;

    private final DrawingPile drawingPile;
    private final List<Player> players;

    private final List<Token> tokens;

    /**
     * This method sets the game for each player and also creates their deck.
     */
    public void setup() {
        for (Player p : players) {
            p.setGame(this);
            if (drawingPile != null) {
                // draw up to 8 cards
                while (p.getHand().size() < 8 && !drawingPile.isEmpty()) {
                    p.drawFromDrawingPile(); //draw one card from drawing pile
                }
            }
        }
    }

    /**
     * Constructor for Game with given amount of players (2-4)
     * @param players the List of all player Names
     */
    public Game(List<Player> players) {
        readHighscores();
        //initializing discard piles and the players list
        discardingPileRed = new DiscardPile('r');
        discardingPileBlue = new DiscardPile('b');
        discardingPileGreen = new DiscardPile('g');
        discardingPilePurple = new DiscardPile('p');
        discardingPileOrange = new DiscardPile('o');
        drawingPile = new DrawingPile();
        this.players = players;

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
        setup();
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
        return !(discardingPileRed.isEmpty() && discardingPileGreen.isEmpty() && discardingPileBlue.isEmpty()
                && discardingPilePurple.isEmpty() && discardingPileOrange.isEmpty()
                // can't draw if all piles are empty
                || trashCard!=null && !getDiscardPile(trashCard.getColor()).isEmpty()
                && getDiscardPile(trashCard.getColor()).getTop() == trashCard
                && allPilesEmptyExcept(trashCard.getColor()));
                //if there is a trashed card and all other piles are empty
    }

    /**
     * Check if all piles except one are empty
     * @param colorNotToCheck the color of the pile to ignore
     * @return true if all piles except the one in the given color are empty
     */
    private boolean allPilesEmptyExcept(char colorNotToCheck) {
        List<Character> colors = new ArrayList<>();
        colors.add('r');
        colors.add('g');
        colors.add('b');
        colors.add('p');
        colors.add('o');
        // remove the color to ignore
        colors.remove((Character) colorNotToCheck);

        for(char c : colors){
            if(!getDiscardPile(c).isEmpty()){
                return false;
            }
        }
        return true;
    }

    /**
     * counts the amount of figures from every player in the finish area
     * @return the amount of figures in the finish area as an int
     */
    private int figuresInFinishArea() {
        int amount = 0;
        for (Player p : players) {
            amount += FigureFunction.getFigureAmountInFinishArea(p.getFigures());
        }
        return amount;
    }

    /**
     * checks if one player has three figures in the finish area.
     * @return true if one player has all his figures in the finish area
     */
    private boolean allFiguresOfOneInFinishArea() {
        for (Player p : players) {
            if (FigureFunction.getFigureAmountInFinishArea(p.getFigures()) == 3) {
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
     * adds the info if the player was one of the best 3 players
     * @param points the points that will be compared to current highscores
     * @param name the name of the player that got the points
     * @param logic the logic that played
     */
    public void updateHighscore(int points, String name, PlayerLogic logic) {
        String[] temp = logic.getClass().toString().split("\\.");
        // example: 34 - Janne - Human
        highscores.add(points + " - " + name + " - " + temp[temp.length-1]);
        Collections.sort(highscores, (a, b) -> (Integer.parseInt(a.split(" - ")[0])) > (Integer.parseInt(b.split(" - ")[0])) ? -1  : 0);
        highscores = highscores.subList(0,3); //trim to the best 3
    }

    /**
     * method reads the file which has the highscores saved
     * and adds them to string-list highscores
     */
    private void readHighscores(){
        highscores.clear();
        Scanner s = null ;
        try  {
            File file = new File ("src/main/resources/highscores.txt");
            s =  new  Scanner ( file ); // read the file contents
            while  ( s.hasNextLine())  { // Read the file line by line
                String line = s.nextLine();
                highscores.add(line);
            }

        }  catch (Exception ex)  {
            System.out.println("Message:" + ex.getMessage());
        }  finally  {
            // Close the file whether the reading was successful or not
            try  {
                if ( s !=  null) s .close ();
            }  catch  (Exception ex2)  {
                System.out.println( "Message 2:"  + ex2 . getMessage());
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

    /**
     * method to get the discard pile of the game in the given color
     * @param color the color of the pile to return
     * @return the discard pile
     */
    public DiscardPile getDiscardPile(char color){
        switch (color) {
            case 'r':
                return discardingPileRed;
            case 'g':
                return discardingPileGreen;
            case 'b':
                return discardingPileBlue;
            case 'p':
                return discardingPilePurple;
            case 'o':
                return discardingPileOrange;
            default:
                return null;
        }
    }

    public DrawingPile getDrawingPile() {
        return drawingPile;
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * returns the size of the list of players
     * @return the amount of players in the game
     */
    public int getPlayerAmount() {
        return players.size();
    }

    public List<String> getHighscores() {
        return highscores;
    }
}
