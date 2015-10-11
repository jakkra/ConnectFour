package se.jakobkrantz.connectfour.app.fragments;
/*
 * Created by jakkra on 2015-09-28.
 */

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import se.jakobkrantz.connectfour.app.util.Commons;
import se.jakobkrantz.connectfour.app.R;
import se.jakobkrantz.connectfour.app.util.Commons.GameState;

import java.io.File;
import java.io.IOException;


/**
 * The main fragment which handles and displays the different menu options for the user.
 */
public class MenuFragment extends Fragment implements GameStartDialog.GameStartListener, View.OnClickListener {
    private FragmentEventListener eventListener;
    private Button resumeButton, newGameButton, highscoreButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_home, container, false);
        resumeButton = (Button) rootView.findViewById(R.id.resume_button);
        newGameButton = (Button) rootView.findViewById(R.id.new_game_button);
        highscoreButton = (Button) rootView.findViewById(R.id.highscore_button);
        Button saveAudit = (Button) rootView.findViewById(R.id.save_audit_button);
        saveAudit.setOnClickListener(this);
        newGameButton.setOnClickListener(this);
        highscoreButton.setOnClickListener(this);
        resumeButton.setOnClickListener(this);
        handleVisibilityOfResumeButton();
        return rootView;
    }

    /**
     * shows or hides the resume button deeding on if there are a saved game or not.
     */
    private void handleVisibilityOfResumeButton() {
        File f = getActivity().getFileStreamPath(Commons.SAVED_GAME_NAME);
        if (!f.exists()) {
            resumeButton.setVisibility(View.INVISIBLE);
        } else {
            resumeButton.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onGameStart(String p1, String p2) {
        Bundle args = new Bundle();
        args.putString(Commons.PLAYER_ONE_NAME_TAG, p1);
        args.putString(Commons.PLAYER_TWO_NAME_TAG, p2);
        eventListener.onEvent(GameState.IN_GAME, args);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.new_game_button) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            GameStartDialog dialog = new GameStartDialog();
            dialog.setTargetFragment(this, 1);
            dialog.show(fm, "GameStart");
        } else if (v.getId() == R.id.resume_button) {
            eventListener.onEvent(GameState.IN_GAME, new Bundle());
        } else if (v.getId() == R.id.highscore_button) {
            eventListener.onEvent(GameState.HIGHSCORE, null);
        } else if (v.getId() == R.id.save_audit_button) {
            try {
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/" + Commons.AUDIT_LOG_FILE_NAME);
                Commons.copyFileToDest(getActivity().getFileStreamPath(Commons.AUDIT_LOG_FILE_NAME), file);
                Toast.makeText(getActivity(), "Audit log saved in your downloads folder", Toast.LENGTH_LONG).show();
                openFileWithTextEditor(file);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Tries to open the default text editor on the users phone with the specified file.
     *
     * @param file file that will be opened in the external app.
     */
    private void openFileWithTextEditor(File file) {
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
            String mimeType = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            Log.d("mime type", mimeType);
            intent.setDataAndType(Uri.fromFile(file), mimeType);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "Found no app to open .txt file with\n file saved at: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Make sure that the container activity has implemented
        // the listener interface. If not, it throws an exception.
        try {
            eventListener = (FragmentEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnEventListener");
        }
    }
}
