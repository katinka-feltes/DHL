package dhl.model;

public class DirectionDiscardPile extends DiscardPile{

    private int direction;

    /**
     * The constructor for DiscardPile
     *
     * @param color the color that the pile will have
     */
    public DirectionDiscardPile(char color) {
        super(color);
        this.direction = 0;
    }

    /**
     * The setter of the direction for one discard pile
     *
     * @param dir the set direction
     */
    public void setDirection(int dir) {
        this.direction = dir;
    }

    /**
     * The getter of the discard piles direction
     *
     * @return direction
     */
    public int getDirection(){
        return direction;
    }

    /**
     * Checks if the given card fits to the pile. The color and the direction are needed to be correct to achieve a true
     *
     * @param direction of the discard pile
     * @param card the given card
     * @return the boolean if the card fits or not
     */
    public boolean cardFitsToPile(Card card){ //color and direction are needed to be correct
        switch(direction){
            case(0):
                return getTop().getColor() == getColor();
            case(1):
                return (getTop().getColor() == getColor() && getTop().getNumber() <= card.getNumber());
            case(-1):
                return (getTop().getColor() == getColor() && getTop().getNumber() >= card.getNumber());
            default:
                return false;
        }
    }
}
