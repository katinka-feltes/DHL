package dhl.model;

/**
 * This Class represents a Card. A Card has a number and a color.
 */
public class Card {
    private final int number;
    private final char color;
    //private final int oracleNumber;

    /**
     * Creates a new card with the specified number and color.
     * @param number The number of the card. Must be between 0 and 10.
     * @param color The color of the card. Here we use 'r' for red, 'g' for green, 'b' for blue 'p' for purple and 'o'
     *              for orange.
     */
    public Card(int number, char color) {
        this.number = number;
        this.color = color;
    }

    /* not yet used
     * Creates a new card with the specified number, color and oracle number.
     * @param number The number of the card. Must be between 0 and 10.
     * @param color The color of the card. Here we use 'r' for red, 'g' for green, 'b' for blue 'p' for purple and 'o'
     *              for orange.
     * @param oracleNumber The oracle number of the card.
    public Card(int number, char color, int oracleNumber) {
        this.number = number;
        this.color = color;
        this.oracleNumber = oracleNumber;
    } */

    public int getNumber() {
        return number;
    }

    public char getColor() {
        return color;
    }
}
