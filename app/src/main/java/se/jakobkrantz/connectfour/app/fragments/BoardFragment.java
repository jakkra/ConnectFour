package se.jakobkrantz.connectfour.app.fragments;

/*
 * Created by jakkra on 2015-09-28.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import se.jakobkrantz.connectfour.app.*;
import se.jakobkrantz.connectfour.app.game.*;

import java.io.*;


/**
 * Fragment that contains the game view and updates the game view to match the ConnectFour game.
 */
public class BoardFragment extends Fragment implements View.OnClickListener, WinnerPresenterDialog.OnGameStateChangeListener {
    private static int COLUMNS = 7;
    private static int ROWS = 6;
    private FragmentEventListener eventListener;
    private ConnectFourGame connectFourGame;
    private Player p1, p2;
    private ImageView[][] boardCells;
    private Context context;
    private TextView whosTurn;
    private TextView playerOneScore;
    private TextView playerTwoScore;
    private AuditLog auditLog;
    private RelativeLayout colorBg;
    private Button restartButton;
    private boolean boardWasResetAfterWin = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_in_game, container, false);
        playerOneScore = (TextView) rootView.findViewById(R.id.score_p1);
        playerTwoScore = (TextView) rootView.findViewById(R.id.score_p2);
        whosTurn = (TextView) rootView.findViewById(R.id.whos_turn_tv);
        colorBg = (RelativeLayout) rootView.findViewById(R.id.relative_bg);
        restartButton = (Button) rootView.findViewById(R.id.restart_in_game_btn);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGameStateChange(Commons.GameState.RESTART);
            }
        });
        fillAndPrepareUI(rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        auditLog = new AuditLog(context, Commons.AUDIT_LOG_FILE_NAME);
        startGame();
        updateUiTurn();
    }

    /**
     * Prepares and starts a new game instance. If the fragments bundle
     * doesn't contain player names, it reloads the saved game from last session.
     */
    private void startGame() {
        Bundle args = getArguments();
        CompleteGameState gs = null;
        if (!args.containsKey(Commons.PLAYER_ONE_NAME_TAG)) {
            FileInputStream fis;
            try {
                fis = context.openFileInput(Commons.SAVED_GAME_NAME);
                ObjectInputStream is = new ObjectInputStream(fis);
                gs = (CompleteGameState) is.readObject();
                is.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (gs != null) {
            p1 = gs.player1;
            p2 = gs.player2;
            connectFourGame = new ConnectFourGame(gs);
            repaintBoard(gs);

        }
        if (connectFourGame == null) {
            String playerOneName = (String) args.get(Commons.PLAYER_ONE_NAME_TAG);
            String playerTwoName = (String) args.get(Commons.PLAYER_TWO_NAME_TAG);
            p1 = new Player(playerOneName, 0);
            p2 = new Player(playerTwoName, 0);
            HighScores.addPlayer(context, playerOneName);
            HighScores.addPlayer(context, playerTwoName);
            connectFourGame = new ConnectFourGame(ROWS, COLUMNS, p1, p2);
            auditLog.writeLog("\nNew game started with player BLUE: " + p1.getName() + " and Red: " + p2.getName());
            auditLog.writeLog(" Turn is now: " + connectFourGame.turnIs().getName());
        }
    }

    /**
     * Fills the UI with rows of LinearLayouts. Fills those rows with ImageViews
     * and sets their background resource.
     *
     * @param rootView
     */
    private void fillAndPrepareUI(View rootView) {
        boardCells = new ImageView[ROWS][COLUMNS];
        LinearLayout[] rowLayouts = new LinearLayout[6];
        rowLayouts[0] = (LinearLayout) rootView.findViewById(R.id.row_0);
        rowLayouts[1] = (LinearLayout) rootView.findViewById(R.id.row_1);
        rowLayouts[2] = (LinearLayout) rootView.findViewById(R.id.row_2);
        rowLayouts[3] = (LinearLayout) rootView.findViewById(R.id.row_3);
        rowLayouts[4] = (LinearLayout) rootView.findViewById(R.id.row_4);
        rowLayouts[5] = (LinearLayout) rootView.findViewById(R.id.row_5);

        ImageView imageView;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                imageView.setImageResource(R.drawable.white_circle1);
                imageView.setTag(R.integer.image_view_coordinate_tag, new Point(row, col));
                imageView.setOnClickListener(this);
                rowLayouts[row].addView(imageView);
                boardCells[row][col] = imageView;
            }

        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the listener interface. If not, it throws an exception.
        try {
            eventListener = (FragmentEventListener) activity;
            context = getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnEventListener");

        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView) { // This is a little h4x I know
            Point coord = (Point) v.getTag(R.integer.image_view_coordinate_tag); //get the position of this ImageView
            handleClickAtPosition(coord);
        }
    }

    /**
     * Handles everything that should happend when a player drops a tile at a certain position.
     *
     * @param coord the coordinate that was clicked in the grid.
     */
    private void handleClickAtPosition(Point coord) {
        String lastPlayerName = connectFourGame.turnIs().getName();
        auditLog.writeLog(lastPlayerName + " dropped tile at column: " + coord.y);
        int resIdTurn = getDrawableCircle();
        if (connectFourGame.dropTile(coord.y) && boardWasResetAfterWin) {
            updateUiTurn();
            Point lastDrop = connectFourGame.getLastDropCoord();
            boardCells[lastDrop.x][lastDrop.y].setImageResource(resIdTurn);

            auditLog.writeLog(lastPlayerName + " drop was legit, landed at row: " + lastDrop.x + " column: " + lastDrop.y);
            if (connectFourGame.isGameWon()) {
                handleWonGame(lastPlayerName);
            } else {
                auditLog.writeLog(" Turn is now: " + connectFourGame.turnIs().getName());
            }
        } else {
            auditLog.writeLog(lastPlayerName + " drop was not ok, column: " + coord.y + " is full \n" +
                    " + " + lastPlayerName + " gets a new chance to drop at different column");
        }
        if (connectFourGame.isGameEven()) {
            Player[] players = connectFourGame.getAllPlayers();
            if (players.length > 1) {
                auditLog.writeLog("Game is a draw between: " + players[0] + " and " + players[1]);
                showEvenGameAlertDialog();
            }
        }
    }

    /**
     * Handles what should happen if the game was won.
     * @param winnerName name of the player that put the winning tile
     */
    private void handleWonGame(String winnerName) {
        auditLog.writeLog(winnerName + " won the game");
        boardWasResetAfterWin = false;
        showWinningDialog();
        HighScores.increaseScoreOne(context, connectFourGame.getWinner().getName());
        deleteSavedGame();
        connectFourGame.resetGame();
    }

    /**
     * Shows a DialogFragment which presents the winning player and
     * gives options to restart or go back to home menu.
     */
    private void showWinningDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        WinnerPresenterDialog dialog = new WinnerPresenterDialog();
        Bundle args = new Bundle();
        args.putString(Commons.WINNER_TAG, connectFourGame.getWinner().getName());
        dialog.setArguments(args);
        dialog.setTargetFragment(this, 1);
        dialog.show(fm, "WinnerPresenter");
    }

    /**
     * Deletes the saved game since the game is won, no need to save that.
     */
    private void deleteSavedGame() {
        File file = context.getFileStreamPath(Commons.SAVED_GAME_NAME);
        if (file.exists()) {
            if (file.delete()) {
                Log.d(getClass().getSimpleName(), "file Deleted :");
            } else {
                Log.d(getClass().getSimpleName(), "file not Deleted :");
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveGameState();
    }

    /**
     * Saves the current game state
     */
    private void saveGameState() {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(Commons.SAVED_GAME_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(connectFourGame.getCompleteGameState());
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Repaints the board from a saved game instance, CompleteGameState object.
     *
     * @param gs the game state used to repaint the board to match the game state
     */
    private void repaintBoard(CompleteGameState gs) {
        byte[][] b = gs.board;
        for (int row = 0; row < b.length; row++) {
            for (int col = 0; col < b[0].length; col++) {
                if (b[row][col] == ConnectFourGame.BLUE) {
                    boardCells[row][col].setImageResource(R.drawable.blue_circle1);
                } else if (b[row][col] == ConnectFourGame.RED) {
                    boardCells[row][col].setImageResource(R.drawable.red_circle1);
                } else {
                    boardCells[row][col].setImageResource(R.drawable.white_circle1);
                }
            }
        }

    }

    /**
     * Shows an AlertDialog that tells the user that the game was a draw.
     * Gives the user option to restart or go back to home menu.
     */
    private void showEvenGameAlertDialog() {
        new AlertDialog.Builder(context)
                .setTitle("This game is a draw")
                .setMessage("Restart game or go back to home?")
                .setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onGameStateChange(Commons.GameState.RESTART);
                    }
                })
                .setNegativeButton(R.string.go_back_home, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onGameStateChange(Commons.GameState.HOME);
                    }
                })
                .setIcon(0)
                .show();
    }

    /**
     * Callback from child components.
     *
     * @param state the GameState requested from the component
     */
    @Override
    public void onGameStateChange(Commons.GameState state) {
        boardWasResetAfterWin = true;
        if (state == Commons.GameState.RESTART) {
            connectFourGame.resetGame();
            clearBoard();
            updateUiTurn();
        } else if (state == Commons.GameState.HOME) {
            eventListener.onEvent(Commons.GameState.HOME, null);
        }
    }

    /**
     * Updates the UI (except the game board) to match the current game state.
     */
    private void updateUiTurn() {
        if (connectFourGame.turnIs().equals(p1)) {
            colorBg.setBackgroundColor(getResources().getColor(R.color.blue));
            whosTurn.setText(connectFourGame.turnIs().getName() + "'s turn");
        } else {
            colorBg.setBackgroundColor(getResources().getColor(R.color.red));
            whosTurn.setText(connectFourGame.turnIs().getName() + "'s turn");
        }
        Player[] players = connectFourGame.getAllPlayers();
        playerOneScore.setText(players[0].getScore() + "");
        playerTwoScore.setText(players[1].getScore() + "");

    }


    /**
     * Calculates the drawable resource corresponding to the current player.
     *
     * @return the drawable resource
     */
    private int getDrawableCircle() {
        if (connectFourGame.turnIs().equals(p1)) {
            return R.drawable.blue_circle1;
        } else {
            return R.drawable.red_circle1;
        }
    }

    /**
     * Clears the UI board.
     */
    private void clearBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                boardCells[row][col].setImageResource(R.drawable.white_circle1);
            }
        }
    }
}