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


public class BoardFragment extends Fragment implements View.OnClickListener, WinnerPresenterDialog.OnGameStateChangeListener {
    private ConnectFourGame connectFourGame;
    private Player p1, p2;
    private ImageView[][] boardCells;
    private static int COLUMNS = 7;
    private static int ROWS = 6;
    private Context context;
    private FragmentEventListener eventListener;
    private TextView whosTurn;
    private AuditLog auditLog;
    private RelativeLayout colorBg;
    private Button restartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_in_game, container, false);
        whosTurn = (TextView) rootView.findViewById(R.id.whos_turn_tv);
        LinearLayout[] rowLayouts = new LinearLayout[6];
        colorBg = (RelativeLayout) rootView.findViewById(R.id.relative_bg);
        restartButton = (Button) rootView.findViewById(R.id.restart_in_game_btn);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGameStateChange(Commons.GameState.RESTART);
            }
        });
        rowLayouts[0] = (LinearLayout) rootView.findViewById(R.id.row_0);
        rowLayouts[1] = (LinearLayout) rootView.findViewById(R.id.row_1);
        rowLayouts[2] = (LinearLayout) rootView.findViewById(R.id.row_2);
        rowLayouts[3] = (LinearLayout) rootView.findViewById(R.id.row_3);
        rowLayouts[4] = (LinearLayout) rootView.findViewById(R.id.row_4);
        rowLayouts[5] = (LinearLayout) rootView.findViewById(R.id.row_5);

        ImageView imageView;
        boardCells = new ImageView[ROWS][COLUMNS];

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
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        auditLog = new AuditLog(context, "GameLog1");
        Bundle args = getArguments();
        CompleteGameState gs = null;
        if (!args.containsKey("p1")) {
            FileInputStream fis = null;
            try {
                fis = context.openFileInput("gameSaved");
                ObjectInputStream is = new ObjectInputStream(fis);
                gs = (CompleteGameState) is.readObject();
                is.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (gs != null) {
            p1 = new Player(gs.player1, 0);
            p2 = new Player(gs.player2, 0);
            connectFourGame = new ConnectFourGame(gs);
            repaintBoard(gs);

        }
        if (connectFourGame == null) {
            String playerOneName = (String) args.get("p1");
            String playerTwoName = (String) args.get("p2");
            p1 = new Player(playerOneName, 0);
            p2 = new Player(playerTwoName, 0);
            HighScores.addPlayer(context, playerOneName);
            HighScores.addPlayer(context, playerTwoName);
            connectFourGame = new ConnectFourGame(ROWS, COLUMNS, p1, p2);
            auditLog.writeLog("New game started with player BLUE: " + p1.getName() + " and Red: " + p2.getName());
            auditLog.writeLog(" Turn is now: " + connectFourGame.turnIs().getName());
        }
        updateUiTurn();
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
            Point coord = (Point) v.getTag(R.integer.image_view_coordinate_tag);
            String currentPlayerName = connectFourGame.turnIs().getName();
            auditLog.writeLog(currentPlayerName + " dropped tile at column: " + coord.y);
            int resIdTurn = getDrawableCircle();
            if (connectFourGame.dropTile(coord.y)) {
                updateUiTurn();
                Point lastDrop = connectFourGame.getLastDropCoord();
                auditLog.writeLog(currentPlayerName + " drop was legit, landed at row: " + lastDrop.x + " column: " + lastDrop.y);

                boardCells[lastDrop.x][lastDrop.y].setImageResource(resIdTurn);

                if (connectFourGame.isGameWon()) {
                    auditLog.writeLog(currentPlayerName + " won the game");

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    WinnerPresenterDialog dialog = new WinnerPresenterDialog();
                    Bundle args = new Bundle();
                    args.putString("winner", connectFourGame.getWinner().getName());
                    HighScores.increaseScore(context, connectFourGame.getWinner().getName());
                    dialog.setArguments(args);
                    dialog.setTargetFragment(this, 1);
                    dialog.show(fm, "WinnerPresenter");
                    Log.d(getClass().getSimpleName(), auditLog.readLog());
                    File file = context.getFileStreamPath("gameSaved");
                    if (file.exists()) {
                        if (file.delete()) {
                            System.out.println("file Deleted :");
                        } else {
                            System.out.println("file not Deleted :");
                        }
                    }
                    connectFourGame.resetGame();
                } else {
                    auditLog.writeLog(" Turn is now: " + connectFourGame.turnIs().getName());
                }
            } else {
                auditLog.writeLog(currentPlayerName + " drop was not ok, column: " + coord.y + " is full \n" +
                        " + " + currentPlayerName + " gets a new chance to drop at different column");
            }
            if (connectFourGame.isGameEven()) {
                Player[] players = connectFourGame.getAllPlayers();
                if (players.length > 1) {
                    auditLog.writeLog("Game is a draw between: " + players[0] + " and " + players[1]);
                    showEvenGameAlertDialog();
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("gameSaved", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(connectFourGame.getCompleateGameState());
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void repaintBoard(CompleteGameState gs){
        byte[][] b = gs.board;
        for (int row = 0; row < b.length; row++) {
            for (int col = 0; col < b[0].length; col++) {
                if(b[row][col] == ConnectFourGame.BLUE) {
                    boardCells[row][col].setImageResource(R.drawable.blue_circle1);
                } else if(b[row][col] == ConnectFourGame.RED) {
                    boardCells[row][col].setImageResource(R.drawable.red_circle1);
                } else {
                    boardCells[row][col].setImageResource(R.drawable.white_circle1);
                }
            }
        }

    }

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

    @Override
    public void onGameStateChange(Commons.GameState state) {
        if (state == Commons.GameState.RESTART) {
            connectFourGame.resetGame();
            clearBoard();
            updateUiTurn();
        } else if (state == Commons.GameState.HOME) {
            eventListener.onEvent(Commons.GameState.HOME, null);
        }
    }

    private void updateUiTurn() {
        if (connectFourGame.turnIs().equals(p1)) {
            colorBg.setBackgroundColor(getResources().getColor(R.color.blue));
            whosTurn.setText(connectFourGame.turnIs().getName() + "'s turn");
        } else {
            colorBg.setBackgroundColor(getResources().getColor(R.color.red));
            whosTurn.setText(connectFourGame.turnIs().getName() + "'s turn");

        }
    }


    private int getDrawableCircle() {
        if (connectFourGame.turnIs().equals(p1)) {
            return R.drawable.blue_circle1;
        } else {
            return R.drawable.red_circle1;
        }
    }

    private void clearBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                boardCells[row][col].setImageResource(R.drawable.white_circle1);
            }

        }
    }
}