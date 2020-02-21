package com.example.android_error404_notesfound.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_error404_notesfound.Adapters.NotesAdapter;
import com.example.android_error404_notesfound.ModelClasses.Notes;
import com.example.android_error404_notesfound.R;
import com.example.android_error404_notesfound.RoomDatabase.NotesDB;
import com.example.android_error404_notesfound.SwipeToDeleteCallbackForNotes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {

    EditText title, desc;
    ImageButton buttonaddnote;

    //audio recording and playing
    private ImageButton play, record,addPic,removeAudio,removeImg;
    //Button stop;
    private MediaRecorder myAudioRecorder;
    private String outputFile,fileName,noteDate;
    private String audioPath = null;
    private ImageView imageView;
   Uri imagePath;
    SeekBar seekBar;

    MediaPlayer mediaPlayer;
    LinearLayout imagevieww;

    private final int REQUEST_CODE = 1;

    Double latitude, longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    String currCategory;

    CardView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getSupportActionBar().setTitle("Add New Note");
        setContentView( R.layout.activity_add_note );
        requestPermission();

        currCategory = getIntent().getStringExtra( "categoryNote" );

        title = findViewById( R.id.titleNote );
        desc = findViewById( R.id.descriptionNote );
        buttonaddnote = findViewById( R.id.delete );
        play = findViewById(R.id.play);
        //stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        addPic = findViewById(R.id.addPic);
        removeAudio = findViewById(R.id.removeAudioBtn);

         seekBar = findViewById(R.id.seekBar);

        imageView = findViewById(R.id.imageView);
        addPic.setOnClickListener(this);

        play.setOnClickListener(this);
        //stop.setOnClickListener(this);
        record.setOnClickListener(this);

        removeImg = findViewById(R.id.removeImageBtn);
        imagevieww = findViewById(R.id.imageVieww);


        removeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagevieww.setVisibility(View.GONE);
                imagePath = null;
            }
        });




        //stop.setEnabled(false);
        play.setEnabled(false);


        playerView = findViewById(R.id.playerView);
        playerView.setVisibility(View.GONE);

        removeAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerView.setVisibility(View.GONE);
                audioPath = null;
            }
        });



        //seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });



        ///setupRecorder();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "YYY MM dd hh:mm:ss" );
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat( "YYY/MM/dd hh:mm" );
        fileName = simpleDateFormat.format(calendar.getTime());
        noteDate = simpleDateFormat1.format(calendar.getTime());
        //noteDate = fileName;
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
                    if(imagePath!=null)
                    {
                        notes.setImagePath(imagePath.toString());
                    }



                NotesDB notesDB = NotesDB.getInstance( v.getContext() );

                notesDB.daoObjct().insert( notes );


                finish();

                //loadNotes();






            }
        } );



    }
    private void loadNotes()
    {
        final NotesDB notesDB = NotesDB.getInstance( this );

        final List<Notes> notesList = notesDB.daoObjct().getcustomNotesDetails( currCategory );




        RecyclerView recyclerView = findViewById(R.id.recyclerNotes);
        final NotesAdapter notesAdapter = new NotesAdapter( this );
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

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallbackForNotes(notesAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


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
                //stop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();


                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
                View sheetView = this.getLayoutInflater().inflate(R.layout.stop_recording, null);
                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.show();
                 Button stop1 = sheetView.findViewById(R.id.stop);
                stop1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(myAudioRecorder != null) {
                            //myAudioRecorder.pause();
                            myAudioRecorder.stop();
                            //myAudioRecorder.reset();
                            myAudioRecorder.release();
                            myAudioRecorder = null;
                            record.setEnabled(true);
                            //stop.setEnabled(false);
                            play.setEnabled(true);
                            Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
                            audioPath = outputFile;
                            mBottomSheetDialog.dismiss();
                            playerView.setVisibility(View.VISIBLE);

                        }

                    }
                });
                break;


//            case R.id.stop:
//                if(myAudioRecorder != null) {
//                    //myAudioRecorder.pause();
//                    myAudioRecorder.stop();
//                    //myAudioRecorder.reset();
//                    myAudioRecorder.release();
//                    myAudioRecorder = null;
//                    record.setEnabled(true);
//                    //stop.setEnabled(false);
//                    play.setEnabled(true);
//                    Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
//                    audioPath = outputFile;
//
//                }
//                break;

            case R.id.play:

                mediaPlayer = new MediaPlayer();
                try {
                    Log.d("player",outputFile);
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    int duration = mediaPlayer.getDuration();
                    seekBar.setMax(duration/1000);
                    mediaPlayer.start();
                    final Handler mHandler = new Handler();
//Make sure you update Seekbar on UI thread
                    AddNoteActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(mediaPlayer != null){
                                int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                                seekBar.setProgress(mCurrentPosition);
                            }
                            mHandler.postDelayed(this, 1000);
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // make something
                }
                break;

            case R.id.addPic:

                selectImage(this);


        }

    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                        imagePath = getImageUri(this,selectedImage);
                        imagevieww.setVisibility(View.VISIBLE);

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                imagePath = getImageUri(this,BitmapFactory.decodeFile(picturePath));
                                imagevieww.setVisibility(View.VISIBLE);
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
    public Uri getImageUri(Context ctx, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage
                (ctx.getContentResolver(),
                        bitmap, "Temp", null);
        return Uri.parse(path);
    }
}
