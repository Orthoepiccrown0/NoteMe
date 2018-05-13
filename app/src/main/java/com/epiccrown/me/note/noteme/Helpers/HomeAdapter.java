package com.epiccrown.me.note.noteme.Helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epiccrown.me.note.noteme.Editor;
import com.epiccrown.me.note.noteme.Note;
import com.epiccrown.me.note.noteme.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.NoteHolder> {
    private Context mContext;
    private List<Note> notes;

    public HomeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public HomeAdapter(Context mContext, List<Note> notes) {
        this.mContext = mContext;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.note_item_layout,parent,false);

        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = notes.get(position);
        holder.bindNote(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void bindNotes(List<Note> notes){
        this.notes = notes;
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        TextView header;
        TextView content;
        LinearLayout note_item_layout;
        Note note;
        NoteHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.note_header_item);
            content = itemView.findViewById(R.id.note_content_item);
            note_item_layout = itemView.findViewById(R.id.note_item);
        }

        void bindNote(final Note note){
            this.note = note;
            if(note.getHeader().equals("")){
               header.setVisibility(View.GONE);
               content.setTextSize(18);
                content.setText(note.getContent());
            }else if(note.getContent().equals("")){
                content.setVisibility(View.GONE);
                header.setTextSize(18);
                header.setText(note.getHeader());
            }else {
                header.setText(note.getHeader());
                content.setText(note.getContent());
            }
            if(!note.getColor_bg().trim().equals(""))
            note_item_layout.setBackground(new ColorDrawable(Color.parseColor(note.getColor_bg())));
            //edit

            note_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,Editor.class);
                    intent.putExtra("note",note);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
