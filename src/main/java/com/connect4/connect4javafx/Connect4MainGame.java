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
import java.util.HashSet;
import java.util.Set;

public class Connect4MainGame extends Application {
    public static final int WINDOW_HEIGHT = 900;
    public static final int WINDOW_WIDTH = 800;
    public static final int NO_OF_COLUMNS = 7;
    public static final int NO_OF_ROWS = 6;
    public static final String GAME_TITLE = "Connect-4";
    public Text mainGameInvalidMoveText;
    public Button mainGamePlayAgainButton;
    private int selectedColumn;
    private int lowestPlayableRow = -1;
    private int player1TotalTokens = 0;
    private int player2TotalTokens = 0;
    private final int[][] board = new int[NO_OF_ROWS][NO_OF_COLUMNS];
    private String currentPlayer = "Player1";
    public GridPane MainGameGridPane;
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
    private final ComputerPlayer computerPlayer = new ComputerPlayer("computerPlayer", Color.RED, 2);
    private final Player humanPlayer = new Player("humanPlayer", Color.BLUE, 1);

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
            initialiseArray();
        } catch (IOException e) {
             System.out.println("Error fetching mainGame.fxml");
        }
    }

    /**
     * Initialise the game array, filling each slot with a placeholder 0.
     */
    private void initialiseArray(){
        for (int i = 0; i < NO_OF_ROWS; i++){
            for (int j = 0; j < NO_OF_COLUMNS; j++){
                board[i][j] = 0;
            }
        }
    }

    /**
     * Draws a circle at the given column, row in the given colour.
     *
     * @param totalTokens              - total number of tokens played so far by that player
     * @param column                   - current column for token placement
     * @param row                      - current row for token placement
     */
    private void createCircleAtNode(int totalTokens, String playerName, int column, int row, Color playerColour) {
        Circle circleToken = new Circle(50);
        circleToken.setFill(playerColour);
        circleToken.setId(playerName + "Token" + totalTokens);
        MainGameGridPane.add(circleToken, column, row);
    }


    /**
     *
     */
    private void takeTurn(Player player) {
        mainGameInvalidMoveText.setVisible(false);
        if (player == humanPlayer){
            int column = getSelectedColumn();
            int row = findLowestPlayableRow(column);
            createCircleAtNode(player.getTotalTokens(), player.getPlayerName(), column, row, player.getPlayerColor());
            player.setTotalTokens(player.getTotalTokens() +1);
            board[row - 1][column] = 1;
            switchPlayerTurn(player);
        } else if (player == computerPlayer){
            int column = computerPlayer.takeTurn(board);
            int row = findLowestPlayableRow(column);
            createCircleAtNode(player.getTotalTokens(), player.getPlayerName(), column, row, player.getPlayerColor());
            player.setTotalTokens(player.getTotalTokens() + 1);
            board[row - 1][column] = 2;
            switchPlayerTurn(player);
        }
        if (humanPlayer.getTotalTokens() >= 4) {
            checkGameWin();
        }
    }

    private void switchPlayerTurn(Player player) {
        if (player == humanPlayer) {
            setCurrentPlayer(computerPlayer.getPlayerName());
            CurrentTurnText.setText("It's Player 2's turn...");
        } else {
            setCurrentPlayer(humanPlayer.getPlayerName());
            CurrentTurnText.setText("It's Player 1's turn...");
        }
    }

    /**
     *
     * @param selectedColumn - the column chosen by the player
     * @return the lowest row in that column where a token has not been played
     */
    private int findLowestPlayableRow(int selectedColumn) {
        for (int currentRow = MainGameGridPane.getRowCount() - 1; currentRow > 0; currentRow--) {
            if (board[currentRow - 1][selectedColumn] == 0){
                return currentRow;
            }
        }
        //If column is full, return -1
        return -1;
    }

    /**
     * Calls each of the win condition checking functions.
     * If a winning condition has been met, call the end game screen
     */
    private void checkGameWin() {
        int checkColumnsResult = checkColumns();
        int checkRowsResult = checkRows();
        int checkDiagonalsResult = checkDiagonals();

        //If player 1 has met any of the win conditions
        if (checkColumnsResult == 1 || checkRowsResult == 1 || checkDiagonalsResult == 1){
            showEndOfGame(1);
        }
        //If player 2 has met any of the win conditions
        else if (checkColumnsResult == 2 || checkRowsResult == 2 || checkDiagonalsResult == 2){
            showEndOfGame(2);
        }
        //If no win conditions have been met, check for a draw
        else if (checkDraw()){
            showEndOfGame(0);
        }
    }

    /**
     * Checks each array index, if a 0 is found then the board is not full,
     * so a draw has not been reached
     * @return true if there are no empty slots (i.e. slots containing 0) in the array,
     * return false
     */
    public boolean checkDraw(){
        int[][] gameArray = getBoard();
        for (int i = 0; i < NO_OF_ROWS; i++){
            for (int j = 0; j < NO_OF_COLUMNS; j++){
                if (gameArray[i][j] == 0){
                    return false;
                }
            }
        }
        // Return true if there are no empty slots left
        return true;
    }

    /**
     * Check if there are any winning rows.
     * @return 1 if player 1 has a winning row, or 2 if player two has a winning row.
     * If neither has won return 0.
     */
    public int checkRows(){
        int[][] gameArray = getBoard();
        for (int rowNo = 0; rowNo < NO_OF_ROWS; rowNo++){
            for (int columnNo = 0; columnNo < NO_OF_COLUMNS - 3; columnNo++){
                Set<Integer> checkRowSet = new HashSet<>();
                for (int offset = 0; offset < 4; offset++){
                    checkRowSet.add(gameArray[rowNo][columnNo + offset]);
                }
                if (checkRowSet.size() == 1){
                    if (checkRowSet.contains(1)){
                        return 1;
                    }
                    else if (checkRowSet.contains(2)){
                        return 2;
                    }
                }
            }
        }
        return 0;
    }

    /**
     *
     * @return 1 if player 1 has a winning column, or 2 if player two has a winning column.
     * If neither has won return 0.
     */
    public int checkColumns() {
        int[][] gameArray = getBoard();
        for (int columnNo = 0; columnNo < NO_OF_COLUMNS; columnNo++){
            for (int rowNo = NO_OF_ROWS - 1; rowNo >= NO_OF_ROWS/2 ; rowNo--){
                Set<Integer> checkColumnSet = new HashSet<>();
                for (int offset = 0; offset < 4; offset++){
                    checkColumnSet.add(gameArray[rowNo - offset][columnNo]);
                }
                if (checkColumnSet.size() == 1){
                    if (checkColumnSet.contains(1)){
                        return 1;
                    }
                    else if (checkColumnSet.contains(2)){
                        return 2;
                    }
                }
            }
        }
        return 0;
    }

    /**
     *
     * @return 1 if player 1 has a winning diagonal, or 2 if player two has a winning diagonal.
     *If neither has won return 0.
     */
    private int checkDiagonals(){
        int[][] gameArray = getBoard();
        int checkTopRightToBottomLeftResult = checkTopRightToBottomLeft(gameArray);
        int checkTopLeftToBottomRightResult = checkTopLeftToBottomRight(gameArray);
        if (checkTopLeftToBottomRightResult == 1 || checkTopRightToBottomLeftResult == 1){
            return 1;
        }
        else if (checkTopLeftToBottomRightResult == 2 || checkTopRightToBottomLeftResult == 2){
            return 2;
        }
        return 0;
    }

    /**
     *
     * @param gameArray the array of the game board
     * @return 1 if player 1 has a winning diagonal, 2 if player 2 has a winning diagonal
     * Return 0 if neither has a winning diagonal
     */
    private int checkTopLeftToBottomRight(int[][] gameArray) {
        for (int columnNo = 0; columnNo <= NO_OF_COLUMNS - 4; columnNo++){
            for (int rowNo = 0; rowNo <= NO_OF_ROWS - 4; rowNo++){
               Set<Integer> checkCurrentDiagonalSet = new HashSet<>();
               for (int offset = 0; offset < 4; offset++) {
                   checkCurrentDiagonalSet.add(gameArray[rowNo + offset][columnNo + offset]);
               }
               if (checkCurrentDiagonalSet.size() == 1){
                   if (checkCurrentDiagonalSet.contains(1)){
                       return 1;
                   }
                   else if (checkCurrentDiagonalSet.contains(2)){
                       return 2;
                   }
               }
            }
        }
        return 0;
    }

    /**
     * Checks for winning diagonals in the top right to bottom left direction, (i.e. 2 o'clock to 7 o'clock direction)
     * @param gameArray the array of the game board
     * @return 1 of player 1 has met a winning diagonal, 2 if player 2 has a winning diagonal.
     * 0 if neither player has a winning diagonal
     */
    private int checkTopRightToBottomLeft(int[][] gameArray) {
        for (int columnNo = NO_OF_COLUMNS - 1; columnNo >= 3; columnNo--){
            for (int rowNo = 0; rowNo <= NO_OF_ROWS - 4; rowNo++){
                Set<Integer> checkCurrentDiagonalSet = new HashSet<>();
                for (int offset = 0; offset < 4; offset++) {
                    checkCurrentDiagonalSet.add(gameArray[rowNo + offset][columnNo - offset]);
                }
                if (checkCurrentDiagonalSet.size() == 1){
                    if (checkCurrentDiagonalSet.contains(1)){
                        return 1;
                    }
                    else if (checkCurrentDiagonalSet.contains(2)){
                        return 2;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Sets the main title text to show the game winner
     * @param gameResult the result from checkGameWin(), 1 represents a player 1 win,
     * 2 represents a player 2 win and 0 represents a draw
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
                mainGameTitleText.setText("Player 1 wins!");
                System.out.println("Game ended in player 1 victory");
            }
            case 2 -> {
                mainGameTitleText.setText("Player 2 wins!");
                System.out.println("Game ended in player 2 victory");
            }
        }
    }

    /**
     * Cleans up the variables used during a game,
     * and resets any strings that have been altered
     */
    public void cleanup(){
        System.out.println("Cleaning up!...");
        setLowestPlayableRow(-1);
        setCurrentPlayer("Player1");
        setPlayer1TotalTokens(0);
        setPlayer2TotalTokens(0);
        initialiseArray();
        mainGameTitleText.setText(GAME_TITLE);
        CurrentTurnText.setText(("It's Player 1's turn..."));
        CurrentTurnText.setVisible(true);
        MainGameGridPane.getChildren().removeIf(Circle.class :: isInstance);
        mainGamePlayAgainButton.setVisible(false);
        mainGamePlayAgainButton.setDisable(true);
    }

    @FXML
    private void mainGamePlayAgainButtonPressed() {
        cleanup();
    }

    @FXML
    private void column0ButtonPress() {
        setSelectedColumn(0);
        takeTurn(humanPlayer);
        takeTurn(computerPlayer);
    }

    @FXML
    private void column1ButtonPress() {
        setSelectedColumn(1);
        takeTurn(humanPlayer);
        takeTurn(computerPlayer);
    }

    @FXML
    private void column2ButtonPress() {
        setSelectedColumn(2);
        takeTurn(humanPlayer);
        takeTurn(computerPlayer);
    }

    @FXML
    private void column3ButtonPress() {
        setSelectedColumn(3);
        takeTurn(humanPlayer);
        takeTurn(computerPlayer);
    }

    @FXML
    private void column4ButtonPress() {
        setSelectedColumn(4);
        takeTurn(humanPlayer);
        takeTurn(computerPlayer);
    }

    @FXML
    private void column5ButtonPress() {
        setSelectedColumn(5);
        takeTurn(humanPlayer);
        takeTurn(computerPlayer);
    }

    @FXML
    private void column6ButtonPress() {
        setSelectedColumn(6);
        takeTurn(humanPlayer);
        takeTurn(computerPlayer);
    }

    public String getCurrentPlayer(){
        return currentPlayer;
    }

    public void setCurrentPlayer(String setPlayer){
        this.currentPlayer = setPlayer;
    }

    public int getLowestPlayableRow(){
        return lowestPlayableRow;
    }

    public void setLowestPlayableRow(int lowestPlayableRow) {
        this.lowestPlayableRow = lowestPlayableRow;
    }

    public Integer getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
    }


    public void setPlayer1TotalTokens(int player1TotalTokens) {
        this.player1TotalTokens = player1TotalTokens;
    }

    public void setPlayer2TotalTokens(int player2TotalTokens) {
        this.player2TotalTokens = player2TotalTokens;
    }

    public int[][] getBoard() {
        return board;
    }

    public static void main (String[] args){
        launch(args);
    }
}
