package dhl.model;

import java.util.ArrayList;

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

        //add player to the players-list
        char [] symbols = {'\u2660', '\u2663', '\u2665', '\u2666'};
        for(int i = 0; i < playerNames.length; i++){
            players.add(new Player(playerNames[i], symbols[i]));
        }
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
