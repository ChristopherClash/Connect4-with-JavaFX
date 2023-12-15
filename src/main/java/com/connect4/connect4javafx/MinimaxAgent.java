package com.connect4.connect4javafx;

import static com.connect4.connect4javafx.GameController.NO_OF_ROWS;

public class MinimaxAgent {
    int maxDepth = 8;
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;

    public int[] takeTurn(int[][] board) {
        int[] bestMove = minimax(board, maxDepth, true, alpha, beta);
        if (bestMove[1] >= 0) {
            return new int[]{findLowestPlayableRow(board, bestMove[1]), bestMove[1]};
        } else {
            System.out.println("No move found");
            return new int[]{findLowestPlayableRow(board, 0), 0};
        }
    }

    private int[] minimax(int[][] board, int depth, boolean maximizingPlayer, int alpha, int beta) {
        int[] bestMove = new int[]{maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE, -1};

        if ((depth == 0) || (isGameOver(board))) {
            return new int[]{evaluate(board), -1, -1};
        }

        int playerValue = maximizingPlayer ? 2 : 1;

        for (int column = 0; column < GameController.NO_OF_COLUMNS; column++) {
            if (isColumnFull(board, column)) {
                continue;
            }

            int row = findLowestPlayableRow(board, column);
            board[row - 1][column] = playerValue;

            if (isWinningMove(board, column, row - 1)) {
                board[row - 1][column] = 0; // Undo the move
                return new int[]{evaluate(board), column, row};
            }

            // !maximizingPlayer because it's the other player's turn
            int score = minimax(board, depth - 1, !maximizingPlayer, alpha, beta)[0];

            board[row - 1][column] = 0; // Undo the move

            if ((maximizingPlayer && score > alpha) || (!maximizingPlayer && score < beta)) {
                if (maximizingPlayer) {
                    alpha = score;
                } else {
                    beta = score;
                }
                bestMove[0] = score;
                bestMove[1] = column;
            }
            if (alpha >= beta) {
                break;
            }
        }
        return bestMove;
    }



    private boolean isGameOver(int[][] board) {
        return checkGameWin(board) || checkDraw(board);
    }

    private boolean checkGameWin(int[][] board) {
        return checkRows(board) || checkColumns(board) || checkDiagonals(board);
    }

    private boolean checkDiagonals(int[][] board) {
        return Connect4Game.checkDiagonals(board) == 2;
    }

    private boolean checkColumns(int[][] board) {
        return Connect4Game.checkColumns(board) == 2;
    }

    private boolean checkRows(int[][] board) {
        return Connect4Game.checkRows(board) == 2;
    }

    private boolean isColumnFull(int[][] board, int column) {
        return board[0][column] != 0;
    }

    private int findLowestPlayableRow(int[][] board, int column) {
        for (int row = NO_OF_ROWS - 1; row >= 0; row--) {
            if (board[row][column] == 0) {
                return row + 1;  // Return the row index (1-based) where the token can be placed
            }
        }
        // If the column is full, return -1 or handle accordingly
        return -1;
    }

    private int evaluate(int[][] board) {
        // Evaluation function for the current board position
        int computerPlayer = 2; // RED
        int humanPlayer = 1; // BLUE

        int score = 0;

        // Check rows, columns, and diagonals for potential winning combinations
        score += evaluateForPlayer(board, computerPlayer);
        score -= evaluateForPlayer(board, humanPlayer);

        return score;
    }

    private int evaluateForPlayer(int[][] board, int player) {
        int score = 0;

        score += evaluateLine(board, player);
        return score;
    }

