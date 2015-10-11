package se.jakobkrantz.connectfour.app.fragments;
/*
 * Created by jakkra on 2015-10-08.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import se.jakobkrantz.connectfour.app.util.HighScores;
import se.jakobkrantz.connectfour.app.R;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Fragment that contains a high score list.
 */
public class HighScoreFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_high_score, container, false);
        RecyclerView highScoreList = (RecyclerView) rootView.findViewById(R.id.hs_list);
        HighScoreListAdapter hsAdapter = new HighScoreListAdapter(HighScores.getFullHighScores(getActivity()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        highScoreList.setLayoutManager(mLayoutManager);
        highScoreList.setHasFixedSize(true);
        highScoreList.setAdapter(hsAdapter);
        highScoreList.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }
}
