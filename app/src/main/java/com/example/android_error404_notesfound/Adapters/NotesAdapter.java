package com.example.android_error404_notesfound.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_error404_notesfound.Activities.NoteDetail;
import com.example.android_error404_notesfound.ModelClasses.Notes;
import com.example.android_error404_notesfound.R;
import com.example.android_error404_notesfound.RoomDatabase.NotesDB;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

   List<Notes> notesList;
   Context context;
   private Filter filter;

    public NotesAdapter(Context context) {
        this.context = context;
    }

    public List<Notes> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<Notes> notesList) {
        this.notesList = notesList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.notes_cell_layout,parent,false);
        return new ViewHolder(view);
    }


    public void filterList(List<Notes> filteredList) {
        notesList =  filteredList;
        notifyDataSetChanged();
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        final Notes currNote = notesList.get(position);



        holder.notes.setText(currNote.getTitle());

        holder.mycardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Toast.makeText(getContext(), currNote.getAudioPath(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), NoteDetail.class);
                    intent.putExtra("data",notesList.get(position));
                    context.startActivity(intent);

                    Toast.makeText(getContext(), "No audio", Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), currNote.getImagePath().toString(), Toast.LENGTH_LONG).show();

            }
        });

//        holder.mycardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent myintent = new Intent(context, PlaceDetailActivity.class);
//                myintent.putExtra("place", mydata);
//
//                context.startActivity(myintent);
//                //  Toast.makeText(context,"position = "+position,Toast.LENGTH_LONG).show();
//
//            }
//        });





    }




    @Override
    public int getItemCount() {
        return notesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notes;
        CardView mycardview;



        public ViewHolder(@NonNull View itemView) {


            super(itemView);


            mycardview = itemView.findViewById(R.id.newcardNote);
            notes = itemView.findViewById( R.id.textView1 );



        }
    }

    public void deleteItem(int position) {

        Notes notes = notesList.get(position);
        NotesDB userDatabase = NotesDB.getInstance(getContext());
        userDatabase.daoObjct().delete(notes);
        Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
        notesList.remove(position);
        notifyDataSetChanged();

    }
}
