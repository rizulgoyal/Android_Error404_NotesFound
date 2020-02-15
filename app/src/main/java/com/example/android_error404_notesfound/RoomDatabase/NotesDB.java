package com.example.android_error404_notesfound.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android_error404_notesfound.ModelClasses.Notes;

@Database(entities = Notes.class , exportSchema = false , version = 2)

public abstract class NotesDB extends RoomDatabase {


        public static final String DB_NAME = "user_db";

        private static NotesDB uInstance;


        public static NotesDB getInstance(Context context)
        {
            if(uInstance == null)
            {
                uInstance = Room.databaseBuilder(context.getApplicationContext(), NotesDB.class, NotesDB.DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
            }
            return uInstance;
        }


        public abstract NotesDao daoObjct();
    }

