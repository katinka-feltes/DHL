package dhl.model;

public class DirectionDiscardPile extends DiscardPile{

    private int direction; //-1 if decreasing, 0 if not set and 1 if increasing

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
     * @param card the given card
     * @return the boolean if the card fits or not
     */
    public boolean cardFitsToPile(Card card){ //color are needed to be correct
        switch(direction){
            case(0):
                if(pile.isEmpty()) {
                    return true;
                }
                return getTop().getColor() == getColor();
            case(1):
                return (getTop().getColor() == getColor() && getTop().getNumber() <= card.getNumber());
            case(-1):
                return (getTop().getColor() == getColor() && getTop().getNumber() >= card.getNumber());
            default:
                return false;
        }
    }


    /**
     * adds card to top (end) of the pile if the color and direction fit
     * updates the direction if not set yet
     * @param card the card that is added
     */
    @Override
    public void add(Card card){
        //check if card exists and fits
        if (card == null || card.getColor()!=color || !cardFitsToPile(card)){
            System.out.println("Card is null, a different color than the pile or doesn't fit the direction(for your info carl)");
            return;
        }
        // set direction if needed
        if (direction == 0 && !pile.isEmpty()){
            int difference = card.getNumber() - super.getTop().getNumber();
            //difference between card to add and top card of pile, larger 0 if added card is higher
            if(difference > 0){
                direction = 1;
            } else if(difference < 0){
                direction = -1;
            }
        }

        //add card to pile
        super.getPile().add(card);
    }
}
