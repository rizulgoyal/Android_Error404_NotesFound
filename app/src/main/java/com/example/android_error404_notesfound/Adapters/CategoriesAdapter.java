package com.example.android_error404_notesfound.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_error404_notesfound.Activities.NotesActivity;
import com.example.android_error404_notesfound.ModelClasses.Notes;
import com.example.android_error404_notesfound.ModelClasses.ObjectSerializer;
import com.example.android_error404_notesfound.R;


import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

   ArrayList<String> categoriesList;
   Context context;

    private static final String SHARED_PREF = "categories";

    private static final String KEY_NAME = "key";

    SharedPreferences sharedPreferences;



    public CategoriesAdapter(Context context) {
        this.context = context;
    }

    public List<String> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(ArrayList<String> categoriesList) {
        this.categoriesList = categoriesList;
    }



    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.notes_categories_cell_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final String currCategory = categoriesList.get(position);



        holder.categories.setText(currCategory);

        holder.mycardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myintent = new Intent(context, NotesActivity.class);
                myintent.putExtra("category", currCategory);

                context.startActivity(myintent);
                //  Toast.makeText(context,"position = "+position,Toast.LENGTH_LONG).show();

            }
        });





    }




    @Override
    public int getItemCount() {
        return categoriesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView categories;
        CardView mycardview;


        public ViewHolder(@NonNull View itemView) {


            super(itemView);


            mycardview = itemView.findViewById(R.id.newcard);
            categories = itemView.findViewById( R.id.textView1 );



        }
    }

    public void deleteItem(int position) {



        String category = categoriesList.get(position);
        categoriesList.remove( category );

        sharedPreferences = context.getSharedPreferences( SHARED_PREF, MODE_PRIVATE );

        try {
            sharedPreferences.edit().putString( KEY_NAME, ObjectSerializer.serialize( categoriesList ) ).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getContext(),"Category Deleted",Toast.LENGTH_SHORT).show();
        notifyItemChanged(position);
        notifyDataSetChanged();
        notifyItemChanged( position );

    }
}
