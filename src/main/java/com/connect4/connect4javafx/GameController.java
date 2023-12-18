package com.connect4.connect4javafx;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

import java.io.IOException;

public class GameController extends Application {
    private static final int WINDOW_HEIGHT = 900;
    private static final int WINDOW_WIDTH = 800;
    private static final int NO_OF_COLUMNS = 7;
    private static final int NO_OF_ROWS = 6;
    private static final String GAME_TITLE = "Connect-4";
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
    private ComputerPlayer computerPlayer;
    private HumanPlayer humanPlayer;
    private Connect4Game connect4Game;

    /**
     * Starts the JavaFX application.
     *
     * @param  stage the primary stage for the application
     */
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
            GameController controller = loader.getController();
            controller.initialise();
        } catch (IOException e) {
             System.out.println("Error fetching mainGame.fxml");
        }
    }

    /**
     * Initialises the function by creating instances of the computer player, human player,
     * and connect4 game. It also calls the takeComputerTurn() function to start the game.
     */
    private void initialise() {
        computerPlayer = new ComputerPlayer("computerPlayer", Color.RED);
        humanPlayer = new HumanPlayer("humanPlayer", Color.BLUE);
        connect4Game = new Connect4Game(computerPlayer, humanPlayer, this);
        connect4Game.takeTurn(computerPlayer);
    }

    /**
     * Creates a circle token at the specified row and column for the given player.
     *
     * @param  player  the player that the circle token belongs to
     * @param  row     the row where the circle token should be created
     * @param  column  the column where the circle token should be created
     */
    public void createCircleAtNode(Player player, int row, int column) {
        Circle circleToken = new Circle(50);
        circleToken.setFill(player.getPlayerColor());
        circleToken.setId(player.getPlayerName() + "Token" + player.getTotalTokens());

        // Check if mainGameGridPane is initialized before adding the circle
        if (mainGameGridPane != null) {
            mainGameGridPane.add(circleToken, column, row);
        } else {
            System.err.println("mainGameGridPane is null. Unable to add circle.");
        }
    }

    /**
     * Cleans up the game state and initializes the game for a new round.
     */
    public void cleanup(){
        System.out.println("Cleaning up!...");
        humanPlayer = new HumanPlayer("humanPlayer", Color.BLUE);
        computerPlayer = new ComputerPlayer("computerPlayer", Color.RED);
        connect4Game = new Connect4Game(computerPlayer, humanPlayer, this);
        mainGameTitleText.setText(GAME_TITLE);
        CurrentTurnText.setText(("It's the computer's turn..."));
        CurrentTurnText.setVisible(true);
        mainGameGridPane.getChildren().removeIf(Circle.class :: isInstance);
        mainGamePlayAgainButton.setVisible(false);
        mainGamePlayAgainButton.setDisable(true);
    }

    /**
     * Takes turns in the game.
     * The human player takes the first turn and the computer player takes the second turn.
     * If the human player makes an invalid move, they are asked to reselect a column.
     * The program waits for a second before the computer takes its turn, to make it easier to see what move the computer is making.
     * After each turn, the game is checked for a win or a draw.
     */
    private void takeTurns() {
        boolean turnSuccessful;
        mainGameInvalidMoveText.setVisible(false);
        turnSuccessful = connect4Game.takeTurn(humanPlayer);
        if (!turnSuccessful) {
            mainGameInvalidMoveText.setText("Invalid move. Please choose another column.");
            mainGameInvalidMoveText.setVisible(true);
        } else {
        checkGameWin();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {connect4Game.takeTurn(computerPlayer);
            checkGameWin();
            CurrentTurnText.setText("It's your turn...");});
        CurrentTurnText.setText("It's the computer's turn...");
        pause.playFromStart();
        }
    }

    /**
     * Checks if the game has been won and shows the end of the game if it has.
     * -1 is returned if the game has not ended yet.
     */
    private void checkGameWin(){
        int gameValue = connect4Game.checkGameWin();
        if (gameValue != -1) {
            showEndOfGame(gameValue);
        }
    }

    /**
     * Shows the end of the game and sets the appropriate UI elements based on the game result.
     *
     * @param  gameResult  the result of the game (0 for draw, 1 for human player win, 2 for computer player win)
     */
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
                    mainGameTitleText.setText("You win!");
                System.out.println("Game ended in human victory");
            }
            case 2 -> {
                mainGameTitleText.setText("The computer wins!");
                System.out.println("Game ended in computer victory");
            }
        }
    }

    /**
     * If play again button is pressed, cleanup the game state and initialize the game for a new round.
     */
    @FXML
    private void mainGamePlayAgainButtonPressed() {
        cleanup();
    }

    /**
     * Set the selected column to column 0 and take the turn.
     */
    @FXML
    private void column0ButtonPress() {
        connect4Game.setSelectedColumn(0);
        takeTurns();
    }

    /**
     * Set the selected column to column 1 and take the turn.
     */
    @FXML
    private void column1ButtonPress() {
        connect4Game.setSelectedColumn(1);
        takeTurns();
    }

    /**
     * Set the selected column to column 2 and take the turn.
     */
    @FXML
    private void column2ButtonPress() {
        connect4Game.setSelectedColumn(2);
        takeTurns();
    }

    /**
     * Set the selected column to column 3 and take the turn.
     */
    @FXML
    private void column3ButtonPress() {
        connect4Game.setSelectedColumn(3);
        takeTurns();
    }

    /**
     * Set the selected column to column 4 and take the turn.
     */
    @FXML
    private void column4ButtonPress() {
        connect4Game.setSelectedColumn(4);
        takeTurns();
    }

    /**
     * Set the selected column to column 5 and take the turn.
     */
    @FXML
    private void column5ButtonPress() {
        connect4Game.setSelectedColumn(5);
        takeTurns();
    }

    /**
     * Set the selected column to column 6 and take the turn.
     */
    @FXML
    private void column6ButtonPress() {
        connect4Game.setSelectedColumn(6);
        takeTurns();
    }

    /**
     * Returns the number of columns.
     *
     * @return the number of columns
     */
    public static int getNoOfColumns() {
        return NO_OF_COLUMNS;
    }

    /**
     * Returns the number of rows.
     *
     * @return the number of rows
     */
    public int getNoOfRows() {
        return NO_OF_ROWS;
    }

    /**
     * Launches the game.
     */
    public static void main (String[] args){
        launch(args);
    }
}