    private int evaluateLine(int[][] board, int player) {
        int score = 0;

        // Evaluate rows
        for (int row = 0; row < GameController.NO_OF_ROWS; row++) {
            for (int col = 0; col < GameController.NO_OF_COLUMNS - 3; col++) {
                int value1 = board[row][col];
                int value2 = board[row][col + 1];
                int value3 = board[row][col + 2];
                int value4 = board[row][col + 3];

                score += evaluatePosition(value1, value2, value3, value4, player);
            }
        }

        // Evaluate columns
        for (int col = 0; col < GameController.NO_OF_COLUMNS; col++) {
            for (int row = 0; row < GameController.NO_OF_ROWS - 3; row++) {
                int value1 = board[row][col];
                int value2 = board[row + 1][col];
                int value3 = board[row + 2][col];
                int value4 = board[row + 3][col];

                score += evaluatePosition(value1, value2, value3, value4, player);
            }
        }

        // Evaluate diagonals
        for (int col = 0; col <= GameController.NO_OF_COLUMNS - 4; col++) {
            for (int row = 0; row < GameController.NO_OF_ROWS - 4; row++) {
                int value1 = board[row][col];
                int value2 = board[row + 1][col + 1];
                int value3 = board[row + 2][col + 2];
                int value4 = board[row + 3][col + 3];

                score += evaluatePosition(value1, value2, value3, value4, player);
            }
        }

        for (int col = GameController.NO_OF_COLUMNS - 1; col >= 3; col--) {
            for (int row = 0; row <= GameController.NO_OF_ROWS - 4; row++) {
                int value1 = board[row][col];
                int value2 = board[row + 1][col - 1];
                int value3 = board[row + 2][col - 2];
                int value4 = board[row + 3][col - 3];

                score += evaluatePosition(value1, value2, value3, value4, player);
            }
        }
        return score;
    }

    private int evaluatePosition(int a, int b, int c, int d, int player) {
        int score = 0;

        // Evaluate a position based on the number of computer player and human player tokens
        if (a == player) score++;
        else if (a != 0) score--;

        if (b == player) score++;
        else if (b != 0) score--;

        if (c == player) score++;
        else if (c != 0) score--;

        if (d == player) score++;
        else if (d != 0) score--;

        // Give higher scores for positions with more computer player tokens
        if (score == 2) score *= 2;
        else if (score == 3) score *= 5;

        return score;
    }

    private boolean isWinningMove(int[][] board, int column, int row) {
        int piecePlayer = board[row][column];

        if (checkVertical(board, column, row, piecePlayer)) {
            return true;
        }

        if (checkHorizontal(board, column, row, piecePlayer)) {
            return true;
        }

        return checkDiagonals(board, column, row, piecePlayer);
    }

    private boolean checkDiagonals(int[][] board, int column, int row, int piecePlayer) {
        return checkDiagonal(board, column, row, 1, piecePlayer) || checkDiagonal(board, column, row, -1, piecePlayer);
    }

    private boolean checkDiagonal(int[][] board, int column, int row, int rowDirection, int piecePlayer) {
        int count = 1;

        for (int i = 1; i < 4; i++) {
            int columnNext = column - i;
            int rowNext = row - i * rowDirection;

            if (columnNext >= 0 && columnNext < GameController.NO_OF_COLUMNS && rowNext >= 0 && rowNext < GameController.NO_OF_ROWS && board[rowNext][columnNext] == piecePlayer) {
                count++;
            } else {
                break;
            }
        }

        for (int i = 1; i < 4; i++) {
            int columnNext = column + i;
            int rowNext = row + i * rowDirection;

            if (columnNext >= 0 && columnNext < GameController.NO_OF_COLUMNS && rowNext >= 0 && rowNext < GameController.NO_OF_ROWS && board[rowNext][columnNext] == piecePlayer) {
                count++;
            } else {
                break;
            }
        }
        return count >= 4;
    }

    private boolean checkHorizontal(int[][] board, int column, int row, int piecePlayer) {
        int count = 1;
        for (int i = column - 1; i >= 0 && board[row][i] == piecePlayer; i--) {
            count++;
        }

        for (int i = column + 1; i < GameController.NO_OF_COLUMNS && board[row][i] == piecePlayer; i++) {
            count++;
        }

        return count >= 4;
    }

    private boolean checkVertical(int[][] board, int column, int row, int piecePlayer) {
        int count = 1;
        for (int i = row - 1; i >= 0 && board[i][column] == piecePlayer; i--) {
            count++;
        }

        for (int i = row; i < GameController.NO_OF_ROWS && board[i][column] == piecePlayer; i++) {
            count++;
        }
        return count >= 4;
    }


    private boolean checkDraw(int[][] board) {
        for (int column = 0; column < GameController.NO_OF_COLUMNS; column++) {
            if (!isColumnFull(board, column)) {
                return false;
            }
        }
        return true;
    }

}
