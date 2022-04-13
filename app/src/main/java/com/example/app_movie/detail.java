package com.example.app_movie;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class detail extends AppCompatActivity {
    private static final int PERMISSION_STORAGE_CODE =1000 ;
    ImageView poster;
    TextView title,description,actor,gerne,directorl;
    Button play,download;
    FilmClass filmClass;
    DBStorageDMovie sqLiteDatabase;
    File fileUriImg,fileUriVideo,UriLocal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        poster=findViewById(R.id.detail_poster);
        title=findViewById(R.id.detail_title);
        description=findViewById(R.id.detail_decription);
        actor=findViewById(R.id.detail_actor);
        directorl=findViewById(R.id.detail_director);
        gerne=findViewById(R.id.detail_gerne);
        Intent intent=getIntent();
        sqLiteDatabase=new DBStorageDMovie(getBaseContext());
        filmClass=(FilmClass) intent.getExtras().getSerializable("infor");
        play=findViewById(R.id.detail_play);
        download=findViewById(R.id.detail_download);
        Glide.with(this).load(filmClass.getUrl_img()).into(poster);
        title.setText(filmClass.getName());
        description.setText(description.getText()+" "+filmClass.getDescription());
        directorl.setText(directorl.getText()+" "+filmClass.getDirector());
        gerne.setText(gerne.getText()+" "+filmClass.getGenre());
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),play_film.class);
                intent.putExtra("name",filmClass.getName());
                intent.putExtra("url", filmClass.getUrlfilm());
                Log.d("film_URL_CHECK", "onClick: "+filmClass.getUrlfilm());
                startActivity(intent);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_STORAGE_CODE);
                    }
                    else{
                            startDownloading(filmClass.getUrlfilm(),filmClass.getUrl_img());
                    }
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void startDownloading(String Url, String Url_Image){
        String filmURL=Url;
        String imgURL=Url_Image;
        if(sqLiteDatabase.checkExistInDownloadFolder(filmClass.getName())==true){
            DownloadManager.Request request=new DownloadManager.Request(Uri.parse(filmURL));
            DownloadManager.Request request2=new DownloadManager.Request(Uri.parse(imgURL));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle("Download Film");
            request.setDescription("Downloading Film");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request2.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE);
            request2.setTitle("Download Image");
            request2.setDescription("Downloading Image");
            request2.allowScanningByMediaScanner();
            request2.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            File file=new File(getExternalFilesDir(null)+"/"+title.getText().toString());
            if(!file.exists())
                file.mkdir();
            UriLocal=file;
            file=new File(getExternalFilesDir(null)+"/"+title.getText().toString()+"/"+"Bloody.jpg");
            fileUriImg=file;
            Uri uri_Img=Uri.fromFile(file);
            request2.setDestinationUri(uri_Img);
            file=new File(getExternalFilesDir(null)+"/"+title.getText().toString()+"/"+"Bloody.mp4");
            fileUriVideo=file;
            Uri uri_Video=Uri.fromFile(file);
            request.setDestinationUri(uri_Video);
            DownloadManager manager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            manager.enqueue(request2);
            sqLiteDatabase.insertDownloadInfo(filmClass,fileUriImg,fileUriVideo,UriLocal);
        }
        else{
            Toast.makeText(detail.this,"This movie has been downloaded",Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        startDownloading(filmClass.getUrlfilm(),filmClass.getUrl_img());
                }
                else{
                    Toast.makeText(getBaseContext(),"Permission not granted",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void putIntoGetSharePreference(){

    }
}