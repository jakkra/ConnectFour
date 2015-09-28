package se.jakobkrantz.connectfour.app;/*
 * Created by jakkra on 2015-09-28.
 */

import android.os.Bundle;

public interface FragmentEventListener {
    public void onEvent(GameHome.GameState gameState, Bundle args);
}
