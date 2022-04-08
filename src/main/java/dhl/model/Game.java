package dhl.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Field[] fields = createFields();

    private List<Card> discardingPileRed = new ArrayList<>();
    private List<Card> discardingPileBlue = new ArrayList<>();
    private List<Card> discardingPileGreen = new ArrayList<>();
    private List<Card> discardingPilePurple = new ArrayList<>();
    private List<Card> discardingPileOrange = new ArrayList<>();

    private List<Card> drawingPile = new ArrayList<>();

    private List<Player> players = new ArrayList<>();


    /**
     * Constructor for Game with given amount of players (2-4)
     * @param playerAmount the amount of players that will play the game as an int
     */
    public Game (int playerAmount){
        //maybe check if playerAmount is between 2 and 4
        //add player sto the players-list
        for(int i = 1; i <= playerAmount; i++){
            players.add(new Player("Player" + i));
            System.out.println(players.get(i-1).getName());
        }

        createDrawingPile();
    }


    /* Carl what is this?

    public Field[] getFields() {
        return fields;
    }

    public Card getDiscardingPileRed() {
        return discardingPileRed.remove(0);
    }
    public Card getDiscardingPileBlue() {
        return discardingPileBlue.remove(0);
    }
    public Card getDiscardingPileGreen() {
        return discardingPileGreen.remove(0);
    }
    public Card getDiscardingPilePurple() {
        return discardingPilePurple.remove(0);
    }
    public Card getDiscardingPileOrange() {
        return discardingPileOrange.remove(0);
    }
    */

    /**
     * Gets the top card of the Drawing Pile and removes it
     * @return the top card from the drawing pile
     */
    public Card draw() {
        return drawingPile.remove(0);
    }

    /**
     * creates the field
     * @return the complete field of the game as a Field-Array
     */
    private Field[] createFields() {
        Field[] temp = new Field[36];
        temp[0] = new LargeField(0,'w', 0);
        temp[1] = new Field(-4, 'p');
        temp[2] = new Field(-4, 'o');
        temp[3] = new Field(-4, 'r');
        temp[4] = new Field(-4, 'g');
        temp[5] = new Field(-4, 'b');
        temp[6] = new Field(-4, 'p');
        temp[7] = new LargeField(-3, 'r', 2);
        temp[8] = new Field(-3, 'o');
        temp[9] = new Field(-3, 'r');
        temp[10] = new Field(-2, 'g');
        temp[11] = new Field(-2, 'b');
        temp[12] = new Field(-2, 'p');
        temp[13] = new Field(-2, 'o');
        temp[14] = new LargeField(1, 'o', 2);
        temp[15] = new Field(1, 'r');
        temp[16] = new Field(1, 'g');
        temp[17] = new Field(2, 'b');
        temp[18] = new Field(2, 'p');
        temp[19] = new Field(2, 'o');
        temp[20] = new Field(2, 'r');
        temp[21] = new LargeField(3, 'r', 2);
        temp[22] = new Field(3, 'g');
        temp[23] = new Field(3, 'b');
        temp[24] = new Field(3, 'p');
        temp[25] = new Field(5, 'o');
        temp[26] = new Field(5, 'r');
        temp[27] = new Field(5, 'g');
        temp[28] = new LargeField(5, 'g', 2);
        temp[29] = new Field(6, 'b');
        temp[30] = new Field(6, 'r');
        temp[31] = new Field(6, 'o');
        temp[32] = new Field(7, 'r');
        temp[33] = new Field(7, 'g');
        temp[34] = new Field(7, 'b');
        temp[35] = new LargeField(10, 'b', 1);
        return temp;
    }

    /**
     * Creates the drawing pile with the 110 cards
     * (numbers 0-10 for each color twice)
     */
    private void createDrawingPile(){
        char[] colors2x = {'r', 'g', 'b', 'p', 'o', 'r', 'g', 'b', 'p', 'o'};
        for (char color : colors2x){
            for(int i = 0; i <= 10; i++) {
                drawingPile.add(new Card(i, color));
            }
        }
    }

}
