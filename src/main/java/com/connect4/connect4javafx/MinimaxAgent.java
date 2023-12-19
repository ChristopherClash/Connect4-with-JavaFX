package com.connect4.connect4javafx;

import java.util.Random;
public class MinimaxAgent {
    int maxDepth = 8;
    int alpha = Integer.MIN_VALUE;
    int beta = Integer.MAX_VALUE;

    /**
     * Generates a move based on the current state of the board and the selected column.
     * Calls the minimax algorithm to determine the best move to make.
     * If no move is found, returns a move using a random column.
     *
     * @param  board            the current state of the game board
     *
     * @return                  an array representing the move to be made
     */
    public int[] takeTurn(int[][] board) {
        int[] bestMove = minimax(board, maxDepth, true, alpha, beta);
        if (bestMove[1] >= 0) {
            return new int[]{findLowestPlayableRow(board, bestMove[1]), bestMove[1]};
        } else {
            System.out.println("No move found, picking random column...");
            Random random = new Random();
            int selectRandomColumn = random.nextInt(Connect4Game.getNoOfColumns());
            return new int[]{findLowestPlayableRow(board, selectRandomColumn), selectRandomColumn};
        }
    }

    /**
     * Finds the best move for the current player using the minimax algorithm.
     *
     * @param  board              the game board represented as a 2D array
     * @param  depth              the current depth of the search
     * @param  maximizingPlayer   true if the current player is maximizing, false otherwise
     * @param  alpha              the best value for the maximizing player
     * @param  beta               the best value for the minimizing player
     *
     * @return                    an array containing the score and the column of the best move
     */
    private int[] minimax(int[][] board, int depth, boolean maximizingPlayer, int alpha, int beta) {
        int[] bestMove = new int[]{maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE, -1};

        // Base case
        if ((depth == 0) || (isGameOver(board))) {
            return new int[]{evaluate(board), -1};
        }

        // 1 for computer, 2 for human
        int playerValue = maximizingPlayer ? 2 : 1;

        // Loop through each column
        for (int column = 0; column < Connect4Game.getNoOfColumns(); column++) {
            if (isColumnFull(board, column)) {
                continue;
            }

            // Find the lowest playable row
            int row = findLowestPlayableRow(board, column);
            board[row - 1][column] = playerValue;

            // Check if it's a winning move
            if (isWinningMove(board, column, row - 1)) {
                board[row - 1][column] = 0; // Undo the move
                return new int[]{evaluate(board), column};
            }

            // !maximizingPlayer because it's the other player's turn
            int score = minimax(board, depth - 1, !maximizingPlayer, alpha, beta)[0];

            board[row - 1][column] = 0; // Undo the move

            // Update the best move
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

    /**
     * Determines whether the game is over based on the current state of the board.
     *
     * @param  board  a 2D array representing the game board
     *
     * @return true if the game is over, false otherwise
     */
    private boolean isGameOver(int[][] board) {
        return checkGameWin(board) || checkDraw(board);
    }

    /**
     * Checks if the game is won based on the current state of the board.
     * Checks the three possible win conditions: rows, columns, and diagonals.
     *
     * @param  board  a 2D array representing the game board
     *
     * @return        true if the game is won, false otherwise
     */
    private boolean checkGameWin(int[][] board) {
        return checkRows(board) || checkColumns(board) || checkDiagonals(board);
    }

    /**
     * Checks the diagonals to see if there is a winner.
     * 2 is used in the array to indicate that the computer has won.
     * If 2 returned by Connect4Game.checkDiagonals, the computer has won.
     *
     * @param  board  a 2D array representing the game board
     *
     * @return        true if the computer has won, false otherwise
     */
    private boolean checkDiagonals(int[][] board) {
        return Connect4Game.checkDiagonals(board) == 2;
    }

    /**
     * Checks the columns to see if there is a winner.
     * 2 is used in the array to indicate that the computer has won.
     * If 2 returned by Connect4Game.checkColumns, the computer has won.
     *
     * @param  board  a 2D array representing the game board
     *
     * @return        true if the computer has won, false otherwise
     */
    private boolean checkColumns(int[][] board) {
        return Connect4Game.checkColumns(board) == 2;
    }

    /**
     * Checks the rows to see if there is a winner.
     * 2 is used in the array to indicate that the computer has won.
     * If 2 returned by Connect4Game.checkRows, the computer has won.
     *
     * @param  board    a 2D array representing the game board
     *
     * @return          true if the computer has won, false otherwise
     */
    private boolean checkRows(int[][] board) {
        return Connect4Game.checkRows(board) == 2;
    }

    /**
     * Checks if a specific column in the board is full.
     *
     * @param  board   a 2D array representing the game board
     * @param  column  the column index to check
     *
     * @return         true if the column is full, false otherwise
     */
    private boolean isColumnFull(int[][] board, int column) {
        return board[0][column] != 0;
    }

    /**
     * Finds the lowest playable row for a given column in the board.
     *
     * @param  board   a 2D array representing the game board
     * @param  column  the column index to check
     *
     * @return         the lowest playable row for the given column or -1 if the column is full
     */
    private int findLowestPlayableRow(int[][] board, int column) {
        for (int row = Connect4Game.getNoOfRows() - 1; row >= 0; row--) {
            if (board[row][column] == 0) {
                return row + 1;  // Return the row index (1-based) where the token can be placed
            }
        }
        // If the column is full, return -1 or handle accordingly
        return -1;
    }

    /**
     * Evaluation function for the current board position
     *
     * @param  board  a 2D array representing the game board
     *
     * @return        the score representing the evaluation of the board position
     */
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

    /**
     * Evaluates the given board for the specified player and returns the score.
     *
     * @param  board   a 2D array representing the game board
     * @param  player  the player for whom the board is being evaluated
     *
     * @return         the score of the evaluation
     */
    private int evaluateForPlayer(int[][] board, int player) {
        int score = 0;
        score += evaluateLine(board, player);
        return score;
    }

    /**
     * Evaluates the given game board for the specified player and returns the score.
     *
     * @param  board  a 2D array representing the game board
     * @param  player the player for whom the board is evaluated
     *
     * @return        the score obtained by the player after evaluating the board
     */
    private int evaluateLine(int[][] board, int player) {
        int score = 0;

        // Evaluate rows
        for (int row = 0; row < Connect4Game.getNoOfRows(); row++) {
            for (int col = 0; col < Connect4Game.getNoOfColumns() - 3; col++) {
                int value1 = board[row][col];
                int value2 = board[row][col + 1];
                int value3 = board[row][col + 2];
                int value4 = board[row][col + 3];

                score += evaluatePosition(value1, value2, value3, value4, player);
            }
        }

        // Evaluate columns
        for (int col = 0; col < Connect4Game.getNoOfColumns(); col++) {
            for (int row = 0; row < Connect4Game.getNoOfRows() - 3; row++) {
                int value1 = board[row][col];
                int value2 = board[row + 1][col];
                int value3 = board[row + 2][col];
                int value4 = board[row + 3][col];

                score += evaluatePosition(value1, value2, value3, value4, player);
            }
        }

        // Evaluate diagonals from top-left to bottom-right
        for (int col = 0; col <= Connect4Game.getNoOfColumns() - 4; col++) {
            for (int row = 0; row < Connect4Game.getNoOfRows() - 4; row++) {
                int value1 = board[row][col];
                int value2 = board[row + 1][col + 1];
                int value3 = board[row + 2][col + 2];
                int value4 = board[row + 3][col + 3];

                score += evaluatePosition(value1, value2, value3, value4, player);
            }
        }

        // Evaluate diagonals from top-right to bottom-left
        for (int col = Connect4Game.getNoOfColumns() - 1; col >= 3; col--) {
            for (int row = 0; row <= Connect4Game.getNoOfRows() - 4; row++) {
                int value1 = board[row][col];
                int value2 = board[row + 1][col - 1];
                int value3 = board[row + 2][col - 2];
                int value4 = board[row + 3][col - 3];

                score += evaluatePosition(value1, value2, value3, value4, player);
            }
        }
        return score;
    }

    /**
     * Evaluates a position based on the number of computer player and human player tokens.
     *
     * @param  a     the value of the first token
     * @param  b     the value of the second token
     * @param  c     the value of the third token
     * @param  d     the value of the fourth token
     * @param  player  the player whose tokens are being evaluated
     *
     * @return          the score of the position
     */
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

    /**
     * Determines whether a move is a winning move on the game board.
     *
     * @param  board   a 2D array representing the game board
     * @param  column  the column of the move
     * @param  row     the row of the move
     *
     * @return         true if the move is a winning move, false otherwise
     */
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

    /**
     * Checks if there is a win on either of the diagonals of the game board.
     *
     * @param  board         a 2D array representing the game board
     * @param  column        the column index of the last piece placed
     * @param  row           the row index of the last piece placed
     * @param  piecePlayer   the player ID of the last piece placed
     *
     * @return               true if there is a win on either of the diagonals, false otherwise
     */
    private boolean checkDiagonals(int[][] board, int column, int row, int piecePlayer) {
        return checkDiagonal(board, column, row, 1, piecePlayer) || checkDiagonal(board, column, row, -1, piecePlayer);
    }

    /**
     * Checks if there is a diagonal line of 4 pieces of the same player on the game board,
     * starting from the given column and row, and moving in the given row direction.
     *
     * @param  board         a 2D array representing the game board
     * @param  column        the starting column index
     * @param  row           the starting row index
     * @param  rowDirection  the direction to move in rows (1 for down, -1 for up)
     * @param  piecePlayer   the player's piece to check for
     *
     * @return               true if there is a diagonal line of 4 pieces, false otherwise
     */
    private boolean checkDiagonal(int[][] board, int column, int row, int rowDirection, int piecePlayer) {
        int count = 1;

        for (int i = 1; i < 4; i++) {
            int columnNext = column - i;
            int rowNext = row - i * rowDirection;

            if (columnNext >= 0 && columnNext < Connect4Game.getNoOfColumns() && rowNext >= 0 && rowNext < Connect4Game.getNoOfRows() && board[rowNext][columnNext] == piecePlayer) {
                count++;
            } else {
                break;
            }
        }

        for (int i = 1; i < 4; i++) {
            int columnNext = column + i;
            int rowNext = row + i * rowDirection;

            if (columnNext >= 0 && columnNext < Connect4Game.getNoOfColumns() && rowNext >= 0 && rowNext < Connect4Game.getNoOfRows() && board[rowNext][columnNext] == piecePlayer) {
                count++;
            } else {
                break;
            }
        }
        return count >= 4;
    }

    /**
     * Checks if there are at least 4 consecutive pieces of the same player
     * horizontally on the game board.
     *
     * @param  board         a 2D array representing the game board
     * @param  column        the column index of the piece being checked
     * @param  row           the row index of the piece being checked
     * @param  piecePlayer   the player identifier for the piece being checked
     *
     * @return               true if there are at least 4 consecutive pieces of the same player horizontally, false otherwise
     */
    private boolean checkHorizontal(int[][] board, int column, int row, int piecePlayer) {
        int count = 1;
        for (int i = column - 1; i >= 0 && board[row][i] == piecePlayer; i--) {
            count++;
        }

        for (int i = column + 1; i < Connect4Game.getNoOfColumns() && board[row][i] == piecePlayer; i++) {
            count++;
        }

        return count >= 4;
    }

    /**
     * Checks if there are four consecutive pieces of the same player vertically in the board.
     *
     * @param  board         a 2D array representing the game board
     * @param  column        the column index of the current piece
     * @param  row           the row index of the current piece
     * @param  piecePlayer   the player identifier of the current piece
     *
     * @return               true if there are four consecutive pieces vertically, false otherwise
     */
    private boolean checkVertical(int[][] board, int column, int row, int piecePlayer) {
        int count = 1;
        for (int i = row - 1; i >= 0 && board[i][column] == piecePlayer; i--) {
            count++;
        }

        for (int i = row; i < Connect4Game.getNoOfRows() && board[i][column] == piecePlayer; i++) {
            count++;
        }
        return count >= 4;
    }

    /**
     * Checks if the game board is in a draw state.
     *
     * @param  board  a 2D array representing the game board
     *
     * @return        true if the game board is in a draw state, false otherwise
     */
    private boolean checkDraw(int[][] board) {
        for (int column = 0; column < Connect4Game.getNoOfColumns(); column++) {
            if (!isColumnFull(board, column)) {
                return false;
            }
        }
        return true;
    }

}
