package com.epiccrown.me.note.noteme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {

    TextView newAccount;
    EditText username;
    EditText password;
    Button try_login;

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

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.link_signup){
                Intent intent = new Intent(LoginScreen.this,RegisterActivity.class);
                startActivity(intent);
            }else if(v.getId() == R.id.btn_login){
                //try to login
            }
        }
    };

}
