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

    /**
     * tests if method collectToken works for collectable
     * tokens on normal fields
     */
    @Test
    public void collectCollectableTokenFromField() {
        Field f = new Field(5, 'r');
        //set collectable token
        f.setToken(new WishingStone());
        assertEquals("WishingStone", f.collectToken().getName());
        assertNull(f.getToken());
        assertNull(f.collectToken());
    }

    /**
     * tests if method collectToken works for not-collectable
     * tokens on normal fields
     */
    @Test
    public void collectNotCollectableTokenFromField() {
        Field f = new Field(5, 'r');
        //set not collectable token
        Token skull = new Skullpoint(1);
        assertFalse(skull.isCollectable());
        f.setToken(skull);
        assertEquals("Skullpoint", f.collectToken().getName());
        assertEquals(skull, f.getToken());
        assertEquals(skull, f.collectToken());
    }

    /**
     * tests if method collectToken works on large fields
     */
    @Test
    public void collectTokenLargeField() {
        //test large field collect token
        LargeField lf = new LargeField(7, 'b', 2);
        assertTrue(lf.collectToken() instanceof WishingStone);
        Token stoneInField = lf.getToken();
        assertEquals(stoneInField, lf.collectToken());
        assertNull(lf.getToken());
        assertNull(lf.collectToken());
    }
}