package com.epiccrown.me.note.noteme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.epiccrown.me.note.noteme.Helpers.DataHelper;
import com.epiccrown.me.note.noteme.Helpers.MD5helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class LoginScreen extends AppCompatActivity {

    private TextView newAccount;
    private EditText username;
    private EditText password;
    private Button try_login;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        //getActionBar().setElevation(0);
        setTitle("Login");

        newAccount = findViewById(R.id.link_signup);
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        try_login = findViewById(R.id.btn_login);

        try_login.setOnClickListener(clickListener);
        newAccount.setOnClickListener(clickListener);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Filling the coffee cup..");
        progressDialog.setTitle("Wait a moment");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        password.setOnEditorActionListener(editorActionListener);

    }

    EditText.OnEditorActionListener editorActionListener = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                launchMissle();
                return true;
            }
            return false;
        }

    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.link_signup){
                Intent intent = new Intent(LoginScreen.this,RegisterActivity.class);
                startActivity(intent);
            }else if(v.getId() == R.id.btn_login){
                //try to login
                //Nope
                launchMissle();
            }
        }
    };

    private void launchMissle(){
        String username_str = username.getText().toString();
        String password_str = MD5helper.getMD5string(password.getText().toString());

        if(username_str.trim().equals("")&&password_str.trim().equals("")){
            Toast.makeText(LoginScreen.this,getResources().getString(R.string.login_error_data),Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.show();
            new Login2System().execute();
        }
    }

    public class Login2System extends AsyncTask<Void, Void, String>{


        @Override
        protected String doInBackground(Void... voids) {
            String username_str = username.getText().toString();
            String password_str = MD5helper.getMD5string(password.getText().toString());
            try {
                Uri ENDPOINT =  Uri.parse("https://msg.altervista.org/note_me_rest/login.php");
                ENDPOINT = ENDPOINT
                        .buildUpon()
                        .appendQueryParameter("username",username_str)
                        .appendQueryParameter("password",password_str)
                        .build();

                URL url = new URL(ENDPOINT.toString());

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
            if(s.equals("Nope")){
                progressDialog.dismiss();
                Toast.makeText(LoginScreen.this,"Check your data",Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String username = jsonObject.getString("username");
                    String passwordHash = jsonObject.getString("password");
                    String id = jsonObject.getString("iduser");

                    User.current_id = id;
                    User.username = username;
                    User.password = passwordHash;

                    DataHelper dataHelper = new DataHelper(LoginScreen.this);
                    SQLiteDatabase db = dataHelper.getWritableDatabase();
                    DataHelper.insertUser(db,username,passwordHash,id);
                    db.close();
                    Intent intent = new Intent(LoginScreen.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
