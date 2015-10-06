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
        boardCells = new CircleImageView[COLUMNS][ROWS];
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point coord = (Point) v.getTag(R.integer.image_view_coordinate_tag);
                Toast.makeText(context, "Column: " + coord.x + " Row: " + coord.y + " clicked", Toast.LENGTH_SHORT).show();
                if (connectFourGame.dropTile(coord.x)) {
                    updateUiTurn();
                    XYCoord lastDrop = connectFourGame.getLastDropCoord();
                    boardCells[lastDrop.getX()][lastDrop.getY()].setBorderColor(currentColor());
                    Log.d(getClass().toString(), "onItemClick col: " + lastDrop.getX() + " row: " + lastDrop.getY());

                }
            }
        };
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
                imageView = new CircleImageView(context);
                imageView.setBorderColor(Color.DKGRAY);
                imageView.setBorderWidth(5);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                imageView.setImageResource(R.drawable.ic_launcher);
                imageView.setTag(R.integer.image_view_coordinate_tag, new Point(col, row));
                imageView.setOnClickListener(clickListener);
                rowLayouts[row].addView(imageView);
                boardCells[col][row] = imageView;
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
        connectFourGame = new ConnectFourGame(COLUMNS, ROWS, p1, p2);
        updateUiTurn();


    }

       /* boardView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int colIndex = position % COLUMNS;
                int rowIndex = position / (COLUMNS + 1);
                Toast.makeText(context, "Position: " + position + " Column: " + colIndex + " Row: " + rowIndex + " clicked", Toast.LENGTH_SHORT).show();
                if (connectFourGame.dropTile(colIndex)) {
                    updateUiTurn();
                    XYCoord lastDrop = connectFourGame.getLastDropCoord();
                    boardCells[lastDrop.getX()][lastDrop.getY()].setBorderWidiews();
                    Log.d(getClass().toString(), "onItemClick col: " + lastDrop.getX() + " row: " + lastDrop.getY());

                }
            }
        });
    }*/

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
            whosTurn.setTextColor(Color.BLUE);
        } else {
            whosTurn.setTextColor(Color.RED);
        }
    }

    private XYCoord invertXYCoord(XYCoord xy) {
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
        private CircleImageView[] images;

        public ImageAdapter(Context c, CircleImageView[][] matrix) {
            images = new CircleImageView[matrix.length * matrix[0].length];
            int imageIndex = 0;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    images[imageIndex] = matrix[i][j];
                    imageIndex++;
                }

            }
            context = c;
        }

        //---returns the number of images---
        public int getCount() {
            return images.length;
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
                return images[position];
            } else {
                return convertView;
            }


        }
    }

}