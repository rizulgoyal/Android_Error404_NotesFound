package com.example.android_error404_notesfound.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.icu.text.Edits;
import android.icu.text.UnicodeSetSpanner;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_error404_notesfound.ModelClasses.Notes;
import com.example.android_error404_notesfound.R;
import com.example.android_error404_notesfound.RoomDatabase.NotesDB;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteDetail extends AppCompatActivity {


    Notes notes;
    ImageButton showMap;

    Button play;
    ImageButton playBtn,removeAudio,delete;
    ImageView imageView;
    EditText title,desc;
    SeekBar seekBar;

    MediaPlayer mediaPlayer;
    String audioPath,imagePath;
    String fileName;


    CardView playerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_note_detail);

        notes =  getIntent().getParcelableExtra("data");

        showMap = findViewById( R.id.mapButton );
        showMap.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent( getApplicationContext(), MapActivity.class );
                myintent.putExtra( "mapdata", notes );
                startActivity( myintent );

            }
        } );

        playBtn = findViewById(R.id.play);
        imageView = findViewById(R.id.imageView);

        title = findViewById(R.id.titleNote);
        desc = findViewById(R.id.descriptionNote);
        //play.setVisibility(View.GONE);
        playerView = findViewById(R.id.playerView);
        removeAudio = findViewById(R.id.removeAudioBtn);
        seekBar = findViewById(R.id.seekBar);

        if(notes.getAudioPath()!=null)
        {
            playerView.setVisibility(View.VISIBLE);
            audioPath = notes.getAudioPath();
        }

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mediaPlayer = new MediaPlayer();
                try {
                    //Log.d("player",notes.getAudioPath());
                    Toast.makeText(getApplicationContext(),notes.getAudioPath(), Toast.LENGTH_SHORT).show();
                    mediaPlayer.setDataSource(notes.getAudioPath());
                    mediaPlayer.prepare();
                    int duration = mediaPlayer.getDuration();
                    seekBar.setMax(duration/1000);
                    mediaPlayer.start();
                    final Handler mHandler = new Handler();
//Make sure you update Seekbar on UI thread
                    NoteDetail.this.runOnUiThread(new Runnable() {

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
                    //Log.d("player",notes.getAudioPath());
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse(notes.getImagePath()));
                    startActivity(intent);
            }
        });

        if(notes!=null)
        {

            title.setText(notes.getTitle());
            desc.setText(notes.getDescription());

            if(notes.getImagePath()!=null)
            {
                try {
                    imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(notes.getImagePath())));
                    imagePath = notes.getImagePath();
                }catch (IOException ioe)
                {

                }

            }

        }

        removeAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerView.setVisibility(View.GONE);
                audioPath = null;
                notes.setAudioPath(audioPath);
            }
        });


        delete = findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titlestr = title.getText().toString();

                //Toast.makeText(getApplicationContext(),titlestr,Toast.LENGTH_SHORT).show();
                String descstr = desc.getText().toString();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "YYY MM dd hh:mm:ss" );
                fileName = simpleDateFormat.format(calendar.getTime());

                notes.setDateModified(fileName);
                notes.setTitle(titlestr);
                notes.setDescription(descstr);
                notes.setAudioPath(audioPath);
                notes.setImagePath(imagePath);
                NotesDB notesDB = NotesDB.getInstance(getApplicationContext());
                notesDB.daoObjct().update(notes);
                finish();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String titlestr = title.getText().toString();
        String descstr = desc.getText().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "YYY MM dd hh:mm:ss" );
        fileName = simpleDateFormat.format(calendar.getTime());

        notes.setDateModified(fileName);
        notes.setTitle(titlestr);
        notes.setDescription(descstr);
        notes.setAudioPath(audioPath);
        notes.setImagePath(imagePath);
        NotesDB notesDB = NotesDB.getInstance(this);
        notesDB.daoObjct().update(notes);
        Toast.makeText(this,"done",Toast.LENGTH_SHORT).show();
    }
}
