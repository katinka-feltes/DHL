package dhl.view;

import dhl.model.Game;
import dhl.model.Player;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Starting point of the JavaFX GUI
 */
public class Gui extends Application{

    private final String RED = "#741C1C";
    private final String GREEN = "#203420";
    private final String BLUE = "#1A2847";
    private final String PURPLE = "#4B2D48";
    private final String ORANGE = "#D05E00";


    /**
     * The entry point of the GUI application.
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method is called by the Application to start the GUI.
     * @param primaryStage The initial root stage of the application.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/start.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * changes a color from char to string
     * @param color as a char
     * @return color as HEX in String
     */
    private String getColor (char color){
        switch (color){
            case 'r': return RED;
            case 'g': return GREEN;
            case 'b': return BLUE;
            case 'p': return PURPLE;
            case 'o': return ORANGE;
            default: return "BLACK";
        }
    }
}