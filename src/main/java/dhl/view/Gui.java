package dhl.view;

import dhl.model.Game;
import dhl.model.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Starting point of the JavaFX GUI
 */
public class Gui extends Application implements View{

    Stage window;

    int intReturn = -1;

    /**
     * This method is called by the Application to start the GUI.
     * @param primaryStage The initial root stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {

        //Setting title to the Stage
        primaryStage.setTitle("Dire Horror Land!");
        window = primaryStage;

        //Layout
        BorderPane layout = new BorderPane();

        Text headline = new Text("Dire Horror Land");
        headline.setStyle("-fx-font: normal bold 30px 'sans-serif'");
        Text player = new Text("Active Player: ");
        player.setStyle("-fx-font: normal 20px 'sans-serif'");
        player.setFill(Color.RED); // setting color of the text to blue //TODO: player color

        //Creating HBox for Top
        VBox top = new VBox();
        top.getChildren().addAll(headline, player);

        //Creating GridPane for Center
        GridPane gridPane = new GridPane();
        //Setting size for the pane
        gridPane.setMinSize(700, 400);
        //Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);
        //Styling
        gridPane.setStyle("-fx-border-style: solid; -fx-border-color: Tomato; -fx-border-width: 2px");
        layout.setStyle("-fx-background-color: rgb(100, 100, 100);");
        layout.setTop(top);
        layout.setCenter(gridPane);

        window.setScene(new Scene(layout, 800, 600));
        window.show();
    }

    /**
     * The entry point of the GUI application.
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param playerAmount the amount of names to get
     * @return
     */
    @Override
    public String[] inputPlayersNames(int playerAmount) {

        //creating labels for names
        Text text1 = new Text("Player 1:");
        Text text2 = new Text("Player 2:");

        //Creating Text Fields for names
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();

        //Creating button to start game
        Button btn = new Button("Start Game");
        btn.setOnAction(event -> System.out.println(textField1.getCharacters()));

        //Creating a Grid Pane
        GridPane gridPane = new GridPane();
        //Setting size for the pane
        gridPane.setMinSize(800, 600);

        //Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        gridPane.add(text1, 0, 0);
        gridPane.add(textField1, 1, 0);
        gridPane.add(text2, 0, 1);
        gridPane.add(textField2, 1, 1);
        gridPane.add(btn, 2,2);

        //Styling nodes
        btn.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        gridPane.setStyle("-fx-background-color: rgb(155,95,255);");

        text1.setStyle("-fx-font: normal 10px ");
        text2.setStyle("-fx-font: normal bold 10px ");

        //Creating a scene object
        Scene scene = new Scene(gridPane);

        //Adding scene to the stage
        window.setScene(scene);

        //Displaying the contents of the stage
        window.show();

        return new String[0];
    }

    /**
     * @param start  lowest number allowed
     * @param end    highest number allowed
     * @param prompt the reason for the input to display
     * @return
     */
    @Override
    public int promptInt(int start, int end, String prompt) {
        if(prompt.equals("How many players?")) {
            //create elements
            Text question = new Text(prompt);
            Button btn2 = new Button("2");
            Button btn3 = new Button("3");
            Button btn4 = new Button("4");
            //add events
            btn2.setOnAction(e -> intReturn = 2);
            btn3.setOnAction(e -> intReturn = 3);
            btn4.setOnAction(e -> intReturn = 4);
            // create scene and layout
            HBox layout = new HBox(5);
            layout.getChildren().addAll(question, btn2, btn3, btn4);

            window.setScene(new Scene(layout, 800, 600));
            window.show();

            while(intReturn == -1) {
            }

            return intReturn;
        }

        return 0;
    }

    /**
     * @param prompt the question the player is asked.
     * @return
     */
    @Override
    public boolean promptPlayersChoice(String prompt) {
        return false;
    }

    /**
     * @param prompt the message that the player is asked
     * @return
     */
    @Override
    public char promptColor(String prompt) {
        return 0;
    }

    /**
     * @param prompt the prompt that is printed to the user
     * @return
     */
    @Override
    public String promptCardString(String prompt) {
        return null;
    }

    /**
     * @param game the currently running game
     */
    @Override
    public void printCurrentBoard(Game game) {

    }

    /**
     * @param player the active player whose hand cards to show
     */
    @Override
    public void printHand(Player player) {

    }

    /**
     * @param player the active player whose top cards to show
     */
    @Override
    public void printTopCards(Player player) {

    }

    /**
     * @param game the game that ended
     */
    @Override
    public void printResults(Game game) {

    }

    /**
     * @param game the current game
     */
    @Override
    public void printDiscardingPiles(Game game) {

    }

    /**
     * @param str the message as a string
     */
    @Override
    public void out(String str) {

    }

    /**
     * @param str Error Message
     */
    @Override
    public void error(String str) {

    }
}
