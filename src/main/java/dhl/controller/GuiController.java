package dhl.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GuiController {

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
