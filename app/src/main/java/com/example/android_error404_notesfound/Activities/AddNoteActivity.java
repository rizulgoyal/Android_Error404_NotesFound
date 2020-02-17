package com.example.android_error404_notesfound.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {

    TextView title, desc;
    Button buttonaddnote;

    //audio recording and playing
    private Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile,fileName,noteDate;
    private String audioPath = null;

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
        requestPermission();

        currCategory = getIntent().getStringExtra( "categoryNote" );

        title = findViewById( R.id.titleNote );
        desc = findViewById( R.id.descriptionNote );
        buttonaddnote = findViewById( R.id.button_save_note );
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        record.setOnClickListener(this);

        stop.setEnabled(false);
        play.setEnabled(false);

        ///setupRecorder();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "YYY MM dd hh:mm:ss" );
        fileName = simpleDateFormat.format(calendar.getTime());
        noteDate = fileName;
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+fileName.replaceAll(":","")+".3gp";
        Log.d("path",outputFile);
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        //getOutputFile(fileName);
        myAudioRecorder.setOutputFile(outputFile);



        getUserLocation();
        //setupRecorder();


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

//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "YYY MM dd hh:mm:ss" );
//                String noteDate = simpleDateFormat.format( calendar.getTime() );

                String titleString = title.getText().toString();
                String descString = desc.getText().toString();

                Notes notes = new Notes( latitude, longitude, noteDate, titleString, descString , currCategory);

                    notes.setAudioPath(audioPath);


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
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE );

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

    private String getOutputFile(String name)
    {
        if(name!=null) {
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + name + "/jjj.3gp";
        }
        return outputFile;
    }

    private void setupRecorder()
    {
        fileName = title.toString() + desc.toString();
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        getOutputFile(fileName);
        myAudioRecorder.setOutputFile(outputFile);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.record:
                //setupRecorder();
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                    Log.d("mdiaplayer",ise.toString());
                } catch (IOException ioe) {
                    // make something
                    Log.d("mdiaplayer",ioe.toString());
                }
                record.setEnabled(false);
                stop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                break;


            case R.id.stop:
                if(myAudioRecorder != null) {
                    //myAudioRecorder.pause();
                    myAudioRecorder.stop();
                    //myAudioRecorder.reset();
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                    record.setEnabled(true);
                    stop.setEnabled(false);
                    play.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
                    audioPath = outputFile;

                }
                break;

            case R.id.play:

                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    Log.d("player",outputFile);
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // make something
                }
                break;


        }

    }
}
