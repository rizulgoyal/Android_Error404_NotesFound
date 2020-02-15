package com.example.android_error404_notesfound.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android_error404_notesfound.ModelClasses.Notes;
import com.example.android_error404_notesfound.R;
import com.example.android_error404_notesfound.RoomDatabase.NotesDB;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddNoteActivity extends AppCompatActivity {

    TextView title, desc;
    Button buttonaddnote;

    private final int REQUEST_CODE = 1;

    Double latitude, longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    String currCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_note );

        currCategory = getIntent().getStringExtra( "categoryNote" );

        title = findViewById( R.id.titleNote );
        desc = findViewById( R.id.descriptionNote );
        buttonaddnote = findViewById( R.id.button_save_note );


        getUserLocation();

        if(!checkPermission())
        {
            requestPermission();
        }
        else
        {
            fusedLocationProviderClient.requestLocationUpdates( locationRequest, locationCallback, Looper.myLooper() );

        }

        buttonaddnote.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "YYY MM dd hh:mm:ss" );
                String noteDate = simpleDateFormat.format( calendar.getTime() );

                String titleString = title.getText().toString();
                String descString = desc.getText().toString();

                Notes notes = new Notes( latitude, longitude, noteDate, titleString, descString , currCategory);

                NotesDB notesDB = NotesDB.getInstance( v.getContext() );

                notesDB.daoObjct().insert( notes );

                finish();





            }
        } );



    }

    private Boolean checkPermission()
    {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION );
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE );

    }

    private void getUserLocation()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( this );
        locationRequest = new LocationRequest();
        locationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
        locationRequest.setInterval( 5000 );
        locationRequest.setFastestInterval( 3000 );
        locationRequest.setSmallestDisplacement( 10 );

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    //new
                    LatLng userLocation = new LatLng( location.getLatitude(), location.getLongitude() );
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        };
                    //setFavouriteMarkers();


    }
}
