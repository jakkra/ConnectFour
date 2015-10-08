package se.jakobkrantz.connectfour.app.game;/*
 * Created by jakkra on 2015-10-08.
 */

import java.io.Serializable;

public class CompleteGameState implements Serializable{

    public final byte[][] board;
    public final String player1;
    public final String player2;
    public int turnIs;

    public CompleteGameState(byte[][] board, String player1, String player2, int turnIs){
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.turnIs = turnIs;
    }
}
