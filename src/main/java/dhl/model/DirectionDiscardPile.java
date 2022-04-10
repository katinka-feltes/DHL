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

    public void setDirection(int dir) {
        this.direction = dir;
    }

    public int getDirection(){
        return direction;
    }

    public boolean cardFitsToPile(int direction, Card card){ //color and direction are needed to be correct
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
