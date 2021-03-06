package com.example.android_error404_notesfound.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
    Boolean checkCategory = false;

    ImageButton addCategory;
    SharedPreferences sharedPreferences;
    private final int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestPermission();
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().setTitle("Notes Found");
        getSupportActionBar().setSubtitle("Folders");
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

                        checkCategory = false;

                        for(String category : names)
                        {
                            if(category.equalsIgnoreCase( textCategory.getText().toString() ))
                            {

                                androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder( v.getContext() );
                                builder1.setMessage( "Already added this category" );
                                builder1.setCancelable( true );


                                builder1.setNegativeButton(
                                        "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                                // alert11.dismiss();

                                            }
                                        } );

                                final androidx.appcompat.app.AlertDialog alert11 = builder1.create();


                                alert11.show();
                                checkCategory = true;

                            }
                        }

                        if(!checkCategory) {
                            names.add( textCategory.getText().toString() );
                            sharedPreferences.edit().clear();
                            try {
                                sharedPreferences.edit().putString( KEY_NAME, ObjectSerializer.serialize( names ) ).apply();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            names.clear();

                            try {
                                names = (ArrayList) ObjectSerializer.deserialize( sharedPreferences.getString( KEY_NAME, ObjectSerializer.serialize( new ArrayList<>() ) ) );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            RecyclerView recyclerView = findViewById( R.id.recyclerCategories );
                            final CategoriesAdapter categoriesAdapter = new CategoriesAdapter( v.getContext() );
                            categoriesAdapter.setCategoriesList( names );
                            recyclerView.setAdapter( categoriesAdapter );
                            categoriesAdapter.notifyDataSetChanged();

                            alertDialog.dismiss();

                        }

                    }
                } );

            }
        } );




    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void setrecagain() {

        names.clear();

        try {
            names = (ArrayList) ObjectSerializer.deserialize( sharedPreferences.getString( KEY_NAME, ObjectSerializer.serialize( new ArrayList<>(  ) ) ) );
        } catch (IOException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerCategories);
        final CategoriesAdapter categoriesAdapter = new CategoriesAdapter(MainActivity.this);
        categoriesAdapter.setCategoriesList( names );
        recyclerView.setAdapter(categoriesAdapter);
        categoriesAdapter.notifyDataSetChanged();


    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE );

    }
}
