package dhl.model;

import java.util.ArrayList;
import java.util.List;

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

    private List<Card> discardingPileRed;
    private List<Card> discardingPileBlue;
    private List<Card> discardingPileGreen;
    private List<Card> discardingPilePurple;
    private List<Card> discardingPileOrange;

    private DrawingPile drawingPile;

    private List<Player> players;


    /**
     * Constructor for Game with given amount of players (2-4)
     * @param playerAmount the amount of players that will play the game as an int
     */
    public Game (int playerAmount){
        //maybe check if playerAmount is between 2 and 4
        //add player sto the players-list
        discardingPileRed = new ArrayList<>();
        discardingPileBlue = new ArrayList<>();
        discardingPileGreen = new ArrayList<>();
        discardingPilePurple = new ArrayList<>();
        discardingPileOrange = new ArrayList<>();
        drawingPile = new DrawingPile();
        players = new ArrayList<>();

        for(int i = 1; i <= playerAmount; i++){
            players.add(new Player("Player" + i));
            System.out.println(players.get(i-1).getName());
        }
    }

    public void putCardOnDiscardingPile(Card card, Player player) {
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
        }
    }

    public List<Card> getDiscardingPileBlue() {
        return discardingPileBlue;
    }

    public List<Card> getDiscardingPileRed() {
        return discardingPileRed;
    }

    public List<Card> getDiscardingPileGreen() {
        return discardingPileGreen;
    }

    public List<Card> getDiscardingPilePurple() {
        return discardingPilePurple;
    }

    public List<Card> getDiscardingPileOrange() {
        return discardingPileOrange;
    }

}
