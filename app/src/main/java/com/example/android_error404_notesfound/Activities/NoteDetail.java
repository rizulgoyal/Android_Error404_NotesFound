package com.example.android_error404_notesfound.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_error404_notesfound.ModelClasses.Notes;
import com.example.android_error404_notesfound.R;

import java.io.IOException;

public class NoteDetail extends AppCompatActivity {


    Notes notes;
    ImageButton showMap;

    Button play;
    ImageView imageView;
    TextView title,desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_detail);

        notes =  getIntent().getParcelableExtra("data");

        showMap = findViewById( R.id.mapButtonDetail );
        showMap.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent( getApplicationContext(), MapActivity.class );
                myintent.putExtra( "mapdata", notes );
                startActivity( myintent );

            }
        } );

        play = findViewById(R.id.play);
        imageView = findViewById(R.id.imageView);

        title = findViewById(R.id.titleNote);
        desc = findViewById(R.id.descriptionNote);
        play.setVisibility(View.GONE);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    //Log.d("player",notes.getAudioPath());
                    Toast.makeText(getApplicationContext(),notes.getAudioPath(), Toast.LENGTH_SHORT).show();
                    mediaPlayer.setDataSource(notes.getAudioPath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    //Log.d("player",notes.getAudioPath());
                }
            }
        });


        if(notes!=null)
        {

            title.setText(notes.getTitle());
            desc.setText(notes.getDescription());
            if(notes.getAudioPath()!=null)
            {
                play.setVisibility(View.VISIBLE);
            }
            else
            {
                play.setVisibility( View.GONE );
            }
            if(notes.getImagePath()!=null)
            {
                try {
                    imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(notes.getImagePath())));
                }catch (IOException ioe)
                {

                }

            }

        }




    }
}
