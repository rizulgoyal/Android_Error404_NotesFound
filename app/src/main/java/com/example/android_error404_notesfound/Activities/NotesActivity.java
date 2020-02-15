package com.example.android_error404_notesfound.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.android_error404_notesfound.Adapters.CategoriesAdapter;
import com.example.android_error404_notesfound.R;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notes );

        RecyclerView recyclerView = findViewById(R.id.recyclerCategories);
        final CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this);
        categoriesAdapter.setCategoriesList( names );
        recyclerView.setAdapter(categoriesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        categoriesAdapter.notifyDataSetChanged();


    }
}
