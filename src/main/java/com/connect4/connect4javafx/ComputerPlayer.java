package com.connect4.connect4javafx;

import javafx.scene.paint.Color;

public class ComputerPlayer extends Player{
    private final MinimaxAgent miniMaxAgent;
    private int totalTokens;

    public ComputerPlayer(String name, Color color, int playerNumber) {
        super(name, color, playerNumber);
        this.miniMaxAgent = new MinimaxAgent();
        this.totalTokens = 0;
    }

    public int takeTurn(int[][] board) {
        return miniMaxAgent.takeTurn(board);
    }
}
