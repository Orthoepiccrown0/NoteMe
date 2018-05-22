package com.epiccrown.me.note.noteme.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epiccrown.me.note.noteme.Editor;
import com.epiccrown.me.note.noteme.Helpers.NotesAdapter;
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

/**
 * Created by Epiccrown on 28.04.2018.
 */

public class SecretsFragment extends Fragment {

    private static List<Note> notes = null;
    private NotesAdapter notesAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private SwipeRefreshLayout mSwipeLayout;
    private CoordinatorLayout homeScreen;
    private ImageView noNotes_image;
    private TextView noNotes_text;

    private String idnote2delete;
    private boolean is2delete = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.foreach_notes_screen,null);

        noNotes_image = v.findViewById(R.id.home_nonotes_image);
        noNotes_text = v.findViewById(R.id.home_nonotes_text);
        homeScreen = v.findViewById(R.id.home_coordinator);
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

        User.isSecretToSend = true;
        fab = v.findViewById(R.id.home_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Editor.class);
                startActivity(intent);
            }
        });
        return v;
    }

    private class FetchItems extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String urls = "https://msg.altervista.org/note_me_rest/getnotes.php";
            Uri ENDPOINT = Uri.parse(urls)
                    .buildUpon()
                    .appendQueryParameter("iduser", User.current_id)
                    .appendQueryParameter("type","Secrets")
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

                }else{
                    noNotes_image.setVisibility(View.VISIBLE);
                    noNotes_text.setVisibility(View.VISIBLE);
                }
        }
    }

    private void setupRecycler() {
        setupRecyclerLayout();
        if (notes.size() > 0) {
            noNotes_image.setVisibility(View.GONE);
            noNotes_text.setVisibility(View.GONE);
            if (notesAdapter == null)
                notesAdapter = new NotesAdapter(getActivity(), notes);
            else {
                notesAdapter.bindNotes(notes);
            }
            recyclerView.setAdapter(notesAdapter);
        } else {
            noNotes_image.setVisibility(View.VISIBLE);
            noNotes_text.setVisibility(View.VISIBLE);
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String name = notes.get(viewHolder.getAdapterPosition()).getHeader().equals("") ?
                        notes.get(viewHolder.getAdapterPosition()).getContent() :
                        notes.get(viewHolder.getAdapterPosition()).getHeader();

                final Note deletedItem = notes.get(viewHolder.getAdapterPosition());
                idnote2delete = deletedItem.getId();
                final int deletedIndex = viewHolder.getAdapterPosition();
                notesAdapter.removeItem(viewHolder.getAdapterPosition());
                is2delete = true;
                new DeleteRestoreItem().execute();
                Snackbar snackbar = Snackbar
                        .make(homeScreen, "Item removed from notes!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notesAdapter.restoreItem(deletedItem, deletedIndex);
                        is2delete = false;
                        new DeleteRestoreItem().execute();
                    }
                });
                snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
                snackbar.show();
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    private void checkNotes(){
        if(notes.isEmpty()){
            noNotes_image.setVisibility(View.VISIBLE);
            noNotes_text.setVisibility(View.VISIBLE);
        }else{
            noNotes_image.setVisibility(View.GONE);
            noNotes_text.setVisibility(View.GONE);
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
        inflater.inflate(R.menu.home_menu, menu);
        MenuItem layout = menu.findItem(R.id.menu_home_layout);

        if (PreferencesNoteme.getRecyclerStyle(getActivity()))
            layout.setIcon(R.drawable.linearadapter);
        else
            layout.setIcon(R.drawable.home_straggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_home_layout:
                PreferencesNoteme.setRecyclerStyle(getActivity(), !PreferencesNoteme.getRecyclerStyle(getActivity()));
                try {
                    getActivity().invalidateOptionsMenu();
                    setupRecyclerLayout();
                    setupRecycler();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            case R.id.home_trash:
                try {
                    FragmentManager fragmentManager = getFragmentManager();
                    User.isSecretToSend = true;
                    Fragment fragment = new TrashBin();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_holder, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .commit();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class DeleteRestoreItem extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String url;
            if (is2delete)
                url = "https://msg.altervista.org/note_me_rest/note2trash.php";
            else
                url = "https://msg.altervista.org/note_me_rest/restorenote.php";

            Uri ENDPOINT = Uri.parse(url);
            ENDPOINT = ENDPOINT
                    .buildUpon()
                    .appendQueryParameter("iduser", User.current_id)
                    .appendQueryParameter("idnote", idnote2delete)
                    .appendQueryParameter("type","Secrets")
                    .build();
            return execURL(ENDPOINT);


        }

        @Override
        protected void onPostExecute(String s) {
            if (!s.equals("Success"))
                Toast.makeText(getActivity(), "Something gone wrong", Toast.LENGTH_SHORT).show();
            checkNotes();
        }

        private String execURL(Uri ENDPOINT) {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        new FetchItems().execute();
    }
}
