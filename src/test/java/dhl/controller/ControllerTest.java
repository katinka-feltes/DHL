package dhl.controller;

import dhl.view.Cli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class ControllerTest {

    private Controller c;

    @BeforeEach
    public void setUp() {
        c = new Controller();
    }

    @Test
    public void startGame() {
        // amount of players
        String input = "2" + System.getProperty("line.separator") + "Player1" + System.lineSeparator() + "Player2";
        InputStream input2 = new ByteArrayInputStream(input.getBytes());
        System.setIn(input2);
        c.setView(new Cli());
        c.startGame();




    }

    @Test
    void takeTurn() {
    }

    @Test
    void playCard() {
    }

    @Test
    void setView() {
    }
}