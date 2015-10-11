package se.jakobkrantz.connectfour.app.fragments;
/*
 * Created by jakkra on 2015-10-08.
 */

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import se.jakobkrantz.connectfour.app.R;


/**
 * Displays an dialog that the user may fill with details. Will make a Callback to the parent
 * with the details put in.
 */
public class GameStartDialog extends DialogFragment implements View.OnClickListener {

    private TextView tvPlayer1;
    private TextView tvPlayer2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_new_game_start, container);
        tvPlayer1 = (TextView) view.findViewById(R.id.player_one_name);
        tvPlayer2 = (TextView) view.findViewById(R.id.player_two_name);
        Button startButton = (Button) view.findViewById(R.id.start_game_button);
        startButton.setOnClickListener(this);
        getDialog().setTitle(R.string.select_names);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = (TextView) this.getDialog().findViewById(android.R.id.title);
        if (textView != null) {
            textView.setGravity(Gravity.CENTER);
        }
    }


    @Override
    public void onClick(View v) {
        doCallback(v);
        dismiss();
    }

    /**
     * Tries to do a callback to the GameStartListener if there is any, otherwise nothing is done
     *
     * @param v
     */
    private void doCallback(View v) {
        GameStartListener callback = null;
        try {
            callback = (GameStartListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(this.getClass().getSimpleName(), "OnGameStartListener of this class must be implemented by target fragment!", e);
        }
        if (callback != null) {
            if (v.getId() == R.id.start_game_button) {
                callback.onGameStart(tvPlayer1.getText().toString(), tvPlayer2.getText().toString());
            }

        }
    }


    /**
     * Callback with data from a GameStartDialog.
     */
    public interface GameStartListener {
        /**
         * @param p1 name of player one
         * @param p2 name of player two
         */
        public void onGameStart(String p1, String p2);
    }
}

