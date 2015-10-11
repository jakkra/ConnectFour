package se.jakobkrantz.connectfour.app.game;
/*
 * Created by jakkra on 2015-09-28.
 */

import android.graphics.Point;
import android.util.Log;

/**
 * All logic needed for the game Connect Four.
 */
public class ConnectFourGame {
    private byte[][] board;
    private int colCount;
    private int rowCount;
    private Player[] players;
    private int turnIsPlayer;
    private int winningPlayerIs;
    public static byte EMPTY = 0;
    public static byte BLUE = 1;
    public static byte RED = 2;
    private boolean gameWon;
    private Point lastDrop;


    /**
     * Creates a new game instance of ConnectFour
     *
     * @param rows       number of rows on the board
     * @param cols       number of column on the board
     * @param playerBlue first player
     * @param playerRed  second player
     */
    public ConnectFourGame(int rows, int cols, Player playerBlue, Player playerRed) {
        this.colCount = cols;
        this.rowCount = rows;
        players = new Player[2];
        players[0] = playerBlue;
        players[1] = playerRed;
        board = new byte[rows][cols];
        gameWon = false;
        turnIsPlayer = 0; //player 0 starts
        winningPlayerIs = -1;
    }

    /**
     * Creates a new game instance loaded from a saved game.
     *
     * @param gs the saved game to load from
     */
    public ConnectFourGame(CompleteGameState gs) {
        Log.d(getClass().getSimpleName(), "Using Saved state constructor");
        board = gs.board;
        colCount = board[0].length;
        rowCount = board.length;
        players = new Player[2];
        players[0] = gs.player1;
        players[1] = gs.player2;
        turnIsPlayer = gs.turnIs;
        gameWon = false;
        winningPlayerIs = -1;
    }


    /**
     * Drops a tile on the board at specified column and calculates where it should land.
     *
     * @param posCol column to drop tile at
     * @return true if the column wasn't full otherwise false
     */
    public boolean dropTile(int posCol) {
        if (gameWon) {
            return false;
        }
        int row;
        for (row = rowCount - 1; row >= 0; row--) {
            if (board[row][posCol] == EMPTY) {
                board[row][posCol] = getCurrentColor();
                lastDrop = new Point(row, posCol);
                if (!checkGameWon(row, posCol)) {
                    nextPlayer();
                }
                return true;
            }
        }
        return false;
    }


    /**
     * Returns either if the game is won or not.
     *
     * @return true if the game is won, false otherwise
     */
    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * returns the winner of the game
     *
     * @return player that won
     */
    public Player getWinner() {
        return players[winningPlayerIs];
    }

    /**
     * Returns which player's turn it is.
     *
     * @return the Player who it is.
     */
    public Player turnIs() {
        return players[turnIsPlayer];
    }

    /**
     * Returns a array of all players in the game
     *
     * @return array of Players
     */
    public Player[] getAllPlayers() {
        return players;
    }

