package se.jakobkrantz.connectfour.app.game;

/*
 * Created by jakkra on 2015-09-28.
 */

import android.util.Log;

public class ConnectFourGame {
    private byte[][] board;
    private int colCount;
    private int rowCount;
    private Player playerRed;
    private Player playerBlue;

    private static byte EMPTY = 0;
    private static byte BLUE = 1;
    private static byte RED = 2;
    private boolean gameWon;
    private Player turnIs;
    private Player winner;
    private XYCoord lastDrop;


    public ConnectFourGame(int cols, int rows, Player playerBlue, Player playerRed) {
        this.colCount = cols;
        this.rowCount = rows;
        this.playerRed = playerRed;
        this.playerBlue = playerBlue;
        board = new byte[cols][rows];
        gameWon = false;
        turnIs = playerBlue;
    }

    public boolean dropTile(int posCol) {
        Log.d(getClass().toString(), "dropTile at col: " + posCol);
        boolean isMoveLegit = false;
        if (gameWon) {
            return isMoveLegit;
        }
        int row;
        for (row = 0; row < rowCount; row++) {
            if (board[posCol][row] == EMPTY) {
                board[posCol][row] = getCurrentColor();
                isMoveLegit = true;
                checkGameWon(posCol, row);
                lastDrop = new XYCoord(posCol, row);
                Log.d(getClass().toString(), "Legit move, dropped at row: " + row);
                return isMoveLegit;

            }
        }
        return isMoveLegit;

    }


    public boolean isGameWon() {
        return gameWon;
    }

    public Player getWinner() {
        return winner;
    }

    public Player turnIs() {
        return turnIs;
    }

    public void resetGame() {
        for (int col = 0; col < colCount; col++) {
            for (int row = 0; row < rowCount; row++) {
                board[col][row] = EMPTY;
            }
        }
        winner = null;
        gameWon = false;
        turnIs = playerBlue;

    }

    private byte getCurrentColor() {
        return turnIs.equals(playerBlue) ? BLUE : RED;

    }

    private boolean checkGameWon(int col, int row) {
        Log.d(getClass().toString(), "LOL STUCK");
        int nbrInRow = 1;
        //check horizontal
        int currentCol = col - 1;
        while (currentCol >= 0 && board[currentCol][row] == getCurrentColor()) {
            currentCol--;
            nbrInRow++;
        }
        currentCol = col + 1;
        while (currentCol < colCount && board[currentCol][row] == getCurrentColor()) {
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
        int currentRow = row - 1;
        while (currentRow >= 0 && board[col][currentRow] == getCurrentColor()) {
            currentCol--;
            nbrInRow++;
        }
        currentRow = row + 1;
        while (currentRow < rowCount && board[currentCol][row] == getCurrentColor()) {
            currentCol++;
            nbrInRow++;
        }

        if (currentRow >= 4) {
            winningUpdate();
            return true;
        }
        Log.d(getClass().toString(), "LOL STUCK");
        //check diagonal top right - down left
        nbrInRow = 1;
        currentCol = col + 1;
        currentRow = row + 1;
        while (currentCol < colCount && currentRow < rowCount && board[currentCol][row] == getCurrentColor()) {
            currentCol++;
            currentRow++;
            nbrInRow++;
        }

        currentCol = col - 1;
        currentRow = row - 1;
        while (currentCol >= 0 && currentRow >= 0 && board[currentCol][row] == getCurrentColor()) {
            currentCol--;
            currentRow--;
            nbrInRow++;
        }

        if (nbrInRow >= 4) {
            winningUpdate();
            return true;
        }
        Log.d(getClass().toString(), "LOL STUCK");
        //check other diagonal
        nbrInRow = 1;
        currentCol = col - 1;
        currentRow = row + 1;
        while (currentCol >= 0 && currentRow < rowCount && board[currentCol][row] == getCurrentColor()) {
            currentCol--;
            currentRow++;
            nbrInRow++;
        }

        currentCol = col + 1;
        currentRow = row - 1;
        while (currentCol < colCount && currentRow <= 0 && board[currentCol][row] == getCurrentColor()) {
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
        winner = turnIs;
        winner.increaseScore();
    }

    public XYCoord getLastDropCoord() {
        return lastDrop;
    }


}
