package dhl.model;

import dhl.Constants;
import dhl.controller.player_logic.PlayerLogic;
import dhl.model.tokens.*;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.*;


/**
 * This Class represents a Game. A Game has a list of players, a playingfield (list of Fields), a list of tokens,
 * a Drwaingpile and a list of discard piles. It is one of the main classes of the project.
 */
public class Game implements Serializable {

    private Field[] fields;
    private static List<String> highscores = new ArrayList<>();
    //the best 3 scores (each one entry): first points, second name, third ai or human
    private final DiscardPile discardingPileRed;
    private final DiscardPile discardingPileBlue;
    private final DiscardPile discardingPileGreen;
    private final DiscardPile discardingPilePurple;
    private final DiscardPile discardingPileOrange;
    private final DrawingPile drawingPile;
    private final List<Player> players; //first player is the active player
    private final List<Token> tokens;
    private int oracle; // the index of the field the oracle is standing on

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
        //initializing discard piles, the players list and the oracle
        discardingPileRed = new DiscardPile('r');
        discardingPileBlue = new DiscardPile('b');
        discardingPileGreen = new DiscardPile('g');
        discardingPilePurple = new DiscardPile('p');
        discardingPileOrange = new DiscardPile('o');
        drawingPile = new DrawingPile();
        this.players = players;
        oracle = 0;

        fields = Constants.BASIC_FIELD;
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
                || figuresInFinishArea() >= Constants.totalFiguresInFinish // Enough figures to end the game in finish area
                || enoughFiguresOfOneInFinishArea(); // or one person has enough figures in the finish area
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
    private boolean enoughFiguresOfOneInFinishArea() {
        for (Player p : players) {
            if (FigureFunction.getFigureAmountInFinishArea(p.getFigures()) >= Constants.figuresInFinishOfOnePlayer) {
                return true;
            }
        }
        return false;
    }

    /**
     * this method randomises the order of the tokens and places them on the fields
     */
    private void placeTokens() {
        Collections.shuffle(this.tokens);
        int i = 0;
        for (Field field : getFields()) {
            if (!(field instanceof LargeField)) {
                field.setToken(this.tokens.get(i));
                i++;
            }
        }
    }

    /**
     * method reads the file which has the highscores saved
     * and adds them to string-list highscores
     */
    private static void readHighscores() {
        highscores.clear();
        Scanner s = null ;
        try {
            File file = new File("src/main/resources/highscores.txt");
            s =  new  Scanner ( file ); // read the file contents
            while ( s.hasNextLine())  { // Read the file line by line
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

    public static List<String> getHighscores() {
        readHighscores();
        return highscores;
    }

    /**
     * sorts the players by victory points
     * @return sorted list of players (0=winner, end of list=loser)
     */
    public List<Player> getSortedPlayers(){
        List<Player> playerList = new ArrayList<>(players);
        playerList.sort(Comparator.comparing(Player::getVictoryPoints).reversed());
        return playerList;
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
     * makes the next player the new active one
     * @return the new active player
     */
    public Player nextPlayer(){
        Player lastActive = players.get(0);
        //remove the last active player from the front of the list because he is no longer the active one
        players.remove(lastActive);
        //add it to the end of the list so that it continues to play
        players.add(lastActive);

        return players.get(0); //return thr new active player
    }

    /**
     * returns the size of the list of players
     * @return the amount of players in the game
     */
    public int getPlayerAmount() {
        return players.size();
    }

    public int getOracle() {return oracle;}

    /**
     * moves the oracle the given amount of steps forward
     * @param steps the amount of steps the oracle should move forward
     * @throws Exception if the oracle would leave the field
     */
    public void moveOracle(int steps) throws Exception{
        if(oracle+steps >= getFields().length){
            throw new Exception("The oracle cannot move this far!");
        }
        oracle+=steps;
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
        String separator = " - ";
        highscores.add(points + separator + name + separator + temp[temp.length-1]);
        highscores.sort((a, b) -> (Integer.parseInt(a.split(separator)[0])) > (Integer.parseInt(b.split(separator)[0])) ? -1 : 0);
        if (highscores.size() > 3) {
            highscores = highscores.subList(0,3); //trim to the best 3
        }

        // update the file
        try {
            FileWriter file = new FileWriter ("src/main/resources/highscores.txt");
            for (String line : highscores)  {
                file.write(line + "\n" ); // Write line by line in the file
            }
            file.close();
        } catch (Exception ex)  {
            System.out.println("Message of exception:" + ex.getMessage());
        }
    }

    public Field[] getFields() {
        return fields;
    }
}
