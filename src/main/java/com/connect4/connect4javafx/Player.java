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

    /**
     * Returns the player name.
     *
     * @return  the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the player color.
     *
     * @return  the player color
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /**
     * Returns the total number of tokens.
     *
     * @return the total number of tokens
     */
    public int getTotalTokens() {
        return totalTokens;
    }

    /**
     * Increments the total number of tokens.
     */
    public void incrementTotalTokens() {
        totalTokens++;
    }

    /**
     * This method takes the current state of the game board and returns the next move to be made by the player.
     *
     * @param board a 2D array representing the game board
     *
     * @return An array of integers representing the next move to be made by the player.
     */
    public abstract int[] takeTurn(int[][] board);

    /**
     * Finds the lowest playable row for a given column on the board.
     *
     * @param  board   a 2D array representing the game board
     * @param  column  the column index to check
     *
     * @return         the lowest playable row index where the token can be placed,
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
