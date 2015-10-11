package se.jakobkrantz.connectfour.app;
/*
 * Created by jakkra on 2015-10-08.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import se.jakobkrantz.connectfour.app.game.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Class used for storing high scores in android's SharedPreferences
 */
public class HighScores {
    public static final String PREFERENCES_FILE_NAME = "HIGHSCORE";


    /**
     * Increases the score for a player with one.
     *
     * @param c    Context associated with the SharedPreference.
     * @param name String containing the name of the player
     */
    public static void increaseScoreOne(Context c, String name) {
        int score = getHighScoreOf(c, name);
        if (score != -1) {
            score++;
            getEditor(c).putInt(name, score).commit();
        } else {
            getEditor(c).putInt(name, 1).commit();
        }
    }

    /**
     * Add a new player to the high score with initial score zero.
     *
     * @param c    Context associated with the SharedPreference.
     * @param name String containing the name of the player
     */
    public static void addPlayer(Context c, String name) {
        if (getHighScoreOf(c, name) == -1) {
            getEditor(c).putInt(name, 0);
        }
    }

    /**
     * Get the high score of a player
     *
     * @param c    the Context associated with the SharedPreference.
     * @param name String with the name of the player
     * @return the score of the player
     */
    public static int getHighScoreOf(Context c, String name) {
        return getPreferences(c).getInt(name, -1);

    }

    /**
     * Returns a sorted List with all Players and their associated score
     *
     * @param c the Context associated with the SharedPreference
     * @return a sorted List with all players and their score
     */
    public static ArrayList<Player> getFullHighScores(Context c) {
        ArrayList<Player> hs = new ArrayList<Player>();
        Map<String, Integer> map = (Map<String, Integer>) getPreferences(c).getAll();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String name = entry.getKey();
            int score = entry.getValue();
            hs.add(new Player(name, score));
        }
        Collections.sort(hs);
        return hs;
    }

    /**
     * Returns an editor for the SharedPreference
     *
     * @param context the Context associated with the SharedPreference
     * @return
     */
    private static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    /**
     * Returns a SharedPreference which data can be extracted from.
     *
     * @param context the Context associated with the SharedPreference
     * @return
     */
    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
    }
}
