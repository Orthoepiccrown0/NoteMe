package com.epiccrown.me.note.noteme.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.epiccrown.me.note.noteme.User;

/**
 * Created by Epiccrown on 29.04.2018.
 */

public class DataHelper extends SQLiteOpenHelper {
    public static final String nameDB = "NoteMeDB";
    public static final int DB_VERSION = 2;

    public DataHelper(Context context) {
        super(context, nameDB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "Create Table Login("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "Username TEXT,"+
                "IdUser TEXT);";
        db.execSQL(query);

        query = "Create Table SecureNotes("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "Password TEXT);";
        db.execSQL(query);
    }

    public static void insertUser(SQLiteDatabase db,String username, String IdUser){
        ContentValues values = new ContentValues();
        values.put("Username",username);
        values.put("IdUser",IdUser);
        db.insert("Login",null, values);
    }

    public static void deleteUser(SQLiteDatabase db,String username){
        db.delete("Login",null,null);
    }

    public static void updateUser(SQLiteDatabase db,String username){
        ContentValues values = new ContentValues();
        values.put("Username",username);
        db.update("Login",values,"Username=?",new String[]{User.username});
        User.username = username;
    }

    public static void insertSecretPassword(SQLiteDatabase db,String pass){
        ContentValues values = new ContentValues();
        values.put("Password",pass);
        db.insert("SecureNotes",null, values);
    }

    public static boolean changeSecretPassword(SQLiteDatabase db,String pass, String oldpass){
        ContentValues values = new ContentValues();
        values.put("Password",pass);
        return db.update("SecureNotes", values, "Password=?", new String[]{oldpass}) > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public static void deleteSecretPass(SQLiteDatabase db){
        db.execSQL("Delete From SecureNotes");
    }

    public static SQLiteDatabase getDB(Context context){
        DataHelper helper = new DataHelper(context);
        return helper.getReadableDatabase();
    }
}
