package dhl.model;

import dhl.model.tokens.Skullpoint;
import dhl.model.tokens.Token;
import dhl.model.tokens.WishingStone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests the class Field
 */
public class FieldTest {

    @Test
    /**
     * tests if method collectToken works
     */
    public void collectToken() {
        Field f = new Field(5, 'r');
        //set collectable token
        f.setToken(new WishingStone());
        assertEquals("WishingStone", f.collectToken().getName());
        assertNull(f.getToken());
        assertNull(f.collectToken());
        //set not collectable token
        Token skull = new Skullpoint(1);
        assertFalse(skull.isCollectable());
        f.setToken(skull);
        assertEquals("Skullpoint", f.collectToken().getName());
        assertEquals(skull, f.getToken());
        assertEquals(skull, f.collectToken());

        //test large field collect token
        LargeField lf = new LargeField(7, 'b', 2);
        assertTrue(lf.collectToken() instanceof WishingStone);
        Token stoneInField = lf.getToken();
        assertEquals(stoneInField, lf.collectToken());
        assertNull(lf.getToken());
        assertNull(lf.collectToken());
    }
}