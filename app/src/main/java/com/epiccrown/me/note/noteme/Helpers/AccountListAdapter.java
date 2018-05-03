package com.epiccrown.me.note.noteme.Helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public ItemHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.account_list_item_image);
            textView = itemView.findViewById(R.id.account_list_item_text);

        }

        public void bindItem(AccountListItem item){
            imageView.setImageResource(item.getImage());
            textView.setText(item.getAction());
        }
    }
}
