package com.example.android_error404_notesfound.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.Edits;
import android.icu.text.UnicodeSetSpanner;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NoteDetail extends AppCompatActivity implements View.OnClickListener {


    Notes notes;
    ImageButton showMap;
    String outputFile;
    Uri imagePathUri;

    Button play;
    ImageButton playBtn,removeAudio,delete,removeImg,record,camera;
    ImageView imageView;
    EditText title,desc;
    TextView datec,datem;
    SeekBar seekBar;

    MediaPlayer mediaPlayer;
    String audioPath,imagePath;
    String fileName;

    LinearLayout imagevieww;


    CardView playerView;

    MediaRecorder myAudioRecorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title

        setContentView(R.layout.activity_note_detail);

        notes =  getIntent().getParcelableExtra("data");
        getSupportActionBar().setTitle(notes.getTitle());

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

        datec = findViewById(R.id.dateC);
        datem = findViewById(R.id.dateM);
        imagevieww = findViewById(R.id.imagev);
        removeImg  = findViewById(R.id.removeImageBtn);
        camera = findViewById(R.id.addPic);
        record = findViewById(R.id.record);



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
                    //Toast.makeText(getApplicationContext(),notes.getAudioPath(), Toast.LENGTH_SHORT).show();
                    if(notes.getAudioPath()!=null) {
                        mediaPlayer.setDataSource(notes.getAudioPath());
                    }
                    else
                    {
                        mediaPlayer.setDataSource(outputFile);
                    }
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
                    //Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
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
            datec.setText("Date Created : " +notes.getDateCreated());
            if(notes.getDateModified()!=null)
            {
                datem.setText("Date Modified: " +notes.getDateModified());
            }
            else
            {
                datem.setVisibility(View.GONE);
            }

            if(notes.getImagePath()!=null)
            {
                try {
                    imagevieww.setVisibility(View.VISIBLE);
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

        removeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagevieww.setVisibility(View.GONE);
                imagePath = null;
                notes.setImagePath(imagePath);
            }
        });


        delete = findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String titlestr = title.getText().toString();
//
//                //Toast.makeText(getApplicationContext(),titlestr,Toast.LENGTH_SHORT).show();
//                String descstr = desc.getText().toString();
//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "YYY MM dd hh:mm:ss" );
//                fileName = simpleDateFormat.format(calendar.getTime());
//
//                notes.setDateModified(fileName);
//                notes.setTitle(titlestr);
//                notes.setDescription(descstr);
//                notes.setAudioPath(audioPath);
//                notes.setImagePath(imagePath);
//                NotesDB notesDB = NotesDB.getInstance(getApplicationContext());
//                notesDB.daoObjct().update(notes);
                NotesDB userDatabase = NotesDB.getInstance(v.getContext());

                userDatabase.daoObjct().delete(notes);
                //Toast.makeText(v.getContext(),"Deleted",Toast.LENGTH_SHORT).show();

                //notifyItemChanged(position);
                //RecyclerView recyclerView = findViewById(R.id.recyclerNotes);
                NotesAdapter notesAdapter = new NotesAdapter(v.getContext());

                notesAdapter.notifyDataSetChanged();


                finish();
            }
        });


        record.setOnClickListener(this);
        camera.setOnClickListener(this);



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
        //Toast.makeText(this,"done",Toast.LENGTH_SHORT).show();
    }

    private void setupRecorder()
    {
        //fileName = title.toString() + desc.toString();
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        //getOutputFile(fileName);
        myAudioRecorder.setOutputFile(notes.getAudioPath());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.record:

                if(notes.getAudioPath()!= null) {
                    setupRecorder();
                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    } catch (IllegalStateException ise) {
                        // make something ...
                        Log.d("mdiaplayer", ise.toString());
                    } catch (IOException ioe) {
                        // make something
                        Log.d("mdiaplayer", ioe.toString());
                    }
                    record.setEnabled(false);
                    //stop.setEnabled(true);
                    //Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();


                    final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
                    View sheetView = this.getLayoutInflater().inflate(R.layout.stop_recording, null);
                    mBottomSheetDialog.setContentView(sheetView);
                    mBottomSheetDialog.show();
                    Button stop1 = sheetView.findViewById(R.id.stop);
                    stop1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (myAudioRecorder != null) {
                                //myAudioRecorder.pause();
                                myAudioRecorder.stop();
                                //myAudioRecorder.reset();
                                myAudioRecorder.release();
                                myAudioRecorder = null;
                                record.setEnabled(true);
                                //stop.setEnabled(false);
                                //play.setEnabled(true);
                                //Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
                                audioPath = notes.getAudioPath();
                                mBottomSheetDialog.dismiss();
                                playerView.setVisibility(View.VISIBLE);

                            }

                        }
                    });
                }
                else
                {
                    setupNewRecorder();
                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    } catch (IllegalStateException ise) {
                        // make something ...
                        Log.d("mdiaplayer", ise.toString());
                    } catch (IOException ioe) {
                        // make something
                        Log.d("mdiaplayer", ioe.toString());
                    }
                    record.setEnabled(false);
                    //stop.setEnabled(true);
                    //Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();


                    final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
                    View sheetView = this.getLayoutInflater().inflate(R.layout.stop_recording, null);
                    mBottomSheetDialog.setContentView(sheetView);
                    mBottomSheetDialog.show();
                    Button stop1 = sheetView.findViewById(R.id.stop);
                    stop1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (myAudioRecorder != null) {
                                //myAudioRecorder.pause();
                                myAudioRecorder.stop();
                                //myAudioRecorder.reset();
                                myAudioRecorder.release();
                                myAudioRecorder = null;
                                record.setEnabled(true);
                                //stop.setEnabled(false);
                                //play.setEnabled(true);
                                //Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
                                audioPath = outputFile;
                                mBottomSheetDialog.dismiss();
                                playerView.setVisibility(View.VISIBLE);

                            }

                        }
                    });
                }
                break;

            case R.id.addPic:

                    selectImage(this);

                break;

        }
    }

    private void setupNewRecorder()
    {
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+notes.getDateCreated().replaceAll(":","").replaceAll("/"," ")+".3gp";
        Log.d("path",outputFile);
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        //getOutputFile(fileName);
        myAudioRecorder.setOutputFile(outputFile);

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
                        imagePathUri = getImageUri(this,selectedImage);
                        imagePath = imagePathUri.toString();
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
                                imagePathUri = getImageUri(this,BitmapFactory.decodeFile(picturePath));
                                imagePath = imagePathUri.toString();
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
