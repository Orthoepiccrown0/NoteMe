package com.epiccrown.me.note.noteme.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.epiccrown.me.note.noteme.Editor;
import com.epiccrown.me.note.noteme.Helpers.DataHelper;
import com.epiccrown.me.note.noteme.Helpers.HomeAdapter;
import com.epiccrown.me.note.noteme.Helpers.PreferencesNoteme;
import com.epiccrown.me.note.noteme.LoginScreen;
import com.epiccrown.me.note.noteme.Note;
import com.epiccrown.me.note.noteme.R;
import com.epiccrown.me.note.noteme.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Epiccrown on 28.04.2018.
 */

public class HomeFragment extends Fragment implements  Serializable {
    private static List<Note> notes = null;
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private SwipeRefreshLayout mSwipeLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, null);
        tryLogin();
        mSwipeLayout = v.findViewById(R.id.home_swipeRefreshLayout);
        mSwipeLayout.setColorSchemeColors(getResources().getColor(R.color.orange_theme_dark));
        mSwipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new FetchItems().execute();
                    }
                }
        );
        notes = new ArrayList<>();
        new FetchItems().execute();
        recyclerView = v.findViewById(R.id.home_recycler_view_danger);
        setupRecyclerLayout();


        fab = v.findViewById(R.id.home_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Editor.class);
                startActivity(intent);
            }
        });
        return v;
    }

    private void tryLogin() {
        DataHelper dataHelper = new DataHelper(getActivity());
        SQLiteDatabase database = dataHelper.getReadableDatabase();

        Cursor cursor = database.query("Login", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            User.current_id = cursor.getString(2);
            //User.password = cursor.getString(2);
            User.username = cursor.getString(1);
        } else {
            Intent intent = new Intent(getActivity(), LoginScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        cursor.close();
        database.close();
    }



    private class FetchItems extends AsyncTask<Void, Void, String> {

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
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            if (s != null)
                if (!s.equals("Empty")) {
                    try {
                        notes.clear();
                        JSONArray array = new JSONArray(s);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String content;
                            String header;
                            String color;

                            if (object.isNull("content")) content = "";
                            else content = object.getString("content");
                            if (object.isNull("header")) header = "";
                            else header = object.getString("header");
                            if (object.isNull("color")) color = "";
                            else color = object.getString("color");
                            Note note = Note.Builder()
                                    .setContent(content)
                                    .setHeader(header)
                                    .setColor_bg(color)
                                    .setId(object.getString("idnote"));

                            notes.add(note);
                        }
                        mSwipeLayout.setRefreshing(false);
                        setupRecycler();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
        }
    }

    private void setupRecycler() {
        if (notes.size() > 0) {
            //if(homeAdapter==null)
            if (homeAdapter == null)
                homeAdapter = new HomeAdapter(getActivity(), notes);
            else
                homeAdapter.bindNotes(notes);
            recyclerView.setAdapter(homeAdapter);
        }
    }

    private void setupRecyclerLayout(){
        if (!PreferencesNoteme.getRecyclerStyle(getActivity()))
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        else
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu,menu);
        MenuItem layout = menu.findItem(R.id.menu_home_layout);

        if(PreferencesNoteme.getRecyclerStyle(getActivity()))
            layout.setIcon(R.drawable.home_straggle);
        else
            layout.setIcon(R.drawable.home_linearl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_home_layout:
                PreferencesNoteme.setRecyclerStyle(getActivity(),!PreferencesNoteme.getRecyclerStyle(getActivity()));
                try{
                    getActivity().invalidateOptionsMenu();
                    setupRecyclerLayout();
                    setupRecycler();
                }catch (Exception ex){ex.printStackTrace();}
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
