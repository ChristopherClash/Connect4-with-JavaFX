package com.connect4.connect4javafx;

import javafx.scene.paint.Color;

public class Player {

    private String playerName;
    private Color playerColor;
    private int totalTokens;
    private int playerNumber;

    public Player(String playerName, Color playerColor, int playerNumber) {
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.playerNumber = playerNumber;
        this.totalTokens = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
