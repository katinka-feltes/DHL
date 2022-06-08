package dhl.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class GuiController {

    @FXML
    private Label redLabel;
    @FXML
    private Label greenLabel;
    @FXML
    private Label blueLabel;
    @FXML
    private Label purpleLabel;
    @FXML
    private Label orangeLabel;
    @FXML
    private Rectangle redDiscardPile;
    @FXML
    private Rectangle greenDiscardPile;
    @FXML
    private Rectangle blueDiscardPile;
    @FXML
    private Rectangle purpleDiscardPile;
    @FXML
    private Rectangle orangeDiscardPile;

    @FXML
    private void setDirection() {
        System.out.println("Lol");
        redLabel.setText("+");
    }


    @FXML
    private Label playername;
    @FXML
    private Label toDoMessage;

    public void setLabel(String message) {
        toDoMessage.setText(message);
    }

    public void setPlayername(String name) {
        playername.setText(name);
    }
}
