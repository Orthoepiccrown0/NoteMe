package com.epiccrown.me.note.noteme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.epiccrown.me.note.noteme.Fragments.HomeFragment;
import com.epiccrown.me.note.noteme.Helpers.Sound;
import com.epiccrown.me.note.noteme.Helpers.SoundPlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Editor extends AppCompatActivity {
    List<View> bubbles = new ArrayList<>();
    ConstraintLayout editor_background;
    ConstraintLayout bottom_background;
    EditText header;
    EditText content;

    String bef_color = "";
    String bef_header = "";
    String bef_content = "";

    String curr_color = "";
    String curr_header = "";
    String curr_content = "";

    boolean isToEdit = false;
    private String user_id = User.current_id;
    private Note note;
    private SoundPlayer soundsoundPlayer;
    private boolean isFinishedPlaying= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_fragment_layout);
        bubbles.add(findViewById(R.id.color_bubble_1));
        bubbles.add(findViewById(R.id.color_bubble_2));
        bubbles.add(findViewById(R.id.color_bubble_3));
        bubbles.add(findViewById(R.id.color_bubble_4));
        bubbles.add(findViewById(R.id.color_bubble_5));
        bubbles.add(findViewById(R.id.color_bubble_6));
        bubbles.add(findViewById(R.id.color_bubble_7));
        bubbles.add(findViewById(R.id.color_bubble_8));
        bubbles.add(findViewById(R.id.color_bubble_9));

        for (View v : bubbles) v.setOnClickListener(bubbles_listener);

        editor_background = findViewById(R.id.editor_bg);
        bottom_background = findViewById(R.id.editor_bottom);
        header = findViewById(R.id.header);
        content = findViewById(R.id.editor_content);
        header.addTextChangedListener(hwatcher);
        content.addTextChangedListener(cwatcher);

        soundsoundPlayer = new SoundPlayer(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            restoreNote(bundle);
        } else {
            isToEdit = false;
        }
    }

    private TextWatcher cwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            curr_content = s.toString();

            if(s.subSequence(start,start+count).toString().toLowerCase().contains("димон")&&isFinishedPlaying){
                Sound sound = soundsoundPlayer.getSoundByName("dimooon.mp3");
                soundsoundPlayer.play(sound);
                isFinishedPlaying = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        isFinishedPlaying = true;
                    }
                }).start();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher hwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            curr_header = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void restoreNote(Bundle bundle) {
        try {
            note = (Note) bundle.get("note");
            if (note != null) {
                isToEdit = true;

                bef_header = note.getHeader();
                curr_header = bef_header;
                if (!bef_header.equals(""))
                    header.setText(note.getHeader());

                bef_content = note.getContent();
                curr_content = bef_content;
                if (!bef_content.equals(""))
                    content.setText(note.getContent());

                bef_color = note.getColor_bg().replace("#", "");
                curr_color = bef_color;
                if (!bef_color.equals("")) {
                    String primarycolor = note.getColor_bg();
                    int colordark = manipulateColor(Color.parseColor(primarycolor), 0.8f);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(primarycolor)));
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(colordark);
                    if (primarycolor.equals("#003049")) {
                        editor_background.setBackground(new ColorDrawable(Color.WHITE));
                        bottom_background.setBackground(new ColorDrawable(Color.WHITE));
                    } else {
                        editor_background.setBackground(new ColorDrawable(Color.parseColor(primarycolor)));
                        bottom_background.setBackground(new ColorDrawable(colordark));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private View.OnClickListener bubbles_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.color_bubble_1:
                    curr_color = "";
                    setBgColor("#003049");
                    return;
                case R.id.color_bubble_2:
                    curr_color = "ff8a80";
                    setBgColor("#ff8a80");
                    return;
                case R.id.color_bubble_3:
                    curr_color = "ffd180";
                    setBgColor("#ffd180");
                    return;
                case R.id.color_bubble_4:
                    curr_color = "ffff8d";
                    setBgColor("#ffff8d");
                    return;
                case R.id.color_bubble_5:
                    curr_color = "ccff90";
                    setBgColor("#ccff90");
                    return;
                case R.id.color_bubble_6:
                    curr_color = "a7ffeb";
                    setBgColor("#a7ffeb");
                    return;
                case R.id.color_bubble_7:
                    curr_color = "80d8ff";
                    setBgColor("#80d8ff");
                    return;
                case R.id.color_bubble_8:
                    curr_color = "82b1ff";
                    setBgColor("#82b1ff");
                    return;
                case R.id.color_bubble_9:
                    curr_color = "b388ff";
                    setBgColor("#b388ff");
                    return;
            }
        }
    };

    private void setBgColor(String primarycolor) {
        try {
            int colordark = manipulateColor(Color.parseColor(primarycolor), 0.8f);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(primarycolor)));
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colordark);
            if (primarycolor.equals("#003049")) {
                editor_background.setBackground(new ColorDrawable(Color.WHITE));
                bottom_background.setBackground(new ColorDrawable(Color.WHITE));
            } else {
                editor_background.setBackground(new ColorDrawable(Color.parseColor(primarycolor)));
                bottom_background.setBackground(new ColorDrawable(colordark));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    @SuppressLint("StaticFieldLeak")
    public class SaveEverything extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            if (!isToEdit) {
                    Uri ENDPOINT = Uri.parse("https://msg.altervista.org/note_me_rest/addnote.php");
                    ENDPOINT = ENDPOINT
                            .buildUpon()
                            .appendQueryParameter("iduser", user_id)
                            .appendQueryParameter("content", curr_content)
                            .appendQueryParameter("header", curr_header)
                            .appendQueryParameter("color", curr_color)
                            .build();
                    return execURL(ENDPOINT);
            } else {
                    Uri ENDPOINT = Uri.parse("https://msg.altervista.org/note_me_rest/editnote.php");
                    ENDPOINT = ENDPOINT
                            .buildUpon()
                            .appendQueryParameter("iduser", user_id)
                            .appendQueryParameter("idnote", note.getId())
                            .appendQueryParameter("content", curr_content)
                            .appendQueryParameter("header", curr_header)
                            .appendQueryParameter("color", curr_color)
                            .build();
                    return execURL(ENDPOINT);
            }
        }

        private String execURL(Uri ENDPOINT){
           try {
               URL url = new URL(ENDPOINT.toString());

               BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
               String str;
               String final_object = "";
               while ((str = in.readLine()) != null)
                   final_object = str;
               return final_object;
           }catch (Exception ex){ex.printStackTrace();}
           return null;
        }

        @Override
        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
            if (s != null)
                if (!s.equals("Success"))
                    Toast.makeText(Editor.this, "Something gone wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        String header_text = header.getText().toString().trim();
        String content_text = content.getText().toString().trim();

        if (isChanged() && (!content_text.equals("") || !header_text.equals("")))
            new SaveEverything().execute();
    }


    private boolean isChanged() {
        return !curr_content.equals(bef_content) || !curr_header.equals(bef_header) || !curr_color.equals(bef_color);
    }


}
