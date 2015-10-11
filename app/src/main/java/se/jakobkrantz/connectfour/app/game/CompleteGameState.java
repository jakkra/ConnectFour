package se.jakobkrantz.connectfour.app.game;
/*
 * Created by jakkra on 2015-10-08.
 */

import java.io.Serializable;

/**
 * Class containing all data to store the full state of ConnectFour.
 */
public class CompleteGameState implements Serializable{

    public final byte[][] board;
    public final Player player1;
    public final Player player2;
    public int turnIs;

    public CompleteGameState(byte[][] board, Player player1, Player player2, int turnIs){
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.turnIs = turnIs;
    }
}
