package se.jakobkrantz.connectfour.app;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import se.jakobkrantz.connectfour.app.fragments.BoardFragment;
import se.jakobkrantz.connectfour.app.fragments.HighScoreFragment;
import se.jakobkrantz.connectfour.app.fragments.MenuFragment;
import se.jakobkrantz.connectfour.app.Commons.GameState;

/**
 * Parent activity that hide and shows different fragments, depending on the state of the app.
 */
public class GameHome extends ActionBarActivity implements FragmentEventListener {
    public GameState state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game_home);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MenuFragment())
                    .commit();
        }
    }

    @Override
    public void onEvent(GameState gameState, Bundle args) {
        this.state = gameState;
        switch (gameState) {
            case IN_GAME:
                BoardFragment fragment = new BoardFragment();
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack("inGame").commit();
                break;
            case HOME:
                MenuFragment menuFragment = new MenuFragment();
                menuFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, menuFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                break;
            case HIGHSCORE:
                HighScoreFragment hsFragment = new HighScoreFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, hsFragment).addToBackStack("highscore").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

        }
    }

}
