package com.epiccrown.me.note.noteme.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epiccrown.me.note.noteme.Helpers.DataHelper;
import com.epiccrown.me.note.noteme.Helpers.MD5helper;
import com.epiccrown.me.note.noteme.LoginScreen;
import com.epiccrown.me.note.noteme.R;
import com.epiccrown.me.note.noteme.User;

/**
 * Created by Epiccrown on 28.04.2018.
 */

public class HomeFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment,null);
        tryLogin();
        return v;
    }

    private void tryLogin(){
        DataHelper dataHelper = new DataHelper(getActivity());
        SQLiteDatabase database = dataHelper.getReadableDatabase();

        Cursor cursor = database.query("Login",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            User.current_id = cursor.getString(3);
            User.password = cursor.getString(2);
            User.username = cursor.getString(1);
        }else{
            Intent intent = new Intent(getActivity(), LoginScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        database.close();
    }
}
