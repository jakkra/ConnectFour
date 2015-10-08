package se.jakobkrantz.connectfour.app.fragments;
/*
 * Created by jakkra on 2015-10-08.
 */


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import se.jakobkrantz.connectfour.app.FragmentEventListener;
import se.jakobkrantz.connectfour.app.HighScores;
import se.jakobkrantz.connectfour.app.R;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class HighScoreFragment extends Fragment{
    private FragmentEventListener eventListener;
    private Button resumeButton, newGameButton, highscoreButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_high_score, container, false);
        resumeButton = (Button) rootView.findViewById(R.id.resume_button);
        newGameButton = (Button) rootView.findViewById(R.id.new_game_button);
        highscoreButton = (Button) rootView.findViewById(R.id.highscore_button);

        RecyclerView highScoreList = (RecyclerView) rootView.findViewById(R.id.hs_list);
        HighScoreListAdapter hsAdapter = new HighScoreListAdapter(HighScores.getFullHighScores(getActivity()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        highScoreList.setLayoutManager(mLayoutManager);
        highScoreList.setHasFixedSize(true);
        highScoreList.setAdapter(hsAdapter);
        highScoreList.setItemAnimator(new DefaultItemAnimator());

        return rootView;
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
