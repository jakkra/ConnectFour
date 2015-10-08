package se.jakobkrantz.connectfour.app.fragments;
/*
 * Created by jakkra on 2015-10-08.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import se.jakobkrantz.connectfour.app.R;
import se.jakobkrantz.connectfour.app.game.Player;

import java.util.ArrayList;


public class HighScoreListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private ArrayList<Player> hsList;

    public HighScoreListAdapter(ArrayList<Player> hsList) {
        this.hsList = hsList;
        Log.e("HS adapter", hsList.toString());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.high_score_list_item, viewGroup, false);
            return new ViewHolderItem(v);

        } else {
            Log.e("WRONG TYPE, not supported in this recycleView", "Returning null!");
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolderItem) {
            TextView row = ((ViewHolderItem) viewHolder).textView;
            row.setText(hsList.get(i).getName() + " : " +hsList.get(i).getScore());
        }
    }

    @Override
    public int getItemCount() {
        return hsList.size();
    }



    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        public final TextView textView;

        public ViewHolderItem(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.hs_name_score_tv);
        }
    }

}
