package se.jakobkrantz.connectfour.app.game;

/*
 * Created by jakkra on 2015-09-28.
 */

import android.graphics.Point;
import android.util.Log;

public class ConnectFourGame {
    private byte[][] board;
    private int colCount;
    private int rowCount;
    private Player[] players;
    private int turnIsPlayer;
    private int winningPlayerIs;
    private static byte EMPTY = 0;
    private static byte BLUE = 1;
    private static byte RED = 2;
    private boolean gameWon;
    private Point lastDrop;


    public ConnectFourGame(int rows, int cols, Player playerBlue, Player playerRed) {
        this.colCount = cols;
        this.rowCount = rows;
        players = new Player[2];
        players[0] = playerBlue;
        players[1] = playerRed;
        board = new byte[rows][cols];
        gameWon = false;
        turnIsPlayer = 0; //player 0 starts
    }

    private void printBoard() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                sb.append(board[i][j] + "  ");
            }
            sb.append("\n");
        }
        Log.d(" ", "LOOL          \n" + sb.toString());

    }

    public boolean dropTile(int posCol) {
        Log.d(getClass().toString(), "dropTile at col: " + posCol);
        boolean isMoveLegit = false;
        if (gameWon) {
            Log.d(getClass().toString(), "GAME WON; WEEE!");
            return isMoveLegit;
        }
        int row;
        for (row = rowCount - 1; row >= 0; row--) {
            if (board[row][posCol] == EMPTY) {
                board[row][posCol] = getCurrentColor();
                isMoveLegit = true;
                lastDrop = new Point(row, posCol);
                Log.d(getClass().toString(), "Legit move, dropped at row: " + row + " Should land at col: " + posCol);
                if (!checkGameWon(row, posCol)) {
                    nextPlayer();
                } else {
                    Log.d(getClass().toString(), "Game won by player: " + getWinner().getName());
                }
                printBoard();
                return isMoveLegit;
            }
        }
        Log.d(getClass().toString(), "Not legit move, did not drop at row: " + row);
        return isMoveLegit;

    }


    public boolean isGameWon() {
        return gameWon;
    }

    public Player getWinner() {
        return players[winningPlayerIs];
    }

    public Player turnIs() {
        return players[turnIsPlayer];
    }

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

    private byte getCurrentColor() {
        return turnIsPlayer == 0 ? BLUE : RED;

    }

    private boolean checkGameWon(int row, int col) {
        Log.d(getClass().toString(), "LOL STUCK");
        int nbrInRow = 1;
        int currentRow;
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
        Log.d(getClass().toString(), "LOL STUCK");
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
        Log.d(getClass().toString(), "LOL STUCK");
        //check diagonal top left - down right
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
        Log.d(getClass().toString(), "LOL STUCK");
        //down left - up right
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
        Log.d(getClass().toString(), "No WIN");
        return false;
    }

    private void winningUpdate() {
        gameWon = true;
        winningPlayerIs = turnIsPlayer;
        players[winningPlayerIs].increaseScore();
    }

    private void nextPlayer() {
        turnIsPlayer++;
        turnIsPlayer %= 2;
    }

    public Point getLastDropCoord() {
        return lastDrop;
    }


}
