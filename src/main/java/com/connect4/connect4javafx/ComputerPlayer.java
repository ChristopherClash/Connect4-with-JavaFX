package com.connect4.connect4javafx;

import javafx.scene.paint.Color;

public class ComputerPlayer extends Player{
    private final MinimaxAgent miniMaxAgent;
    public ComputerPlayer(String name, Color color) {
        super(name, color);
        this.miniMaxAgent = new MinimaxAgent();
    }
    /**
     * Generates a move based on the current state of the board and the selected column.
     * Calls the minimax algorithm to determine the best move to make.
     *
     * @param  board            the current state of the game board
     * @return                  an array representing the move to be made
     */
    @Override
    public int[] takeTurn(int[][] board) {
        return miniMaxAgent.takeTurn(board);
    }
}
