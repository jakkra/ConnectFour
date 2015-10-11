package se.jakobkrantz.connectfour.app;/*
 * Created by jakkra on 2015-09-28.
 */

import android.os.Bundle;

/**
 * Callback to pass data between two components, usually between an activity and a fragment.
 * Data contains of a requested state and some data.
 */
public interface FragmentEventListener {
    /**
     * Callback with the requested state and data.
     * @param gameStatestate requested from child
     * @param args data to be passed to parent
     */
    public void onEvent(Commons.GameState gameState, Bundle args);
}
