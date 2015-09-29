package se.jakobkrantz.connectfour.app.fragments;
/*
 * Created by jakkra on 2015-09-28.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import se.jakobkrantz.connectfour.app.FragmentEventListener;
import se.jakobkrantz.connectfour.app.R;
import se.jakobkrantz.connectfour.app.Commons.GameState;



public class MenuFragment extends Fragment {
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
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("p1", "Alex");
                args.putString("p1", "Elin");
                eventListener.onEvent(GameState.IN_GAME, args);
            }
        });
        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the listener interface. If not, it throws an exception.
        try {
            eventListener = (FragmentEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnEventListener");
        }
    }
}
