package com.epiccrown.me.note.noteme.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Epiccrown on 29.04.2018.
 */

public class DataHelper extends SQLiteOpenHelper {
    public static final String nameDB = "NoteMeDB";
    public static final int DB_VERSION = 1;

    public DataHelper(Context context) {
        super(context, nameDB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "Create Table Login("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "Username TEXT,"+
                "Password TEXT);";
        db.execSQL(query);
        //

    }

    public static void insertUser(SQLiteDatabase db,String username, String password){
        ContentValues values = new ContentValues();
        values.put("Username",username);
        values.put("Password",password);
        db.insert("Login",null, values);
    }

    public static void deleteUser(SQLiteDatabase db,String username){
        ContentValues values = new ContentValues();
        values.put("Username",username);
        db.delete("Login","Username=?",new String[]{username});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
