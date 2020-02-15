package com.example.android_error404_notesfound.RoomDatabase;

import androidx.annotation.StyleableRes;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android_error404_notesfound.ModelClasses.Notes;

import java.util.List;

@Dao
public interface NotesDao {



        @Insert
        void insert(Notes notes);

        @Delete
        void delete(Notes notes);

        @Update
        void update(Notes notes);

        @Query("Select count(id) from notes")
        Integer count();

        @Query("Select * from notes")
        LiveData<List<Notes>> getUserDetails();

        @Query("Select * from notes")
        List<Notes> getDefault();

        @Query("Select * from notes where category = :category")
        List<Notes> getcustomNotesDetails(String category);

       @Query("Select * from notes where category = :category")
        LiveData<List<Notes>> getLivecustomNotesDetails(String category);
    }

