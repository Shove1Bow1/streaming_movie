package com.example.app_movie;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class film_adapter extends RecyclerView.Adapter<film_adapter.ViewHolder> {
    private ArrayList<FilmClass> films;
    private Context context;

    public film_adapter(ArrayList<FilmClass> films, Context context) {
        this.films = films;
        this.context = context;

    }

    @NonNull
    @Override
    public film_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.iteam_film,parent,false);
        return new film_adapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull film_adapter.ViewHolder holder, int position) {
        FilmClass film_class =films.get(position);
        Log.e("run",""+ film_class.getUrl_img());
        Glide.with(context).load(film_class.getUrl_img()).into(holder.imgview);
        holder.textView.setText(film_class.getName());
        holder.imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,detail.class);
                intent.putExtra("infor", film_class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return films.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgview;
        TextView textView;
        public ViewHolder(@NonNull View view){
            super(view);
           imgview=view.findViewById(R.id.view_film);
           textView=view.findViewById(R.id.txtTitle_itemFilm);
        }
    }
}
