package com.connect4.connect4javafx;

import java.util.HashSet;
import java.util.Set;

import static com.connect4.connect4javafx.GameController.*;

public class Connect4Game {
    private final int[][] board = new int[NO_OF_ROWS][NO_OF_COLUMNS];
    private final ComputerPlayer computerPlayer;
    private final HumanPlayer humanPlayer;
    private int selectedColumn;
    private final GameController gameController;

    public Connect4Game(ComputerPlayer computerPlayer, HumanPlayer humanPlayer, GameController gameController) {
        this.computerPlayer = computerPlayer;
        this.humanPlayer = humanPlayer;
        this.gameController = gameController;
        initialiseArray();
    }
    private void initialiseArray(){
        for (int i = 0; i < NO_OF_ROWS; i++){
            for (int j = 0; j < NO_OF_COLUMNS; j++){
                board[i][j] = 0;
            }
        }
    }

    public boolean takeTurn(Player player) {
        int[] move;
        if (player == humanPlayer){
            move = humanPlayer.takeTurn(board, selectedColumn);
            if (!isValidMove(move)){
                return false;
            }
            gameController.createCircleAtNode(player, move[0], selectedColumn);
            player.incrementTotalTokens();
            board[move[0] - 1][move[1]] = 1;
            return true;
        } else if (player == computerPlayer){
            move = computerPlayer.takeTurn(board);
            gameController.createCircleAtNode(player, move[0], move[1]);
            player.incrementTotalTokens();
            board[move[0] - 1][move[1]] = 2;
            return true;
        }
        return false;
    }

    private boolean isValidMove(int[] move){
        return !(move[0] == -1);
    }
    public int checkGameWin() {
        int checkColumnsResult = checkColumns(board);
        int checkRowsResult = checkRows(board);
        int checkDiagonalsResult = checkDiagonals(board);

        //If player 1 has met any of the win conditions
        if (checkColumnsResult == 1 || checkRowsResult == 1 || checkDiagonalsResult == 1){
            return (1);
        }
        //If player 2 has met any of the win conditions
        else if (checkColumnsResult == 2 || checkRowsResult == 2 || checkDiagonalsResult == 2){
            return (2);
        }
        //If no win conditions have been met, check for a draw
        else if (checkDraw()){
            return (0);
        }
        return -1;
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
    public static int checkRows(int[][] board){
        for (int rowNo = 0; rowNo < NO_OF_ROWS; rowNo++){
            for (int columnNo = 0; columnNo < NO_OF_COLUMNS - 3; columnNo++){
                Set<Integer> checkRowSet = new HashSet<>();
                for (int offset = 0; offset < 4; offset++){
                    checkRowSet.add(board[rowNo][columnNo + offset]);
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
    public static int checkColumns(int[][] board) {
        for (int columnNo = 0; columnNo < NO_OF_COLUMNS; columnNo++){
            for (int rowNo = NO_OF_ROWS - 1; rowNo >= NO_OF_ROWS/2 ; rowNo--){
                Set<Integer> checkColumnSet = new HashSet<>();
                for (int offset = 0; offset < 4; offset++){
                    checkColumnSet.add(board[rowNo - offset][columnNo]);
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
    public static int checkDiagonals(int[][] board) {
        int checkTopRightToBottomLeftResult = checkTopRightToBottomLeft(board);
        int checkTopLeftToBottomRightResult = checkTopLeftToBottomRight(board);
        if (checkTopLeftToBottomRightResult == 1 || checkTopRightToBottomLeftResult == 1){
            return 1;
        }
        else if (checkTopLeftToBottomRightResult == 2 || checkTopRightToBottomLeftResult == 2){
            return 2;
        }
        return 0;
    }

    private int[][] getBoard() {
        return board;
    }

    /**
     *
     * @param gameArray the array of the game board
     * @return 1 if player 1 has a winning diagonal, 2 if player 2 has a winning diagonal
     * Return 0 if neither has a winning diagonal
     */
    private static int checkTopLeftToBottomRight(int[][] gameArray) {
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
    private static int checkTopRightToBottomLeft(int[][] gameArray) {
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

    public void setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
    }
}
