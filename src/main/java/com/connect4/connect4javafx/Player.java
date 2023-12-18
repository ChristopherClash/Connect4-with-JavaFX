package com.connect4.connect4javafx;

import javafx.scene.paint.Color;

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

    /**
     * This method takes the current state of the game board and returns the next move to be made by the player.
     *
     * @param board The current state of the game board.
     * @return An array of integers representing the next move to be made by the player.
     */
    public abstract int[] takeTurn(int[][] board);

    /**
     * Finds the lowest playable row for a given column on the board.
     *
     * @param  board   the 2D array representing the game board
     * @param  column  the column index (0-based) to check
     * @return         the lowest playable row index (1-based) where the token can be placed,
     *                 or -1 if the column is full
     */
    public int findLowestPlayableRow(int[][] board, int column) {
        for (int row = Connect4Game.getNoOfRows() - 1; row >= 0; row--) {
            if (board[row][column] == 0) {
                return row + 1;  // Return the row index (1-based) where the token can be placed
            }
        }
        // If the column is full, return -1
        return -1;
    }
}
