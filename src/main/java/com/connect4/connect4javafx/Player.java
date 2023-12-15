package com.connect4.connect4javafx;

import javafx.scene.paint.Color;

public class Player {

    private final String playerName;
    private final Color playerColor;
    private int totalTokens;

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

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }
}
