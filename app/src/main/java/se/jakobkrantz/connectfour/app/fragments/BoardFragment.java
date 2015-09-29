package se.jakobkrantz.connectfour.app.fragments;
/*
 * Created by jakkra on 2015-09-28.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import de.hdodenhof.circleimageview.CircleImageView;
import se.jakobkrantz.connectfour.app.FragmentEventListener;
import se.jakobkrantz.connectfour.app.R;
import se.jakobkrantz.connectfour.app.game.*;


public class BoardFragment extends Fragment {
    private ConnectFourGame connectFourGame;
    private GridView boardView;
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
        boardView = (GridView) rootView.findViewById(R.id.board_grid);
        boardView.setNumColumns(COLUMNS);
        whosTurn = (TextView) rootView.findViewById(R.id.whos_turn_tv);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        String playerOneName = (String) args.get("p1");
        String playerTwoName = (String) args.get("p2");
        p1 = new Player(playerOneName, 0);
        p2 = new Player(playerTwoName, 0);
        connectFourGame = new ConnectFourGame(COLUMNS, ROWS, p1, p2);
        updateUiTurn();
        boardCells = new CircleImageView[COLUMNS][ROWS];
        CircleImageView imageView;

        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
                imageView = new CircleImageView(context);
                imageView.setBorderColor(Color.WHITE);
                imageView.setBorderWidth(5);
                imageView.setImageResource(R.drawable.ic_launcher);
                imageView.setVisibility(ImageView.VISIBLE);
                boardCells[col][row] = imageView;

            }
        }
        boardView.setAdapter(new ImageAdapter(context, boardCells));
        boardView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int colIndex = position % COLUMNS;
                int rowIndex = position / COLUMNS;
                Toast.makeText(context, "Position: " + position + " Column: " + colIndex + " Row: " + rowIndex + " clicked", Toast.LENGTH_SHORT).show();
                if (connectFourGame.dropTile(colIndex)) {
                    updateUiTurn();
                    XYCoord lastDrop = connectFourGame.getLastDropCoord();
                    boardCells[lastDrop.getX()][lastDrop.getY()].setImageResource(R.drawable.abc_ic_menu_copy_mtrl_am_alpha);
                    boardView.invalidateViews();
                    Log.d(getClass().toString(), "onItemClick col: " + lastDrop.getX());

                }
            }
        });


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

    private void updateUiTurn() {
        if (connectFourGame.turnIs().equals(p1)) {
            whosTurn.setBackgroundColor(Color.BLUE);
        } else {
            whosTurn.setBackgroundColor(Color.RED);
        }
    }

    private XYCoord invertXYCoord(XYCoord xy){
        return new XYCoord(COLUMNS - xy.getX(), ROWS - xy.getY());
    }

    private int currentColor() {
        if (connectFourGame.turnIs().equals(p1)) {
            return Color.BLUE;
        } else {
            return Color.RED;
        }
    }



    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private CircleImageView[][] images;

        public ImageAdapter(Context c, CircleImageView[][] images) {
            this.images = images;
            context = c;
        }

        //---returns the number of images---
        public int getCount() {
            return images.length * images[0].length;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                return images[position % COLUMNS][position % ROWS];

            } else {
                return convertView;
            }


        }
    }

}