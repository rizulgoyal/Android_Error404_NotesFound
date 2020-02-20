package com.example.android_error404_notesfound.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android_error404_notesfound.Adapters.CategoriesAdapter;
import com.example.android_error404_notesfound.ModelClasses.ObjectSerializer;
import com.example.android_error404_notesfound.R;
import com.example.android_error404_notesfound.SwipeToDeleteCallbackForCategories;
import com.example.android_error404_notesfound.SwipeToDeleteCallbackForNotes;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String SHARED_PREF = "categories";

    private static final String KEY_NAME = "key";

    ArrayList<String> names;

    ImageButton addCategory;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        sharedPreferences = this.getSharedPreferences( SHARED_PREF, MODE_PRIVATE );

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
        if (names.isEmpty())
        {
            names = new ArrayList<>( Arrays.asList( "Home", "Work", "College" ) );
//        sharedPreferences.edit().putStringSet( "array", new HashSet<String>( names ) ).apply();
//
//
//        Set<String> namenew = sharedPreferences.getStringSet( "array",new HashSet<String>(  ) );
//
//        Log.i( TAG, "onCreate: " + namenew.toString() );

            try {
                sharedPreferences.edit().putString( KEY_NAME, ObjectSerializer.serialize( names ) ).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        RecyclerView recyclerView = findViewById(R.id.recyclerCategories);
        final CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this);
        categoriesAdapter.setCategoriesList( names );
        recyclerView.setAdapter(categoriesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        categoriesAdapter.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallbackForCategories(categoriesAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);






//        ItemTouchHelper itemTouchHelper = new
//                ItemTouchHelper(new SwipeToDeleteCallbackForPlaces(placesAdapter));
//        itemTouchHelper.attachToRecyclerView(recyclerView);


        addCategory = findViewById( R.id.add_category );
        addCategory.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v1) {

                AlertDialog.Builder builder = new AlertDialog.Builder( v1.getContext() );
                // builder.setTitle( "Edit Employee" );
                LayoutInflater inflater = LayoutInflater.from( v1.getContext() );
                View v = inflater.inflate( R.layout.add_category_layout,null );
                builder.setView( v );
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                final EditText textCategory = v.findViewById( R.id.text_add_category );
                Button buttonAdd = v.findViewById( R.id.button_add_category );

                buttonAdd.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        names.add( textCategory.getText().toString() );

                        try {
                            sharedPreferences.edit().putString(KEY_NAME, ObjectSerializer.serialize( names ) ).apply();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }


                        RecyclerView recyclerView = findViewById(R.id.recyclerCategories);
                        final CategoriesAdapter categoriesAdapter = new CategoriesAdapter(v.getContext());
                        categoriesAdapter.setCategoriesList( names );
                        recyclerView.setAdapter(categoriesAdapter);
                        categoriesAdapter.notifyDataSetChanged();

                        alertDialog.dismiss();



                    }
                } );

            }
        } );




    }
}
