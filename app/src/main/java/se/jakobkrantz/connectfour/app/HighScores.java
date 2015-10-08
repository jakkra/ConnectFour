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

public class HighScores {
    public static final String PREFERENCES_FILE_NAME = "HIGHSCORE";


    public static void increaseScore(Context c, String name){
        Log.e("Highscores increase score", name);
        int score = getHighScoreOf(c, name);
        if(score != -1){
            score++;
            getEditor(c).putInt(name, score).commit();
        } else {
            getEditor(c).putInt(name, 1).commit();
        }
    }

    public static void addPlayer(Context c, String name){
        if(getHighScoreOf(c,name) == -1){
            getEditor(c).putInt(name, 0);
        }
    }

    public static  int getHighScoreOf(Context c, String name){
        return getPreferences(c).getInt(name, -1);

    }

    public static  ArrayList<Player> getFullHighScores(Context c){
        ArrayList<Player> hs = new ArrayList<Player>();
        Map<String, Integer> map = (Map<String, Integer>) getPreferences(c).getAll();
        for(Map.Entry<String, Integer> entry : map.entrySet()){
            String name = entry.getKey();
            int score = entry.getValue();
            hs.add(new Player(name, score));
        }
        Collections.sort(hs);
        return hs;
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
    }
}
