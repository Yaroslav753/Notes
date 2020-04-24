package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public ArrayList<Note> mNotes;
    private Context context;

    public Adapter(Context applicationContext, ArrayList<Note> notes){
        context = applicationContext;
        mNotes = notes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.titleView);
            imageView = itemView.findViewById(R.id.trash);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, NoteView.class);
                        intent.putExtra("pos", pos);
                        intent.putExtra("title", mNotes.get(pos).GetTitle());
                        intent.putExtra("text", mNotes.get(pos).GetText());
                        context.startActivity(intent);
                    }
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    RemoveNote(pos);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Note currentNote = mNotes.get(i);

        viewHolder.titleView.setText(currentNote.GetTitle());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void AddNote(Note note){
        mNotes.add(note);
        notifyItemInserted(getItemCount());
    }

    public void UpdateNote(int pos, Note note){
        mNotes.set(pos, note);
        notifyItemChanged(pos);
    }

    public void RemoveNote(int pos){
        MainActivity.Instance.RemoveNote(mNotes.get(pos).GetId());
        mNotes.remove(pos);
        notifyItemRemoved(pos);
    }
}
