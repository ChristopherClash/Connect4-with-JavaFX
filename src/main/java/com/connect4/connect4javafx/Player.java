package com.connect4.connect4javafx;

import javafx.scene.paint.Color;

import static com.connect4.connect4javafx.GameController.NO_OF_ROWS;

public abstract class Player {

    protected final String playerName;
    protected final Color playerColor;
    protected int totalTokens;

    public Player(String playerName, Color playerColor) {
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.totalTokens = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void incrementTotalTokens() {
        totalTokens++;
    }

    public abstract int[] takeTurn(int[][] board);
    public int findLowestPlayableRow(int[][] board, int column) {
        for (int row = NO_OF_ROWS - 1; row >= 0; row--) {
            if (board[row][column] == 0) {
                return row + 1;  // Return the row index (1-based) where the token can be placed
            }
        }
        // If the column is full, return -1
        return -1;
    }
}
