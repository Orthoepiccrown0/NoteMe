package com.epiccrown.me.note.noteme.Fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.epiccrown.me.note.noteme.Helpers.HomeAdapter;
import com.epiccrown.me.note.noteme.Helpers.PreferencesNoteme;
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

public class TrashBin extends Fragment {
    private static List<Note> notes = null;
    private HomeAdapter homeAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView noTrash_text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, null);
        noTrash_text = v.findViewById(R.id.empty_trash_text);
        mSwipeLayout = v.findViewById(R.id.home_swipeRefreshLayout);
        mSwipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
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
        fab.hide();
        return v;
    }

    private class FetchItems extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String urls = "https://msg.altervista.org/note_me_rest/gettrash.php";
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
                    noTrash_text.setVisibility(View.GONE);
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

                } else {
                    notes.clear();
                    noTrash_text.setVisibility(View.VISIBLE);
                    setupRecycler();
                }
        }
    }

    private class ClearTrash extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String urls = "https://msg.altervista.org/note_me_rest/cleartrash.php";
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
            if (s.equals("Success"))
                Toast.makeText(getActivity(), "Cleared trash", Toast.LENGTH_SHORT).show();
            new FetchItems().execute();
        }
    }

    private class RestoreTrash extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String urls = "https://msg.altervista.org/note_me_rest/restorenotes.php";
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


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new FetchItems().execute();
        }
    }

    private void setupRecycler() {
        setupRecyclerLayout();
        if (notes.size() > 0) {
            noTrash_text.setVisibility(View.GONE);
            //if(homeAdapter==null)
            if (homeAdapter == null)
                homeAdapter = new HomeAdapter(getActivity(), notes);
            else {
                homeAdapter.bindNotes(notes);
                //homeAdapter.notifyDataSetChanged();
            }
            recyclerView.setAdapter(homeAdapter);
        } else {
            noTrash_text.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerLayout() {
        if (!PreferencesNoteme.getRecyclerStyle(getActivity()))
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        else
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.trash_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.trash_clear:
                new ClearTrash().execute();
                return true;
            case R.id.trash_restore:
                new RestoreTrash().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
