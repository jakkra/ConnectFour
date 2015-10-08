package se.jakobkrantz.connectfour.app.fragments;
/*
 * Created by jakkra on 2015-09-28.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import de.hdodenhof.circleimageview.CircleImageView;
import se.jakobkrantz.connectfour.app.Commons;
import se.jakobkrantz.connectfour.app.FragmentEventListener;
import se.jakobkrantz.connectfour.app.R;
import se.jakobkrantz.connectfour.app.game.*;


public class BoardFragment extends Fragment implements View.OnClickListener, WinnerPresenterDialog.OnGameStateChangeListener {
    private ConnectFourGame connectFourGame;
    private Player p1, p2;
    private CircleImageView[][] boardCells;
    private static int COLUMNS = 7;
    private static int ROWS = 6;
    private Context context;
    private FragmentEventListener eventListener;
    private TextView whosTurn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_in_game, container, false);
        whosTurn = (TextView) rootView.findViewById(R.id.whos_turn_tv);
        LinearLayout[] rowLayouts = new LinearLayout[6];
        rowLayouts[0] = (LinearLayout) rootView.findViewById(R.id.row_0);
        rowLayouts[1] = (LinearLayout) rootView.findViewById(R.id.row_1);
        rowLayouts[2] = (LinearLayout) rootView.findViewById(R.id.row_2);
        rowLayouts[3] = (LinearLayout) rootView.findViewById(R.id.row_3);
        rowLayouts[4] = (LinearLayout) rootView.findViewById(R.id.row_4);
        rowLayouts[5] = (LinearLayout) rootView.findViewById(R.id.row_5);

        CircleImageView imageView;
        boardCells = new CircleImageView[ROWS][COLUMNS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                imageView = new CircleImageView(context);
                imageView.setBorderColor(Color.DKGRAY);
                imageView.setBorderWidth(5);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                imageView.setImageResource(R.drawable.ic_launcher);
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
        Bundle args = getArguments();
        Log.d(getClass().toString(), "Player one is: " + args.get("p1"));
        Log.d(getClass().toString(), "Player two is: " + args.get("p2"));

        String playerOneName = (String) args.get("p1");
        String playerTwoName = (String) args.get("p2");
        p1 = new Player(playerOneName, 0);
        p2 = new Player(playerTwoName, 0);
        connectFourGame = new ConnectFourGame(ROWS, COLUMNS, p1, p2);
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
        if (v instanceof CircleImageView) { // This is a little h4x I know
            Point coord = (Point) v.getTag(R.integer.image_view_coordinate_tag);
            int previousColor = getCurrentColor();
            if (connectFourGame.dropTile(coord.y)) {
                updateUiTurn();
                Point lastDrop = connectFourGame.getLastDropCoord();
                boardCells[lastDrop.x][lastDrop.y].setBorderColor(previousColor);

                if (connectFourGame.isGameWon()) {
                    Toast.makeText(context, "Game won by player: " + connectFourGame.getWinner().getName(), Toast.LENGTH_SHORT).show();

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    WinnerPresenterDialog dialog = new WinnerPresenterDialog();
                    Bundle args = new Bundle();
                    args.putString("winner", connectFourGame.getWinner().getName());
                    dialog.setArguments(args);
                    dialog.setTargetFragment(this, 1);
                    dialog.show(fm, "WinnerPresenter");
                }
            }
        }
    }

    @Override
    public void onGameStateChange(Commons.GameState state) {
        if(state == Commons.GameState.RESTART){
            connectFourGame.resetGame();
            clearBoard();
            updateUiTurn();
        } else if(state == Commons.GameState.HOME){
            eventListener.onEvent(Commons.GameState.HOME, null);
        }
    }

    private void updateUiTurn() {
        if (connectFourGame.turnIs().equals(p1)) {
            whosTurn.setTextColor(Color.BLUE);
        } else {
            whosTurn.setTextColor(Color.RED);
        }
    }


    private int getCurrentColor() {
        if (connectFourGame.turnIs().equals(p1)) {
            return Color.BLUE;
        } else {
            return Color.RED;
        }
    }

    private void clearBoard(){
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                boardCells[row][col].setBorderColor(Color.DKGRAY);
            }

        }
    }
}