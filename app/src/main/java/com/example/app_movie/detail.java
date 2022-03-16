package com.example.app_movie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class detail extends AppCompatActivity {
    ImageView poster;
    TextView title,description,actor,gerne,directorl;
    Button play,download;
    FilmClass filmClass;
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
                startActivity(intent);
            }
        });
    }
}