/*
 * Created by jakkra on 2015-10-11.
 */

import static org.junit.Assert.*;

import android.util.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.jakobkrantz.connectfour.app.game.CompleteGameState;
import se.jakobkrantz.connectfour.app.game.ConnectFourGame;
import se.jakobkrantz.connectfour.app.game.Player;


public class ConnectFourTest {
    public static byte EMPTY = ConnectFourGame.EMPTY; // is 0
    public static byte BLUE = ConnectFourGame.BLUE; // is 1
    public static byte RED = ConnectFourGame.RED; // is 2

    /**
     * Sets up the test fixture.
     */
    @Before
    public void setUp() {

    }


    @Test
    public void testWinningDiagonal() {
        byte[][] board = new byte[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 2, 0, 0},
                {0, 0, 1, 2, 2, 0, 0},
                {0, 1, 2, 1, 1, 0, 0},
                {2, 2, 1, 1, 2, 0, 0}
        };
        Player pBlue = new Player("pBlue", 0);
        Player pRed = new Player("pRed", 0);
        ConnectFourGame game = new ConnectFourGame(new CompleteGameState(board, pBlue, pRed, 0)); // turn = 0 => Blue
        game.dropTile(4);
        assertEquals(true, game.isGameWon());
        assertEquals(pBlue, game.getWinner());
    }

    @Test
    public void testWinningHorizontal() {
        byte[][] board = new byte[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 1, 1, 0, 1, 0, 0}
        };
        Player pBlue = new Player("pBlue", 0);
        Player pRed = new Player("pRed", 0);
        ConnectFourGame game = new ConnectFourGame(new CompleteGameState(board, pBlue, pRed, 0)); // turn = 0 => Blue
        game.dropTile(3);
        assertEquals(true, game.isGameWon());
        assertEquals(pBlue, game.getWinner());
    }

    @Test
    public void testWinningVertical() {
        byte[][] board = new byte[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0}
        };
        Player pBlue = new Player("pBlue", 0);
        Player pRed = new Player("pRed", 0);
        ConnectFourGame game = new ConnectFourGame(new CompleteGameState(board, pBlue, pRed, 0)); // turn = 0 => Blue
        game.dropTile(1);
        assertEquals(true, game.isGameWon());
        assertEquals(pBlue, game.getWinner());
    }

    @Test
    public void testWinningOtherDiagonal() {
        byte[][] board = new byte[][]{
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {2, 1, 0, 2, 1, 0, 0},
                {2, 2, 1, 2, 0, 1, 0},
                {1, 1, 2, 1, 0, 0, 1}
        };
        Player pBlue = new Player("pBlue", 0);
        Player pRed = new Player("pRed", 0);
        ConnectFourGame game = new ConnectFourGame(new CompleteGameState(board, pBlue, pRed, 0)); // turn = 0 => Blue
        game.dropTile(3);
        assertEquals(true, game.isGameWon());
        assertEquals(pBlue, game.getWinner());
    }

    @Test
    public void testWinningByDropping() {
        Player pBlue = new Player("pBlue", 0);
        Player pRed = new Player("pRed", 0);
        ConnectFourGame game = new ConnectFourGame(7, 6, pBlue, pRed); // turn = 0 => Blue
        game.dropTile(0);
        game.dropTile(1);
        game.dropTile(0);
        game.dropTile(1);
        game.dropTile(0);
        game.dropTile(1);
        game.dropTile(0);
        assertEquals(true, game.isGameWon());
        assertEquals(pBlue, game.getWinner());
    }


    /**
     * Tears down the test fixture.
     * (Called after every test case method.)
     */
    @After
    public void tearDown() {

    }


}
