package com.epiccrown.me.note.noteme.Fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epiccrown.me.note.noteme.Helpers.AccountListAdapter;
import com.epiccrown.me.note.noteme.Helpers.AccountListItem;
import com.epiccrown.me.note.noteme.Helpers.DataHelper;
import com.epiccrown.me.note.noteme.Helpers.ChangeUsernameDialog;
import com.epiccrown.me.note.noteme.LoginScreen;
import com.epiccrown.me.note.noteme.R;
import com.epiccrown.me.note.noteme.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Epiccrown on 28.04.2018.
 */

public class AccountFragment extends Fragment {
    private static final int USERNAME_CHANGE_REQUEST=0;

    private TextView exit;
    private TextView username;
    private ImageView edit;
    private RecyclerView recyclerView;
    private List<AccountListItem> items = new ArrayList<>();

    private Uri ENDPOINT;
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

    private class UpdateUsername extends AsyncTask<String,Void,String> {
        private String username;
        @Override
        protected String doInBackground(String... strings) {
            try {
                for(String string : strings) username = string;
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
            if(s.equals("Updated successfully")){
                Toast.makeText(getActivity(),R.string.account_username_change_success,Toast.LENGTH_SHORT).show();
                DataHelper dataHelper = new DataHelper(getActivity());
                SQLiteDatabase sqLiteDatabase = dataHelper.getWritableDatabase();
                if(username!=null) {
                    DataHelper.updateUser(sqLiteDatabase, username);
                    AccountFragment.this.username.setText(username);
                }
            }else if(s.equals("User exist")){
                Toast.makeText(getActivity(),R.string.account_username_change_exist,Toast.LENGTH_SHORT).show();
            }
        }
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

                Bundle bundle = new Bundle();

                bundle.putSerializable("title","Username");
                bundle.putSerializable("hint","Insert your new username");

                ChangeUsernameDialog editDialog = new ChangeUsernameDialog();
                editDialog.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                editDialog.setTargetFragment(AccountFragment.this,USERNAME_CHANGE_REQUEST);
                if(fragmentManager!=null)
                editDialog.show(fragmentManager,"ChangeUsername");
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==USERNAME_CHANGE_REQUEST){

            String new_username = data.getStringExtra("newusername");
            if(new_username!=null) {
                String url = "https://msg.altervista.org/note_me_rest/change_username.php";
                ENDPOINT = Uri.parse(url)
                        .buildUpon()
                        .appendQueryParameter("newusername", new_username)
                        .appendQueryParameter("iduser", User.current_id)
                        .build();
                new UpdateUsername().execute(new_username);
            }
        }
    }
}
