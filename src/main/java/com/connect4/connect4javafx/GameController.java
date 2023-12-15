package com.connect4.connect4javafx;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class GameController extends Application {
    public static final int WINDOW_HEIGHT = 900;
    public static final int WINDOW_WIDTH = 800;
    public static final int NO_OF_COLUMNS = 7;
    public static final int NO_OF_ROWS = 6;
    public static final String GAME_TITLE = "Connect-4";
    public Text mainGameInvalidMoveText;
    public Button mainGamePlayAgainButton;
    public GridPane mainGameGridPane;
    public Text CurrentTurnText;
    public Button Column0Button;
    public Button Column1Button;
    public Button Column2Button;
    public Button Column3Button;
    public Button Column4Button;
    public Button Column5Button;
    public Button Column6Button;
    public Text mainGameTitleText;
    public javafx.scene.layout.VBox VBox;
    private ComputerPlayer computerPlayer = new ComputerPlayer("computerPlayer", Color.RED);
    private HumanPlayer humanPlayer = new HumanPlayer("humanPlayer", Color.BLUE);
    private Connect4Game game = new Connect4Game(computerPlayer, humanPlayer, this);

    @Override
    public void start(Stage stage) {
        try {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/mainGame.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setHeight(WINDOW_HEIGHT);
            stage.setWidth(WINDOW_WIDTH);
            stage.setTitle(GAME_TITLE);
            stage.show();
        } catch (IOException e) {
             System.out.println("Error fetching mainGame.fxml");
        }
    }

    public void createCircleAtNode(Player player, int row, int column) {
        Circle circleToken = new Circle(50);
        circleToken.setFill(player.getPlayerColor());
        circleToken.setId(player.getPlayerName() + "Token" + player.getTotalTokens());
        mainGameGridPane.add(circleToken, column, row);
    }

    /**
     * Cleans up the variables used during a game,
     * and resets any strings that have been altered
     */
    public void cleanup(){
        System.out.println("Cleaning up!...");
        humanPlayer = new HumanPlayer("humanPlayer", Color.BLUE);
        computerPlayer = new ComputerPlayer("computerPlayer", Color.RED);
        game = new Connect4Game(computerPlayer, humanPlayer, this);
        mainGameTitleText.setText(GAME_TITLE);
        CurrentTurnText.setText(("It's Player 1's turn..."));
        CurrentTurnText.setVisible(true);
        mainGameGridPane.getChildren().removeIf(Circle.class :: isInstance);
        mainGamePlayAgainButton.setVisible(false);
        mainGamePlayAgainButton.setDisable(true);
    }

    private void takeTurns(){
        int gameOverValue;
        mainGameInvalidMoveText.setVisible(false);
        gameOverValue = game.takeTurn(humanPlayer);
        if (gameOverValue != -1) {
            showEndOfGame(gameOverValue);
        } else {
            CurrentTurnText.setText("It's Player 2's turn...");
            gameOverValue = game.takeTurn(computerPlayer);
            if (gameOverValue != -1) {
                showEndOfGame(gameOverValue);
            }
        }
        CurrentTurnText.setText("It's Player 1's turn...");
    }

    private void showEndOfGame(int gameResult){
        CurrentTurnText.setVisible(false);
        mainGamePlayAgainButton.setVisible(true);
        mainGamePlayAgainButton.setDisable(false);
        switch (gameResult) {
            case 0 -> {
                mainGameTitleText.setText("It's a draw!");
                System.out.println("Game ended in draw");
            }
            case 1 -> {
                mainGameTitleText.setText("Player 1 wins!");
                System.out.println("Game ended in player 1 victory");
            }
            case 2 -> {
                mainGameTitleText.setText("Player 2 wins!");
                System.out.println("Game ended in player 2 victory");
            }
        }
    }

    @FXML
    private void mainGamePlayAgainButtonPressed() {
        cleanup();
    }

    @FXML
    private void column0ButtonPress() {
        game.setSelectedColumn(0);
        takeTurns();
    }

    @FXML
    private void column1ButtonPress() {
        game.setSelectedColumn(1);
        takeTurns();
    }

    @FXML
    private void column2ButtonPress() {
        game.setSelectedColumn(2);
        takeTurns();
    }

    @FXML
    private void column3ButtonPress() {
        game.setSelectedColumn(3);
        takeTurns();
    }

    @FXML
    private void column4ButtonPress() {
        game.setSelectedColumn(4);
        takeTurns();
    }

    @FXML
    private void column5ButtonPress() {
        game.setSelectedColumn(5);
        takeTurns();
    }

    @FXML
    private void column6ButtonPress() {
        game.setSelectedColumn(6);
        takeTurns();
    }

    public static void main (String[] args){
        launch(args);
    }
}
