package com.connect4.connect4javafx;

import javafx.scene.paint.Color;

public class ComputerPlayer extends Player{
    private final MinimaxAgent miniMaxAgent;
    public ComputerPlayer(String name, Color color) {
        super(name, color);
        this.miniMaxAgent = new MinimaxAgent();
        this.setTotalTokens(0);
    }

    public int[] takeTurn(int[][] board) {
        return miniMaxAgent.takeTurn(board);
    }
}
