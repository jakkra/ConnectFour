package se.jakobkrantz.connectfour.app;
/*
 * Created by jakkra on 2015-09-29.
 */

import java.io.*;

public class Commons {
    public enum GameState {
        HOME, IN_GAME, HIGHSCORE, RESTART
    }

    public static void copyFileUsingFileStreams(File source, File dest)throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}
