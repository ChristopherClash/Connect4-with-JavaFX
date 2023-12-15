package com.connect4.connect4javafx;

import javafx.scene.paint.Color;

import static com.connect4.connect4javafx.GameController.NO_OF_ROWS;

public class HumanPlayer extends Player{
    public HumanPlayer(String name, Color color){
        super(name, color);
    }

    public int[] takeTurn(int[][] board, int selectedColumn){
        return new int[]{findLowestPlayableRow(board, selectedColumn), selectedColumn};
    }

    private int findLowestPlayableRow(int[][] board, int column) {
        for (int row = NO_OF_ROWS - 1; row >= 0; row--) {
            if (board[row][column] == 0) {
                return row + 1;  // Return the row index (1-based) where the token can be placed
            }
        }
        // If the column is full, return -1 or handle accordingly
        return -1;
    }
}
