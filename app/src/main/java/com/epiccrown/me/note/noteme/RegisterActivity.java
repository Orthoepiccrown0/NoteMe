package com.epiccrown.me.note.noteme;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epiccrown.me.note.noteme.Helpers.DataHelper;
import com.epiccrown.me.note.noteme.Helpers.MD5helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.BufferUnderflowException;

public class RegisterActivity extends AppCompatActivity {

    private Button button;
    private EditText name;
    private EditText username;
    private EditText password;

    private Handler handler;


    private static final int MAKE_TOAST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registration");
        button = findViewById(R.id.btn_register);
        name = findViewById(R.id.input_name);
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);

        button.setOnClickListener(clickListener);

        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1 == MAKE_TOAST){
                    String toast = msg.getData().getString("toast");
                    makeToast(toast);
                }
            }
        };
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isChecked()) {
                registerNewUser();
            }
        }
    };

    private boolean isChecked(){
        String nameS = name.getText().toString();
        String usernameS = username.getText().toString();
        String passwordS = password.getText().toString();

        if(nameS.trim().equals("")){
            makeToast("Insert your name, please");
            return false;
        }
        else if(usernameS.trim().equals("")){
            makeToast("Insert your username");
            return false;
        }else if(passwordS.length()<4){
            makeToast("Your password must be unless 4 characters long");
            return false;
        }

        return true;
    }

    private void registerNewUser(){
        new UserRegistration().execute();
    }

    public class UserRegistration extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            final String nameS = name.getText().toString();
            final String usernameS = username.getText().toString();
            final String passwordS = MD5helper.getMD5string(password.getText().toString());

            try {
                String ENDPOINT = Uri.parse("https://msg.altervista.org/note_me_rest/register.php")
                        .buildUpon()
                        .appendQueryParameter("name",nameS)
                        .appendQueryParameter("username",usernameS)
                        .appendQueryParameter("password",passwordS)
                        .build().toString();

                URL url = new URL(ENDPOINT);

                // Read all the text returned by the server
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
            //super.onPostExecute(s);
            if(s==null){
                makeToast("Something gone wrong");
                return;
            }
            if(s.equals("User exist")){
                makeToast("The username is already taken!");
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String username = jsonObject.getString("username");
                    String passwordHash = jsonObject.getString("password");
                    String id = jsonObject.getString("iduser");

                    User.current_id = id;
                    User.username = username;
                    User.password = passwordHash;

                    DataHelper dataHelper = new DataHelper(RegisterActivity.this);
                    SQLiteDatabase db = dataHelper.getWritableDatabase();
                    DataHelper.insertUser(db,username,passwordHash,id);

                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void makeToast(String toast){
        Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
    }

}
