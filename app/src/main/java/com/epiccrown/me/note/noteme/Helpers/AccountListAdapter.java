package com.epiccrown.me.note.noteme.Helpers;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.epiccrown.me.note.noteme.Fragments.ChangePasswordDialog;
import com.epiccrown.me.note.noteme.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Epiccrown on 03.05.2018.
 */

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ItemHolder> {

    private Context mContext;
    private List<AccountListItem> items;

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

                   }else if(service.equals("Security password")){
                       ChangePasswordDialog dialog = new ChangePasswordDialog();
                       Bundle bundle = new Bundle();
//                       FragmentManager fm = ((Activity) mContext).getSupportFragmentManager();
//                       bundle.putString("type","secret");
//                       dialog.setArguments(bundle);
//                       dialog.show;
                   }else if(service.equals("Delete account")){

                   }
                }
            });
        }
    }
}
