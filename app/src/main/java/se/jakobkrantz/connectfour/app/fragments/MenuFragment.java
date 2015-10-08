package se.jakobkrantz.connectfour.app.fragments;
/*
 * Created by jakkra on 2015-09-28.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import se.jakobkrantz.connectfour.app.FragmentEventListener;
import se.jakobkrantz.connectfour.app.R;
import se.jakobkrantz.connectfour.app.Commons.GameState;


public class MenuFragment extends Fragment implements NewGameStartDialog.OnGameStartListener, View.OnClickListener {
    FragmentEventListener eventListener;
    private Button resumeButton, newGameButton, highscoreButton;

    public MenuFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_home, container, false);
        resumeButton = (Button) rootView.findViewById(R.id.resume_button);
        newGameButton = (Button) rootView.findViewById(R.id.new_game_button);
        highscoreButton = (Button) rootView.findViewById(R.id.highscore_button);
        newGameButton.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onGameStart(String p1, String p2) {
        Log.d(getClass().getSimpleName(), "onGameStart called");
        Bundle args = new Bundle();
        args.putString("p1", p1);
        args.putString("p2", p2);
        eventListener.onEvent(GameState.IN_GAME, args);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.new_game_button) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            NewGameStartDialog dialog = new NewGameStartDialog();
            dialog.setTargetFragment(this, 1);
            dialog.show(fm, "GameStart");
        } else if(v.getId() == R.id.resume_button){


        } else if(v.getId() == R.id.highscore_button){


        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Make sure that the container activity has implemented
        // the listener interface. If not, it throws an exception.
        try {
            eventListener = (FragmentEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnEventListener");
        }
    }
}
