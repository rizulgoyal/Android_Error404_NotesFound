package com.example.android_error404_notesfound.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android_error404_notesfound.Adapters.CategoriesAdapter;
import com.example.android_error404_notesfound.Adapters.NotesAdapter;
import com.example.android_error404_notesfound.ModelClasses.Notes;
import com.example.android_error404_notesfound.R;
import com.example.android_error404_notesfound.RoomDatabase.NotesDB;
import com.example.android_error404_notesfound.SwipeToDeleteCallbackForNotes;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    List<Notes> notesList;

    String currCategory;

    ImageButton addnote, sortNote;

    NotesAdapter notesAdapter;
    EditText search;

    private SearchView searchView;

    @Override
    protected void onResume() {
        super.onResume();
        //notesList.clear();
        loadNotes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView( R.layout.activity_notes );

        search = findViewById(R.id.editSearch );
        search.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter( s.toString() );

            }
        } );







        currCategory = getIntent().getStringExtra( "category" );

        addnote = findViewById( R.id.add_note );
        addnote.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent( getApplicationContext(), AddNoteActivity.class );
                myintent.putExtra( "categoryNote", currCategory );
                startActivity( myintent );
            }
        } );

        sortNote = findViewById( R.id.sort_button );
        sortNote.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {




                AlertDialog.Builder builder1 = new AlertDialog.Builder( v.getContext() );
                // builder.setTitle( "Edit Employee" );
                LayoutInflater inflater1 = LayoutInflater.from( v.getContext() );

                View v1 = inflater1.inflate( R.layout.select_sorttype_layout,null );
                builder1.setView( v1 );
                final AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
                final Spinner spinnerSort = v1.findViewById( R.id.spinnerSortTypes );
                Button buttonChangeSort = v1.findViewById( R.id.button_sortNotes );

                buttonChangeSort.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String sortType = spinnerSort.getSelectedItem().toString();
                        if (sortType.equalsIgnoreCase( "Sort by Date" ))
                        {
                            SortNotes( "date" );

                        }
                        else if (sortType.equalsIgnoreCase( "Sort by Title" ))
                        {
                            SortNotes( "title" );
                        }


                        alertDialog1.dismiss();



                    }
                } );



            }

        } );

        final NotesDB notesDB = NotesDB.getInstance( this );

        notesList = notesDB.daoObjct().getcustomNotesDetails( currCategory );




        RecyclerView recyclerView = findViewById(R.id.recyclerNotes);
        notesAdapter = new NotesAdapter(this);
        notesDB.daoObjct().getLivecustomNotesDetails(currCategory).observe( this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable List<Notes> notelist) {

                notesList = notelist;
                notesAdapter.setNotesList(notesList);
                notesAdapter.notifyDataSetChanged();

            }
        } );
        notesAdapter.setNotesList( notesList );
        recyclerView.setAdapter(notesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        notesAdapter.notifyDataSetChanged();






        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallbackForNotes(notesAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);





    }

    private void SortNotes(final String sortby)
    {

        //Toast.makeText( getApplicationContext(),"asfszdfz",Toast.LENGTH_SHORT ).show();
        Collections.sort( notesList, new Comparator<Notes>() {
            @Override
            public int compare(final Notes o1,final Notes o2) {
                //Toast.makeText( getApplicationContext(),o1.getTitle()+o2.getTitle(),Toast.LENGTH_SHORT ).show();
               if(sortby.equalsIgnoreCase( "date" ))
               {
                   return o1.getDateCreated().compareTo( o2.getDateCreated() );

               }
               else if(sortby.equalsIgnoreCase( "title" ))
               {
                   return o1.getTitle().compareTo( o2.getTitle() );

               }
               return 0;
            }
        } );
        //notesAdapter.setNotesList(notesList);
        notesAdapter.notifyDataSetChanged();
    }

    private void loadNotes()
    {
        final NotesDB notesDB = NotesDB.getInstance( this );

        notesList = notesDB.daoObjct().getcustomNotesDetails( currCategory );




        RecyclerView recyclerView = findViewById(R.id.recyclerNotes);
        notesAdapter = new NotesAdapter(this);
        notesDB.daoObjct().getLivecustomNotesDetails(currCategory).observe( this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable List<Notes> notelist) {


                notesAdapter.setNotesList(notesList);
                notesAdapter.notifyDataSetChanged();

            }
        } );
        notesAdapter.setNotesList( notesList );
        recyclerView.setAdapter(notesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        notesAdapter.notifyDataSetChanged();






        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallbackForNotes(notesAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }

    private void filter(String s) {
        List<Notes> filteredList = new ArrayList<>(  );
        for(Notes notes : notesList)
        {
            if(notes.getTitle().toLowerCase().contains( s.toLowerCase() )
                    || notes.getDescription().toLowerCase().contains( s.toLowerCase() )

            )
            {
                filteredList.add( notes );
            }
        }
        notesAdapter.filterList(filteredList);




    }


}
