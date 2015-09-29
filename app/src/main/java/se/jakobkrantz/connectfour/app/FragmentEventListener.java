package se.jakobkrantz.connectfour.app;/*
 * Created by jakkra on 2015-09-28.
 */

import android.os.Bundle;

public interface FragmentEventListener {
    public void onEvent(Commons.GameState gameState, Bundle args);
}
