package dhl.controller;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ControllerTest {

    @Test
    public void startGame() {
        String input = "Test";
        InputStream input2 = new ByteArrayInputStream(input.getBytes());
        System.setIn(input2);
    }

    @Test
    public void takeTurn() {
    }

    @Test
    public void playCard() {
    }

    @Test
    public void setView() {
    }
}