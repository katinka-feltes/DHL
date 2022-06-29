package dhl.model;

/**
 * This Class represents a Card. A Card has a number and a color.
 */
public class Card {
    private final int number;
    private final char color;
    private final int oracleNumber;

    /**
     * Creates a new card with the specified number and color.
     * The oracle number is set according to the given number: (0:5,1:4,2:3,3:2,4:1,5:5,6:1,7:2,8:3,9:4,10:5)
     * @param number The number of the card. Must be between 0 and 10.
     * @param color The color of the card. Here we use 'r' for red, 'g' for green, 'b' for blue 'p' for purple and 'o'
     *              for orange.
     */
    public Card(int number, char color) {
        this.number = number;
        this.color = color;

        //set oracle number
        switch (number){
            case 1:
            case 9:
                this.oracleNumber = 4;
                break;
            case 2:
            case 8:
                this.oracleNumber = 3;
                break;
            case 3:
            case 7:
                this.oracleNumber = 2;
                break;
            case 4:
            case 6:
                this.oracleNumber = 1;
                break;
            default: // cases 0,5,10
                this.oracleNumber = 5;
                break;
        }
    }

    public int getNumber() {
        return number;
    }

    public int getOracleNumber() {
        return oracleNumber;
    }

    public char getColor() {
        return color;
    }
}
