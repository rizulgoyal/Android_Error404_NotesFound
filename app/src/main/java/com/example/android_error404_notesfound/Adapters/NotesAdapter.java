package com.example.android_error404_notesfound.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_error404_notesfound.Activities.NoteDetail;
import com.example.android_error404_notesfound.ModelClasses.Notes;
import com.example.android_error404_notesfound.ModelClasses.ObjectSerializer;
import com.example.android_error404_notesfound.R;
import com.example.android_error404_notesfound.RoomDatabase.NotesDB;

import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

   List<Notes> notesList;
   Context context;
   private Filter filter;

    ArrayList<String> names;


    private static final String SHARED_PREF = "categories";

    private static final String KEY_NAME = "key";

    SharedPreferences sharedPreferences;




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
        holder.desc.setText(currNote.getDescription());


        holder.date.setText("Created On :" + currNote.getDateCreated().split(" ")[0]);

        holder.mycardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    //Toast.makeText(getContext(), currNote.getAudioPath(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), NoteDetail.class);
                    intent.putExtra("data",notesList.get(position));
                    context.startActivity(intent);

                    //Toast.makeText(getContext(), "No audio", Toast.LENGTH_LONG).show();
                    //Toast.makeText(getContext(), currNote.getImagePath().toString(), Toast.LENGTH_LONG).show();

            }
        });

        holder.mycardview.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder( v.getContext() );
                // builder.setTitle( "Edit Employee" );
                LayoutInflater inflater1 = LayoutInflater.from( v.getContext() );

                View v1 = inflater1.inflate( R.layout.change_folder_layout,null );
                builder1.setView( v1 );
                final AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();

              //  initialize recycler view


                final RecyclerView recyclerViewChangeFolder = v1.findViewById( R.id.recyclerChangeFolder );

                sharedPreferences = context.getSharedPreferences( SHARED_PREF, MODE_PRIVATE );

                //sharedPreferences.edit().putString( KEY_NAME, "evneet" ).apply();

                //read from shared preferences

                //String name = sharedPreferences.getString( KEY_NAME,"Rizul" );

                // Log.i( TAG, "onCreate: " + name );

                //names = new ArrayList<>(  );
                try {
                    names = (ArrayList) ObjectSerializer.deserialize( sharedPreferences.getString( KEY_NAME, ObjectSerializer.serialize( new ArrayList<>(  ) ) ) );
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final ChangeCategoriesAdapter categoriesAdapter = new ChangeCategoriesAdapter(context);
                categoriesAdapter.setCategoriesList( names );
                recyclerViewChangeFolder.setAdapter(categoriesAdapter);
                categoriesAdapter.setNoteToChange( currNote );
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerViewChangeFolder.setLayoutManager(layoutManager);
                categoriesAdapter.notifyDataSetChanged();



                Button buttonChangeFolder = v1.findViewById( R.id.button_changefolder );

                buttonChangeFolder.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        alertDialog1.dismiss();



                    }
                } );
                return true;
            }
        } );


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
        TextView desc,date;



        public ViewHolder(@NonNull View itemView) {


            super(itemView);


            mycardview = itemView.findViewById(R.id.newcardNote);
            notes = itemView.findViewById( R.id.textView1 );
            desc = itemView.findViewById(R.id.descTv);
            date = itemView.findViewById(R.id.dateTV);



        }
    }

    public void deleteItem(int position) {

        NotesDB userDatabase = NotesDB.getInstance(getContext());
        notesList = userDatabase.daoObjct().getDefault();
        Notes notes = notesList.get(position);

        userDatabase.daoObjct().delete(notes);
        Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
        notesList.remove(position);
        //notifyItemChanged(position);
        notifyDataSetChanged();

    }
}
