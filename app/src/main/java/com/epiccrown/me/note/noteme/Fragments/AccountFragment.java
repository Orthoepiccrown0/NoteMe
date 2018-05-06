package com.epiccrown.me.note.noteme.Fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epiccrown.me.note.noteme.Helpers.AccountListAdapter;
import com.epiccrown.me.note.noteme.Helpers.AccountListItem;
import com.epiccrown.me.note.noteme.Helpers.DataHelper;
import com.epiccrown.me.note.noteme.Helpers.EditDialog;
import com.epiccrown.me.note.noteme.LoginScreen;
import com.epiccrown.me.note.noteme.R;
import com.epiccrown.me.note.noteme.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Epiccrown on 28.04.2018.
 */

public class AccountFragment extends Fragment {
    TextView exit;
    TextView username;
    ImageView edit;
    RecyclerView recyclerView;
    List<AccountListItem> items = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_fragment_scr,null);
        exit = v.findViewById(R.id.account_exit);
        username = v.findViewById(R.id.account_username);
        edit = v.findViewById(R.id.account_edit);
        recyclerView = v.findViewById(R.id.account_recycler_view);
        createAndSetupList();
        edit.setOnClickListener(listener);
        exit.setOnClickListener(listener);
        if(User.username!=null) username.setText(User.username);
        return v;
    }

    private void createAndSetupList() {
        items.clear();
        items.add(new AccountListItem(R.drawable.account_security,"Security"));
        items.add(new AccountListItem(R.drawable.account_secret,"Secret"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        AccountListAdapter listAdapter = new AccountListAdapter(getActivity(),items);
        recyclerView.setAdapter(listAdapter);

    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.account_exit){
                if(User.username!=null) {
                    DataHelper helper = new DataHelper(getActivity());
                    SQLiteDatabase database = helper.getWritableDatabase();
                    DataHelper.deleteUser(database,User.username);
                    database.close();
                    helper.close();
                    Intent intent = new Intent(getActivity(), LoginScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }else if(v.getId() == R.id.account_edit){
                String url = "https://msg.altervista.org/note_me_rest/change_username.php";

                Bundle bundle = new Bundle();
                bundle.putSerializable("url",url);
                bundle.putSerializable("title","Username");
                bundle.putSerializable("hint","Insert your new username");

                EditDialog editDialog = new EditDialog();
                editDialog.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                //editDialog.setTargetFragment(AccountFragment.this,0);
            }
        }
    };
}
