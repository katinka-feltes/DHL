package dhl.model;

import dhl.model.tokens.*;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    public static final Field[] FIELDS = {
            new LargeField(0,'w', 0),
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



    private DiscardPile discardingPileRed;
    private DiscardPile discardingPileBlue;
    private DiscardPile discardingPileGreen;
    private DiscardPile discardingPilePurple;
    private DiscardPile discardingPileOrange;

    private DrawingPile drawingPile;
    private ArrayList<Player> players;

    private ArrayList<Token> tokens;


    /**
     * Constructor for Game with given amount of players (2-4)
     *
     * @param playerAmount the amount of players that will play the game as an int
     */
    public Game (int playerAmount){
        //initializing discard piles and the players list
        discardingPileRed = new DiscardPile('r');
        discardingPileBlue = new DiscardPile('b');
        discardingPileGreen = new DiscardPile('g');
        discardingPilePurple = new DiscardPile('p');
        discardingPileOrange = new DiscardPile('o');
        drawingPile = new DrawingPile();
        players = new ArrayList<>();

        tokens = new ArrayList<>();

        for (int i = 1; i <= 3; i++){
            tokens.add(new Goblin());
            tokens.add(new Spiral());
            tokens.add(new Mirror());
        }
        for (int i = 1; i <= 5; i++){
            tokens.add(new SpiderWeb());
        }
        for (int i = 1; i <= 16; i++) {
            if (i <= 4) {
                tokens.add(new Skullpoints(1));
            } else if (i >= 5 && i <= 10) {
                tokens.add(new Skullpoints(2));
            } else if (i >= 11 && i <= 13) {
                tokens.add(new Skullpoints(3));
            } else {
                tokens.add(new Skullpoints(4));
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
        char [] symbols = {'\u2660', '\u2663', '\u2665', '\u2666'};
        for(int i = 1; i <= playerAmount; i++){
            players.add(new Player("P" + i, symbols[i-1]));
        }
    }

    public void createDecks() {
        for (Player p: players) {
            p.drawCardsUpToEight(drawingPile);
        }
    }

    /**
     * Constructor for Game with given amount of players (2-4)
     * @param playerNames the List of all player Names
     */
    public Game(String[] playerNames){

        //initializing discard piles and the players list
        discardingPileRed = new DiscardPile('r');
        discardingPileBlue = new DiscardPile('b');
        discardingPileGreen = new DiscardPile('g');
        discardingPilePurple = new DiscardPile('p');
        discardingPileOrange = new DiscardPile('o');
        drawingPile = new DrawingPile();
        players = new ArrayList<>();

        tokens = new ArrayList<>();

        for (int i = 1; i <= 3; i++){
            tokens.add(new Goblin());
            tokens.add(new Spiral());
            tokens.add(new Mirror());
        }
        for (int i = 1; i <= 5; i++){
            tokens.add(new SpiderWeb());
        }
        for (int i = 1; i <= 16; i++) {
            if (i <= 4) {
                tokens.add(new Skullpoints(1));
            } else if (i >= 5 && i <= 10) {
                tokens.add(new Skullpoints(2));
            } else if (i >= 11 && i <= 13) {
                tokens.add(new Skullpoints(3));
            } else {
                tokens.add(new Skullpoints(4));
            }
        }

        placeTokens();

        //add player to the players-list
        char [] symbols = {'\u2660', '\u2663', '\u2665', '\u2666'};
        for(int i = 0; i < playerNames.length; i++){
            players.add(new Player(playerNames[i], symbols[i]));
        }

        // all players draw 8 cards
        for (Player p: players) {
            p.drawCardsUpToEight(drawingPile);
        }
    }

    /**
     * checks if drawing pile is empty
     * or if 5 figures are in the finish area
     * or if one player has all figures in the finish area
     * @return true if one of the conditions to end the game true
     */
    public boolean gameOver (){
        return drawingPile.isEmpty()  // drawing pile is empty
                || figuresInFinishArea() >= 5 // 5 figures are in finish area
                || allFiguresOfOneInFinishArea(); // or one person has all figures in the finish area
    }

    /**
     * Adds the given card to the matching discard pile and removes it from the players hand
     * @param card the card to be discarded
     * @param player the player that removes card
     */
    public void putCardOnDiscardingPile(Card card, Player player) throws Exception {
        player.getHand().remove(card);
        switch (card.getColor()) {
            case 'r': discardingPileRed.add(card);
                break;
            case 'g': discardingPileGreen.add(card);
                break;
            case 'b': discardingPileBlue.add(card);
                break;
            case 'p': discardingPilePurple.add(card);
                break;
            case 'o': discardingPileOrange.add(card);
                break;
            default:
                throw new Exception("Color of the card doesn't exist.");
        }
    }

    public boolean canDrawFromDiscarding(Card trashCard) {
        boolean canDraw = !(getDiscardingPileRed().isEmpty() && getDiscardingPileGreen().isEmpty() && getDiscardingPileBlue().isEmpty() &&
                getDiscardingPilePurple().isEmpty() && getDiscardingPileOrange().isEmpty());
        if(trashCard != null) {
            if(canDraw == true) {
                switch (trashCard.getColor()) {
                    case ('r'): if(!getDiscardingPileRed().isEmpty()) {
                        canDraw = !(getDiscardingPileRed().getTop() == trashCard);
                    }
                    case ('g'): if(!getDiscardingPileGreen().isEmpty()) {
                        canDraw = !(getDiscardingPileGreen().getTop() == trashCard);
                    }
                    case ('b'): if(!getDiscardingPileBlue().isEmpty()) {
                        canDraw = !(getDiscardingPileBlue().getTop() == trashCard);
                    }
                    case ('p'): if(!getDiscardingPilePurple().isEmpty()) {
                        canDraw = !(getDiscardingPilePurple().getTop() == trashCard);
                    }
                    case ('o'): if(!getDiscardingPileOrange().isEmpty()) {
                        canDraw = !(getDiscardingPileOrange().getTop() == trashCard);
                    }
                }
            }
        }
        return canDraw;
    }

    /**
     * @return the amount of figures in the finish area as an int
     */
    private int figuresInFinishArea(){
        int amount = 0;
        for (Player p : players){
            amount += p.getFigureAmountInFinishArea();
        }
        return amount;
    }

    /**
     * @return true if one player has all his figures in the finish area
     */
    private boolean allFiguresOfOneInFinishArea() {
        for (Player p : players) {
            if (p.getFigureAmountInFinishArea()==3){
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
            for (Field field : FIELDS){
                if (!(field instanceof LargeField)){
                    field.setToken(this.tokens.get(i));
                    i++;
                }
            }
    }

public Player getWinningPlayer(){
        Player winningP = players.get(0);
        for (Player p : players){
            if (p.getVictoryPoints() > winningP.getVictoryPoints()){
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

    public ArrayList<Player> getPlayers (){
        return players;
    }

    /**
     * returns the amount of the players in the game
     * @return the size of the players-list
     */
    public int getPlayerAmount(){
        return players.size();
    }

}
