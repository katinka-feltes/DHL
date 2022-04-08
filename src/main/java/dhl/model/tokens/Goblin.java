package dhl.model.tokens;

public class Goblin implements Token {

    @Override
    public boolean collectable() {
        return false;
    }

    @Override
    public String getName() {
        String[] temp = this.getClass().toString().split("\\.");
        return temp[temp.length-1];
    }

    @Override
    public void action() {

    }
}
