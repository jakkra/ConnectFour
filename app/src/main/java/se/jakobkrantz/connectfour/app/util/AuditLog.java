package se.jakobkrantz.connectfour.app.util;
/*
 * Created by jakkra on 2015-10-08.
 */

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Used for logging information with a timestamp. Data will be saved local
 * so only the app itself can read the data. It's not readable or editable from
 * outside the app or writable by the user.
 */
public class AuditLog {
    private File file;
    private DateFormat dateFormat;

    public AuditLog(Context context, String fileName) {
        file = new File(context.getFilesDir(), fileName);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    }

    /**
     * Writes a message and appends it to the log, adds a newline at the end.
     *
     * @param message message to be written
     */
    public void writeLog(String message) {

        try {
            Date date = new Date();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.append(dateFormat.format(date) + ": " + message + "\n");
            bufferedWriter.close();

        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Could not create a BufferedWriter", e);
            e.printStackTrace();
        }
    }

    /**
     * For reading the log from inside the app.
     *
     * @return the full log
     */
    public String readLog() {
        Log.e("Audit file path", file.getAbsolutePath());
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            return text.toString();
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Error reading from auditLog", e);
        }
        return "";
    }
}
