package dhl.model.tokens;

public class WishingStone implements Token{

    @Override
    public boolean collectable() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void action() {

    }
}
