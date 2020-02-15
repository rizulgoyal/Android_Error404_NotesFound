package com.example.android_error404_notesfound.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;

import com.example.android_error404_notesfound.Adapters.CategoriesAdapter;
import com.example.android_error404_notesfound.Adapters.NotesAdapter;
import com.example.android_error404_notesfound.ModelClasses.Notes;
import com.example.android_error404_notesfound.R;
import com.example.android_error404_notesfound.RoomDatabase.NotesDB;

import java.util.List;

public class NotesActivity extends AppCompatActivity {

    List<Notes> notesList;

    String currCategory;

    ImageButton addnote;

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notes );

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

        loadNotes();



    }

    private void loadNotes()
    {
        final NotesDB notesDB = NotesDB.getInstance( this );

        notesList = notesDB.daoObjct().getcustomNotesDetails( currCategory );




        RecyclerView recyclerView = findViewById(R.id.recyclerNotes);
        final NotesAdapter notesAdapter = new NotesAdapter(this);
        notesAdapter.setNotesList( notesList );
        recyclerView.setAdapter(notesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        notesAdapter.notifyDataSetChanged();

        notesDB.daoObjct().getLivecustomNotesDetails(currCategory).observe( this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable List<Notes> notelist) {


                notesAdapter.setNotesList(notesList);
                notesAdapter.notifyDataSetChanged();

            }
        } );

    }
}
