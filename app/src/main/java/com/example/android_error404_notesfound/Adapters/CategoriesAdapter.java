package com.example.android_error404_notesfound.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.android_error404_notesfound.R;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder>{

   List<String> categoriesList;
   Context context;

    public CategoriesAdapter(Context context) {
        this.context = context;
    }

    public List<String> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(List<String> categoriesList) {
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
//        Notes userDatabase = PlacesDB.getInstance(getContext());
//        userDatabase.daoObjct().delete(places);
//        Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
//        placesList.remove(position);
        notifyDataSetChanged();

    }
}
