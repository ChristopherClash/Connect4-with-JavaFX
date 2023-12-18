package com.connect4.connect4javafx;

import javafx.scene.paint.Color;

import java.util.Random;

public class HumanPlayer extends Player{
    public HumanPlayer(String name, Color color){
        super(name, color);
    }

    @Override
    public int[] takeTurn(int[][] board) {
        Random random = new Random();
        int selectedColumn = random.nextInt(8);
        return new int[] {findLowestPlayableRow(board, selectedColumn), selectedColumn};
    }

    public int[] takeTurn(int[][] board, int selectedColumn) {
        int[] move = new int[] {findLowestPlayableRow(board, selectedColumn), selectedColumn};
        if (findLowestPlayableRow(board, selectedColumn) == -1) {
            return new int[] {-1, -1};
        } else {
        return move;
    }
        }
}
