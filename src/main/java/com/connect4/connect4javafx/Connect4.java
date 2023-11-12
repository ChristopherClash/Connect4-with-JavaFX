package com.connect4.connect4javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Connect4 extends Application {
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
    private final int[][] mainGameGridPaneArray = new int[NO_OF_ROWS][NO_OF_COLUMNS];
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

    private void initialiseArray(){
        for (int i = 0; i < NO_OF_ROWS; i++){
            for (int j = 0; j < NO_OF_COLUMNS; j++){
                mainGameGridPaneArray[i][j] = 0;
            }
        }
    }
    private void createCircleAtNode(String currentPlayer, Color playerColour, int currentPlayerTotalTokens, int column, int row){
        Circle circleToken = new Circle(50);
        circleToken.setFill(playerColour);
        circleToken.setId(currentPlayer + "Token" + currentPlayerTotalTokens);
        MainGameGridPane.add(circleToken,column, row);
    }

    private void takeTurn(){
        mainGameInvalidMoveText.setVisible(false);
        int column = getSelectedColumn();
        setLowestPlayableRow(findLowestPlayableRow(column));
        int row = getLowestPlayableRow();
        String currentPlayer = getCurrentPlayer();
        if (row != -1) {
            if (currentPlayer.equals("Player1")) {
                int player1TotalTokens = getPlayer1TotalTokens();
                createCircleAtNode("Player1", Color.BLUE, player1TotalTokens, column, row);
                setPlayer1TotalTokens(player1TotalTokens + 1);
                mainGameGridPaneArray[row - 1][column] = 1;
                setCurrentPlayer("Player2");
                CurrentTurnText.setText(("It's Player 2's turn..."));
            } else if (currentPlayer.equals("Player2")){
                int player2TotalTokens = getPlayer2TotalTokens();
                createCircleAtNode("Player2", Color.RED, player2TotalTokens, column, row);
                setPlayer2TotalTokens(player2TotalTokens + 1);
                mainGameGridPaneArray[row - 1][column] = 2;
                setCurrentPlayer("Player1");
                CurrentTurnText.setText(("It's Player 1's turn..."));
            }
            //Only begin checking for met win conditions once player 1 has placed their fourth token
            if (getPlayer1TotalTokens() >= 4){
                checkGameWin();
            }
        } else {
            mainGameInvalidMoveText.setVisible(true);
            mainGameInvalidMoveText.setText("Error - that column is full!");}
    }

    private int findLowestPlayableRow(int selectedColumn) {
        for (int currentRow = MainGameGridPane.getRowCount() - 1; currentRow > 0; currentRow--) {
            if (mainGameGridPaneArray[currentRow - 1][selectedColumn] == 0){
                return currentRow;
            }
        }
        //If column is full, return -1
        return -1;
    }
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

    //Check if there are any remaining slots in the array that haven't been filled
    //If no slots are left and the other win conditions have not been met, the game has ended in a draw
    private boolean checkDraw(){
        int[][] gameArray = getMainGameGridPaneArray();
        for (int i = 0; i < NO_OF_ROWS; i++){
            for (int j = 0; j < NO_OF_COLUMNS; j++){
                if (gameArray[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }
    private int checkRows(){
        int[][] gameArray = getMainGameGridPaneArray();
        for (int i = 0; i < NO_OF_ROWS; i++){
            for (int j = 0; j < NO_OF_COLUMNS - 3; j++){
                Set<Integer> checkRowSet = new HashSet<>();
                checkRowSet.add(gameArray[i][j]);
                checkRowSet.add(gameArray[i][j + 1]);
                checkRowSet.add(gameArray[i][j + 2]);
                checkRowSet.add(gameArray[i][j + 3]);
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

    private int checkColumns() {
        int[][] gameArray = getMainGameGridPaneArray();
        for (int j = 0; j < NO_OF_COLUMNS; j++){
            for (int i = NO_OF_ROWS - 1; i >= NO_OF_ROWS/2 ; i--){
                Set<Integer> checkColumnSet = new HashSet<>();
                checkColumnSet.add(gameArray[i][j]);
                checkColumnSet.add(gameArray[i - 1][j]);
                checkColumnSet.add(gameArray[i - 2][j]);
                checkColumnSet.add(gameArray[i - 3][j]);
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

    private int checkDiagonals(){
        int[][] gameArray = getMainGameGridPaneArray();
        int checkBotLeftToTopRightResult = checkTopRowLeftColumn(gameArray);
        //int checkBotRightToTopLeftResult = checkBottomRowRightColumn(gameArray);
        if (checkBotLeftToTopRightResult != -1){
            return  checkBotLeftToTopRightResult;
        }
        //else if (checkBotRightToTopLeftResult != -1){
        //    return checkBotRightToTopLeftResult;
        //}
        return -1;
    }

    private int checkDiagonalSets(int column, int row, int[][] gameArray){
        Set<Integer> checkingSet = new HashSet<>();
        checkingSet.add(gameArray[row][column]);
        checkingSet.add(gameArray[row + 1][column - 1]);
        checkingSet.add(gameArray[row + 2][column - 2]);
        checkingSet.add(gameArray[row + 3][column - 3]);
        if (checkingSet.size() == 1){
            if (checkingSet.contains(1)){
                return 1;
            }
            else if (checkingSet.contains(2)){
                return 2;
            }
        }
        return -1;
    }

    //Check for diagonals that connect the top row to the rightmost column
    private int checkTopRowRightColumn(int[][] gameArray){
        return -1;
    }

    //Check for diagonals that connect the bottom row to the rightmost column
    private int checkBottomRowRightColumn(int[][] gameArray){
        for (int column = 1; column < 3; column++){
            int row = column;
            int currentColumn = NO_OF_COLUMNS - 1;
            while ((row + 3 < NO_OF_ROWS && currentColumn - 3 >= 0)){
                int hasWinnerBeenFound = checkDiagonalSets(currentColumn, row, gameArray);
                if (hasWinnerBeenFound != -1){
                    return hasWinnerBeenFound;
                }
                currentColumn--;
                row++;
            }
        }
        return -1;
    }

    //Check for diagonals that connect the bottom row to the leftmost column
    private int checkBottomRowLeftColumn(int[][] gameArray){
        return -1;
    }

    //Check for diagonals that connect the top row to the leftmost column
    private int checkTopRowLeftColumn(int[][] gameArray){
        for (int column = 3; column < NO_OF_COLUMNS; column++){
            int row = 1;
            int currentColumn = column;
            while (currentColumn - 3 >= 0 && row + 3 < NO_OF_ROWS) {
                int hasWinnerBeenFound = checkDiagonalSets(currentColumn, row, gameArray);
                if (hasWinnerBeenFound != -1) {
                    return hasWinnerBeenFound;
                }
                row++;
                currentColumn--;
            }
        }
        return -1;
    }

    private void showEndOfGame(int gameResult){
        CurrentTurnText.setVisible(false);
        mainGamePlayAgainButton.setVisible(true);
        mainGamePlayAgainButton.setDisable(false);
        switch (gameResult) {
            case 0:
                mainGameTitleText.setText("It's a draw!");
                break;
            case 1:
                mainGameTitleText.setText("Player 1 wins!");
                break;
            case 2:
                mainGameTitleText.setText("Player 2 wins!");
                break;
        }
    }
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
    private void mainGamePlayAgainButtonPressed(ActionEvent actionEvent) {
        cleanup();
    }
    @FXML
    private void column0ButtonPress(ActionEvent actionEvent) {
        setSelectedColumn(0);
        takeTurn();
    }

    @FXML
    private void column1ButtonPress(ActionEvent actionEvent) {
        setSelectedColumn(1);
        takeTurn();
    }

    @FXML
    private void Column2ButtonPressed(ActionEvent actionEvent) {
        setSelectedColumn(2);
        takeTurn();
    }

    @FXML
    private void Column3ButtonPressed(ActionEvent actionEvent) {
       setSelectedColumn(3);
       takeTurn();
    }

    @FXML
    private void Column4ButtonPressed(ActionEvent actionEvent) {
        setSelectedColumn(4);
        takeTurn();
    }

    @FXML
    private void Column5ButtonPressed(ActionEvent actionEvent) {
       setSelectedColumn(5);
       takeTurn();
    }

    @FXML
    private void Column6ButtonPressed(ActionEvent actionEvent) {
        setSelectedColumn(6);
        takeTurn();
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

    public int getPlayer1TotalTokens() {
        return player1TotalTokens;
    }

    public void setPlayer1TotalTokens(int player1TotalTokens) {
        this.player1TotalTokens = player1TotalTokens;
    }

    public int getPlayer2TotalTokens() {
        return player2TotalTokens;
    }

    public void setPlayer2TotalTokens(int player2TotalTokens) {
        this.player2TotalTokens = player2TotalTokens;
    }

    public int[][] getMainGameGridPaneArray() {
        return mainGameGridPaneArray;
    }

    public static void main (String[] args){
        launch(args);
    }
}
