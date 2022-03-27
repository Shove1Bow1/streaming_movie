package com.example.app_movie;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        rcDownload.setAdapter(downloadAdapterFragment);
    }

    @Override
    public void onClickList(FilmClass filmClass) {
        Intent intent=new Intent(getActivity(),detail.class);
        intent.putExtra("infor",filmClass);
        startActivity(intent);
    }
}