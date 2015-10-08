package se.jakobkrantz.connectfour.app;
/*
 * Created by jakkra on 2015-10-08.
 */

import android.content.Context;
import android.util.Log;

import java.io.*;

public class AuditLog {

    private File file;

    public AuditLog(Context context, String fileName){
        file = new File(context.getFilesDir(), fileName);
    }

    public void writeLog(String message){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.append(message + "\n");
            bufferedWriter.close();

        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Could not create a BufferedWriter", e);
            e.printStackTrace();
        }
    }

    public String readLog(){
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
        }
        catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Error reading from auditLog", e);
        }
        return "";
    }
}
