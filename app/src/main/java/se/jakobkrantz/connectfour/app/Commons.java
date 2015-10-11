package se.jakobkrantz.connectfour.app;
/*
 * Created by jakkra on 2015-09-29.
 */


import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Common functions and values used by various classes inside the app.
 */
public class Commons {
    public static String SAVED_GAME_NAME = "connect4saved";
    public static String AUDIT_LOG_FILE_NAME = "audit_log.txt";
    public static String PLAYER_ONE_NAME_TAG = "P1";
    public static String PLAYER_TWO_NAME_TAG = "P2";
    public static String WINNER_TAG = "winner";

    public enum GameState {
        HOME, IN_GAME, HIGHSCORE, RESTART
    }

    /**
     * Copies a file in one destination and makes a copy which will be put at the destination.
     * @param sourceFile file that will be copied
     * @param destFile destination file/location that the copy will be put at.
     * @throws IOException
     */
     public static void copyFileToDest(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }



}
