package com.example.app_movie;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import java.io.Serializable;
import java.io.File;
public class AppConfig extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        createDB();
    }
    private void createDB(){
        DBStorageDMovie dbStorageDMovie=new DBStorageDMovie(getBaseContext());
        dbStorageDMovie.createTable();
    }
}
