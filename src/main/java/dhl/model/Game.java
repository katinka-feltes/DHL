package dhl.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Field[] fields = createFields();
    private final List<Card> discardingPileRed = new ArrayList<>();
    private final List<Card> discardingPileBlue = new ArrayList<>();
    private final List<Card> discardingPileGreen = new ArrayList<>();
    private final List<Card> discardingPilePurple = new ArrayList<>();
    private final List<Card> discardingPileOrange = new ArrayList<>();
    private final List<Card> drawingPile = new ArrayList<>();

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

    public Card draw() {
        return drawingPile.remove(0);
    }


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
        temp[14] = new LargeField(1, 'o', 0);
        temp[15] = new Field(1, 'r');
        temp[16] = new Field(1, 'g');
        temp[17] = new Field(2, 'b');
        temp[18] = new Field(2, 'p');
        temp[19] = new Field(2, 'o');
        temp[20] = new Field(2, 'r');
        temp[21] = new LargeField(3, 'r', 0);
        temp[22] = new Field(3, 'g');
        temp[23] = new Field(3, 'b');
        temp[24] = new Field(3, 'p');
        temp[25] = new Field(5, 'o');
        temp[26] = new Field(5, 'r');
        temp[27] = new Field(5, 'g');
        temp[28] = new LargeField(5, 'b', 0);
        temp[29] = new Field(6, 'b');
        temp[30] = new Field(6, 'r');
        temp[31] = new Field(6, 'o');
        temp[32] = new Field(7, 'r');
        temp[33] = new Field(7, 'g');
        temp[34] = new Field(7, 'b');
        temp[35] = new LargeField(10, 'b', 0);
        return temp;
    }

}
