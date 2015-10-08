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
import se.jakobkrantz.connectfour.app.Commons.GameState;
import se.jakobkrantz.connectfour.app.R;


public class WinnerPresenterDialog extends DialogFragment implements View.OnClickListener {
    private TextView winnerTextView;
    private String winner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        winner = getArguments().getString("winner");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_winner_presenter, container);
        winnerTextView = (TextView) view.findViewById(R.id.text_view_winner);
        Button restartButton = (Button) view.findViewById(R.id.restart_game_button);
        Button backToMainButton = (Button) view.findViewById(R.id.back_to_home_button);
        restartButton.setOnClickListener(this);
        backToMainButton.setOnClickListener(this);
        getDialog().setTitle(R.string.game_won);
        winnerTextView.setText(winner);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = (TextView) this.getDialog().findViewById(android.R.id.title);
        if(textView != null){
            textView.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void onClick(View v) {
        OnGameStateChangeListener callback;
        try {
            callback = (OnGameStateChangeListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(this.getClass().getSimpleName(), "OnGameStateChangeListener of this class must be implemented by target fragment!", e);
            throw e;
        }
        if (callback != null) {
            if (v.getId() == R.id.restart_game_button) {
                callback.onGameStateChange(GameState.RESTART);

            } else if(v.getId() == R.id.back_to_home_button){
                callback.onGameStateChange(GameState.HOME);


            }

        }
        getDialog().dismiss(); //TODO Not recommended to do this, should be changed I believe
    }


    public interface OnGameStateChangeListener {
        public void onGameStateChange(GameState state);
    }
}