    /**
     * Resets the game, the initial Players are not cleared.
     */
    public void resetGame() {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                board[row][col] = EMPTY;
            }
        }
        winningPlayerIs = -1;
        gameWon = false;
        turnIsPlayer = 0;
    }

    /**
     * Checks if the game ended even.
     *
     * @return if the game was even
     */
    public boolean isGameEven() {
        for (int col = 0; col < colCount; col++) {
            if (board[0][col] == EMPTY) {
                return false;
            }
        }
        Log.e("game is", "EVEN");
        return true;
    }

    /**
     * Returns everything needed to recreate the current game later.
     *
     * @return the data needed for recreation
     */
    public CompleteGameState getCompleteGameState() {
        return new CompleteGameState(board, players[0], players[1], turnIsPlayer);
    }

    /**
     * @return which color the current turn is
     */
    private byte getCurrentColor() {
        return turnIsPlayer == 0 ? BLUE : RED;
    }

    /**
     * Checks if the board contains a winning sequence of tiles including the coordinate (column, row)
     *
     * @param row row coordinate
     * @param col column coordinate
     * @return
     */
    private boolean checkGameWon(int row, int col) {
        if (checkWinHorizontal(row, col)) return true;
        if (checkWinVertical(row, col)) return true;
        if (checkWinDiagonalTopLeft(row, col)) return true;
        if (checkWinDiagonalTopRight(row, col)) return true;
        return false;
    }

    /**
     * Checks if the board contains a winning sequence
     * down left - up right from the given coordinate
     *
     * @param row starting row
     * @param col starting col
     * @return if a winning sequence was found
     */
    private boolean checkWinDiagonalTopRight(int row, int col) {
        int nbrInRow;
        int currentCol;
        int currentRow;
        nbrInRow = 1;
        currentCol = col - 1;
        currentRow = row + 1;
        while (currentCol >= 0 && currentRow < rowCount && board[currentRow][currentCol] == getCurrentColor()) {
            currentCol--;
            currentRow++;
            nbrInRow++;
        }

        currentCol = col + 1;
        currentRow = row - 1;
        while (currentCol < colCount && currentRow >= 0 && board[currentRow][currentCol] == getCurrentColor()) {
            currentCol++;
            currentRow--;
            nbrInRow++;
        }

        if (nbrInRow >= 4) {
            winningUpdate();
            return true;
        }
        return false;
    }

    /**
     * Checks if the board contains a winning sequence
     * top left - down right from the given coordinate
     *
     * @param row starting row
     * @param col starting col
     * @return if a winning sequence was found
     */
    private boolean checkWinDiagonalTopLeft(int row, int col) {
        int nbrInRow;
        int currentCol;
        int currentRow;
        nbrInRow = 1;
        currentCol = col + 1;
        currentRow = row + 1;
        while (currentCol < colCount && currentRow < rowCount && board[currentRow][currentCol] == getCurrentColor()) {
            currentCol++;
            currentRow++;
            nbrInRow++;
        }

        currentCol = col - 1;
        currentRow = row - 1;
        while (currentCol >= 0 && currentRow >= 0 && board[currentRow][currentCol] == getCurrentColor()) {
            currentCol--;
            currentRow--;
            nbrInRow++;
        }

        if (nbrInRow >= 4) {
            winningUpdate();
            return true;
        }
        return false;
    }

    /**
     * Checks if the board contains a winning sequence
     * vertically from the given coordinate
     *
     * @param row starting row
     * @param col starting col
     * @return if a winning sequence was found
     */
    private boolean checkWinVertical(int row, int col) {
        int nbrInRow;
        int currentRow;
        //check vertical
        nbrInRow = 1;
        currentRow = row - 1;
        while (currentRow >= 0 && board[currentRow][col] == getCurrentColor()) {
            currentRow--;
            nbrInRow++;
        }
        currentRow = row + 1;
        while (currentRow < rowCount && board[currentRow][col] == getCurrentColor()) {
            currentRow++;
            nbrInRow++;
        }

        if (nbrInRow >= 4) {
            winningUpdate();
            return true;
        }
        return false;
    }

    /**
     * Checks if the board contains a winning sequence
     * horizontally from the given coordinate
     *
     * @param row starting row
     * @param col starting col
     * @return if a winning sequence was found
     */
    private boolean checkWinHorizontal(int row, int col) {
        int nbrInRow = 1;
        int currentCol;
        //check horizontal
        currentCol = col - 1;
        while (currentCol >= 0 && board[row][currentCol] == getCurrentColor()) {
            currentCol--;
            nbrInRow++;
        }
        currentCol = col + 1;
        while (currentCol < colCount && board[row][currentCol] == getCurrentColor()) {
            currentCol++;
            nbrInRow++;
        }
        if (nbrInRow >= 4) {
            winningUpdate();
            return true;
        }
        return false;
    }

    /**
     * Updates the state of the game to won.
     */
    private void winningUpdate() {
        gameWon = true;
        winningPlayerIs = turnIsPlayer;
        players[winningPlayerIs].increaseScore();
    }

    /**
     * Sets the turn to next player.
     */
    private void nextPlayer() {
        turnIsPlayer++;
        turnIsPlayer %= 2;
    }

    /**
     * Returns the coordinate of the postion the last tile landed on.
     *
     * @return Point containing the coordinate
     */
    public Point getLastDropCoord() {
        return lastDrop;
    }


}
