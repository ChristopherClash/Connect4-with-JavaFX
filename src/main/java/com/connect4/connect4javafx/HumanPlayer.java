package com.connect4.connect4javafx;

import javafx.scene.paint.Color;

import java.util.Random;

public class HumanPlayer extends Player{
    public HumanPlayer(String name, Color color){
        super(name, color);
    }

    /**
     * Generates a move based on the current state of the board. Uses a random column selection.
     * findLowestPlayableRow is used to find the lowest playable row in the selected column.
     *
     * @param  board  a 2D array representing the current game board
     * @return        an array containing the row and column selected by the AI player
     */
    @Override
    public int[] takeTurn(int[][] board) {
        Random random = new Random();
        int selectedColumn = random.nextInt();
        return new int[] {findLowestPlayableRow(board, selectedColumn), selectedColumn};
    }

    /**
     * Generates a move based on the current state of the board and the selected column.
     * findLowestPlayableRow is used to find the lowest playable row in the selected column.
     *
     * @param  board            the current state of the game board
     * @param  selectedColumn   the column selected by the player
     * @return                  an array representing the move to be made
     */
    public int[] takeTurn(int[][] board, int selectedColumn) {
        int[] move = new int[] {findLowestPlayableRow(board, selectedColumn), selectedColumn};
        if (findLowestPlayableRow(board, selectedColumn) == -1) {
            return new int[] {-1, -1};
        } else {
        return move;
    }
        }
}
