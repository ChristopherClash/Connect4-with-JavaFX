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
    private final int[][] mainGameGridPaneArray = new int[NO_OF_COLUMNS][NO_OF_ROWS];
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
        for (int i = 0; i < NO_OF_COLUMNS; i++){
            for (int j = 0; j < NO_OF_ROWS; j++){
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
                mainGameGridPaneArray[column][row - 1] = 1;
                setCurrentPlayer("Player2");
                CurrentTurnText.setText(("It's Player 2's turn..."));
            } else if (currentPlayer.equals("Player2")){
                int player2TotalTokens = getPlayer2TotalTokens();
                createCircleAtNode("Player2", Color.RED, player2TotalTokens, column, row);
                setPlayer2TotalTokens(player2TotalTokens + 1);
                mainGameGridPaneArray[column][row - 1] = 2;
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
            if (mainGameGridPaneArray[selectedColumn][currentRow - 1] == 0){
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
        for (int i = 0; i < NO_OF_COLUMNS; i++){
            for (int j = 0; j < NO_OF_ROWS; j++){
                if (gameArray[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }
    private int checkRows(){
        int[][] gameArray = getMainGameGridPaneArray();
        for (int rowNo = 0; rowNo < NO_OF_ROWS; rowNo++){
            for (int columnNo = 0; columnNo < NO_OF_COLUMNS - 3; columnNo++){
                Set<Integer> checkRowSet = new HashSet<>();
                for (int offset = 0; offset < 4; offset++){
                    checkRowSet.add(gameArray[columnNo + offset][rowNo]);
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

    private int checkColumns() {
        int[][] gameArray = getMainGameGridPaneArray();
        for (int columnNo = 0; columnNo < NO_OF_COLUMNS; columnNo++){
            for (int rowNo = NO_OF_ROWS - 1; rowNo >= NO_OF_ROWS/2 ; rowNo--){
                Set<Integer> checkColumnSet = new HashSet<>();
                for (int offset = 0; offset < 4; offset++){
                    checkColumnSet.add(gameArray[columnNo][rowNo - offset]);
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

    private int checkDiagonals(){
        int[][] gameArray = getMainGameGridPaneArray();
        int checkBottomLeftToTopRightResult = checkBottomLeftToTopRight(gameArray);
        int checkTopLeftToBottomRightResult = checkTopLeftToBottomRight(gameArray);
        if (checkTopLeftToBottomRightResult == 1 || checkBottomLeftToTopRightResult == 1){
            return 1;
        }
        else if (checkTopLeftToBottomRightResult == 2 || checkBottomLeftToTopRightResult == 2){
            return 2;
        }
        return 0;
    }

    private int checkTopLeftToBottomRight(int[][] gameArray) {
        for (int columnNo = 0; columnNo <= NO_OF_COLUMNS - 4; columnNo++){
            for (int rowNo = 0; rowNo <= NO_OF_ROWS - 4; rowNo++){
               Set<Integer> checkCurrentDiagonalSet = new HashSet<>();
               for (int offset = 0; offset < 4; offset++) {
                   checkCurrentDiagonalSet.add(gameArray[columnNo + offset][rowNo + offset]);
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

    private int checkBottomLeftToTopRight(int[][] gameArray) {
        return 0;
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
    private void mainGamePlayAgainButtonPressed() {
        cleanup();
    }
    @FXML
    private void column0ButtonPress() {
        setSelectedColumn(0);
        takeTurn();
    }

    @FXML
    private void column1ButtonPress() {
        setSelectedColumn(1);
        takeTurn();
    }

    @FXML
    private void Column2ButtonPressed() {
        setSelectedColumn(2);
        takeTurn();
    }

    @FXML
    private void Column3ButtonPressed() {
       setSelectedColumn(3);
       takeTurn();
    }

    @FXML
    private void Column4ButtonPressed() {
        setSelectedColumn(4);
        takeTurn();
    }

    @FXML
    private void Column5ButtonPressed() {
       setSelectedColumn(5);
       takeTurn();
    }

    @FXML
    private void Column6ButtonPressed() {
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
