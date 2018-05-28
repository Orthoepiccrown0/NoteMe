package com.epiccrown.me.note.noteme.Helpers;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epiccrown.me.note.noteme.Fragments.ChangePasswordDialog;
import com.epiccrown.me.note.noteme.LoginScreen;
import com.epiccrown.me.note.noteme.R;
import com.epiccrown.me.note.noteme.User;


import java.util.List;

/**
 * Created by Epiccrown on 03.05.2018.
 */

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ItemHolder> {

    public AccountListAdapter(Context mContext, List<AccountListItem> items, FragmentManager fm) {
        this.mContext = mContext;
        this.items = items;
        this.fm = fm;
    }

    private Context mContext;
    private List<AccountListItem> items;
    private FragmentManager fm;

    public AccountListAdapter(Context mContext, List<AccountListItem> items) {
        this.mContext = mContext;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.account_list_item,null);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        AccountListItem item = items.get(position);
        holder.bindItem(item);
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        RelativeLayout relativeLayout;
        public ItemHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.account_list_item_image);
            textView = itemView.findViewById(R.id.account_list_item_text);
            relativeLayout = itemView.findViewById(R.id.account_item_from_list);

        }

        public void bindItem(AccountListItem item){
            imageView.setImageResource(item.getImage());
            textView.setText(item.getAction());

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   String service = textView.getText().toString();
                   if(service.equals("Account password")){
                       ChangePasswordDialog dialog = new ChangePasswordDialog();
                       Bundle bundle = new Bundle();
                       bundle.putString("type","accpass");
                       dialog.setArguments(bundle);
                       dialog.show(fm,"ss");
                   }else if(service.equals("Security password")){
                       ChangePasswordDialog dialog = new ChangePasswordDialog();
                       Bundle bundle = new Bundle();
                       bundle.putString("type","secret");
                       dialog.setArguments(bundle);
                       dialog.show(fm,"ss");
                   }else if(service.equals("Delete account")){
                       AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                       alertDialog.setTitle("Attention");
                       alertDialog.setMessage("Are you sure to delete you account?\nAll information will be deleted!");
                       alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                                new DeleteUser().execute();
                           }
                       });
                       alertDialog.setNegativeButton("NO",null);
                       alertDialog.show();
                   }
                }
            });
        }

        public class DeleteUser extends AsyncTask<Void,Void,String>{

            @Override
            protected String doInBackground(Void... voids) {
                Uri ENDPOINT = Uri.parse("https://msg.altervista.org/note_me_rest/deleteuser.php");
                ENDPOINT = ENDPOINT
                        .buildUpon()
                        .appendQueryParameter("iduser", User.current_id)
                        .build();
                return URLContentDownloader.execURL(ENDPOINT);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null) {
                    if(s.equals("Success")){
                        Toast.makeText(mContext,"Success",Toast.LENGTH_SHORT).show();
                        try {
                            DataHelper helper = new DataHelper(mContext);
                            SQLiteDatabase database = helper.getWritableDatabase();
                            DataHelper.deleteUser(database, User.username);
                            DataHelper.deleteSecretPass(database);
                            database.close();
                            helper.close();
                            Intent intent = new Intent(mContext, LoginScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mContext.startActivity(intent);
                        }catch (Exception ex){ex.printStackTrace();}
                    }else{
                        Toast.makeText(mContext,"Something gone wrong",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }
}
