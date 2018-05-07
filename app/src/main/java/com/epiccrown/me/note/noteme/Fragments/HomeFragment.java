package com.epiccrown.me.note.noteme.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epiccrown.me.note.noteme.Helpers.DataHelper;
import com.epiccrown.me.note.noteme.Helpers.HomeAdapter;
import com.epiccrown.me.note.noteme.Helpers.MD5helper;
import com.epiccrown.me.note.noteme.LoginScreen;
import com.epiccrown.me.note.noteme.Note;
import com.epiccrown.me.note.noteme.R;
import com.epiccrown.me.note.noteme.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Epiccrown on 28.04.2018.
 */

public class HomeFragment extends Fragment {
    private List<Note> notes;
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment,null);
        tryLogin();
        notes = new ArrayList<>();
        new FetchItems().execute();
        recyclerView = v.findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    private void tryLogin(){
        DataHelper dataHelper = new DataHelper(getActivity());
        SQLiteDatabase database = dataHelper.getReadableDatabase();

        Cursor cursor = database.query("Login",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            User.current_id = cursor.getString(2);
            //User.password = cursor.getString(2);
            User.username = cursor.getString(1);
        }else{
            Intent intent = new Intent(getActivity(), LoginScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        database.close();
    }

    private class FetchItems extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            String urls = "https://msg.altervista.org/note_me_rest/getnotes.php";
            Uri ENDPOINT = Uri.parse(urls)
                    .buildUpon()
                    .appendQueryParameter("iduser", User.current_id)
                    .build();
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
            //super.onPostExecute(s);
            if(!s.equals("Empty")){
                try{
                    notes.clear();
                    JSONArray array = new JSONArray(s);
                    for(int i=0;i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        Note note = Note.Builder()
                                .setContent(object.getString("content"))
                                .setHeader(object.getString("header"))
                                .setId(object.getString("idnote"));
                        notes.add(note);
                    }
                    setupRecycler();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }
    }

    private void setupRecycler() {
        if(notes.size()>0) {
            //if(homeAdapter==null)
            homeAdapter = new HomeAdapter(getActivity(),notes);
            //homeAdapter.bindNotes(notes);
            recyclerView.setAdapter(homeAdapter);
        }
    }
}
