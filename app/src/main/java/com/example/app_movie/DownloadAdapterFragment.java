package com.example.app_movie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class DownloadAdapterFragment extends RecyclerView.Adapter<DownloadAdapterFragment.DownloadHolder> {

    private ArrayList<FilmClass> filmClassArrayList;
    private Context context;
    private onClick onClickGetDetail;
    public DownloadAdapterFragment(ArrayList<FilmClass> filmClassArrayList,Context context,onClick onClickGetDetail){
        this.filmClassArrayList=filmClassArrayList;
        this.context=context;
        this.onClickGetDetail=onClickGetDetail;
    }
    @NonNull
    @Override
    public DownloadAdapterFragment.DownloadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_download,parent,false);
        return new DownloadAdapterFragment.DownloadHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadHolder holder, int position) {
        FilmClass film=filmClassArrayList.get(position);
        Bitmap bitmap= BitmapFactory.decodeFile(film.getUrl_img());
        holder.imgView_ItemDownload.setImageBitmap(bitmap);
        holder.tvPlot.setText(film.getDescription());
        holder.tvTitle.setText(film.name);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        holder.rcGenre_itemDownload.setLayoutManager(linearLayoutManager);
        holder.genreAdapter=new GenreAdapter(film.getGenre(),context);
        holder.rcGenre_itemDownload.setAdapter(holder.genreAdapter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGetDetail.onClickList(film);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmClassArrayList.size();
    }

    class DownloadHolder extends RecyclerView.ViewHolder{
        ImageView imgView_ItemDownload;
        TextView tvTitle,tvPlot;
        RecyclerView rcGenre_itemDownload;
        GenreAdapter genreAdapter;
        public DownloadHolder(@NonNull View itemView) {
            super(itemView);
            imgView_ItemDownload =itemView.findViewById(R.id.imgView_itemDownload);
            tvTitle=itemView.findViewById(R.id.tvTitle_itemDownload);
            tvPlot=itemView.findViewById(R.id.tvPlot_itemDownload);
            rcGenre_itemDownload=itemView.findViewById(R.id.rvGenre_itemDownload);

        }
    }
    public interface onClick{
        public void onClickList(FilmClass filmClass);
    }
}
