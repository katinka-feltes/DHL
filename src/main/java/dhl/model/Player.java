package dhl.model;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;

    private List<Card> playedCardsRed = new ArrayList<>();
    private List<Card> playedCardsBlue = new ArrayList<>();
    private List<Card> playedCardsGreen = new ArrayList<>();
    private List<Card> playedCardsPurple = new ArrayList<>();
    private List<Card> playedCardsOrange = new ArrayList<>();

    private List<Card> hand = new ArrayList<>();

    private boolean goblinSpecialPlayed = false;

    private int victoryPoints = 0;

    // figures, collected tokens and color need to be added


    public Player(String name){
        this.name = name;
    }

    /**
     * fills the hand up to eight cards
     * @param game The game which the player currently plays
     */
    public void drawCardsUpToEight (Game game) {
        if (game != null){
            while (hand.size() < 8) {
                hand.add(game.draw()); //draws one card and adds it to the hand
            }
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public List<Card> getHand() {
        return hand;
    }
}
