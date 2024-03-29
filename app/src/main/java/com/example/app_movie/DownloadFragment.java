package com.example.app_movie;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;

public class DownloadFragment extends Fragment implements DownloadAdapterFragment.onClick {
    RecyclerView rcDownload;
    DownloadAdapterFragment downloadAdapterFragment;
    ArrayList<FilmClass> filmClassArrayList;
    DBStorageDMovie sqLiteDatabase;
    SwipeRefreshLayout pullToRefreshDownload;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcDownload=view.findViewById(R.id.rcDownload);
        filmClassArrayList=new ArrayList<FilmClass>();
        sqLiteDatabase=new DBStorageDMovie(getContext());
        filmClassArrayList=sqLiteDatabase.getAllMovieList();
        downloadAdapterFragment=new DownloadAdapterFragment(filmClassArrayList,getContext(),this);
        LinearLayoutManager linearLayout=new LinearLayoutManager(getContext());
        linearLayout.setOrientation(RecyclerView.VERTICAL);
        rcDownload.setLayoutManager(linearLayout);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rcDownload);
        rcDownload.setAdapter(downloadAdapterFragment);
        pullToRefreshDownload=view.findViewById(R.id.pullToRefreshDownload);
        pullToRefreshDownload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadAdapterFragment.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onClickList(FilmClass filmClass) {
        Intent intent=new Intent(getActivity(),detail.class);
        intent.putExtra("infor",filmClass);
        startActivity(intent);
    }
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int i=viewHolder.getAdapterPosition();
            FilmClass film=filmClassArrayList.get(i);
            File file=new File(film.getUrl_img());
            File file1=new File(film.getUrlfilm());
            sqLiteDatabase.removeDownload(film.getName(),file,file1);

            Log.d("yo", "onSwiped: "+filmClassArrayList.get(i).name);
            filmClassArrayList.remove(viewHolder.getAdapterPosition());
            downloadAdapterFragment.notifyDataSetChanged();
        }
    };
}