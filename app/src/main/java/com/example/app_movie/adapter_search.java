package com.example.app_movie;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class adapter_search extends RecyclerView.Adapter<adapter_search.ViewHolder> {
    private ArrayList<FilmClass> films;
    private Context context;

    public adapter_search(ArrayList<FilmClass> films, Context context) {
        this.films = films;
        this.context = context;
    }
    public void clear(){
        films.clear();
        notifyDataSetChanged();
    }
    public void addlist(ArrayList<FilmClass> films){
        this.films=films;
    }
    @NonNull
    @Override
    public adapter_search.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        return new adapter_search.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_search.ViewHolder holder, int position) {
       FilmClass film_object= films.get(position);
        Glide.with(context).load(film_object.getUrl_img()).into(holder.imageView);
        holder.textView.setText(film_object.getName());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,detail.class);
                intent.putExtra("infor", film_object);
                context.startActivity(intent);
            }
        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,play_film.class);
                intent.putExtra("name", film_object.getName());
                intent.putExtra("url",film_object.getUrlfilm());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    public class ViewHolder  extends  RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        TextView textView;
        ImageView imageView;
        ImageButton imageButton;
        public ViewHolder(@NonNull View view) {
            super(view);
            constraintLayout=view.findViewById(R.id.seacrh_layout);
            textView=view.findViewById(R.id.titile_serch);
            imageView=view.findViewById(R.id.imageView_search);
            imageButton=view.findViewById(R.id.imgButton_play_search);
        }
    }
}
