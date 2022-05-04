package com.example.app_movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class searchview extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<FilmClass> film_object;
    adapter_search film_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searcnview);
        recyclerView=findViewById(R.id.rv_search);
        searchView=findViewById(R.id.searchView_searchview);
        Gson gson = new Gson();
        SharedPreferences mPrefs =getSharedPreferences("oject", Context.MODE_PRIVATE);
        String json = mPrefs.getString("MyObject", "");
        Type collectionType = new TypeToken<Collection<FilmClass>>(){}.getType();
        film_object = gson.fromJson(json,collectionType);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
         film_adapter =new adapter_search(film_object,this);
        recyclerView.setAdapter( film_adapter );
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText); return false;
            }
        });
    }
    public void hideSoftKeyboard(View view){
        InputMethodManager imm
                =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void search(String textSearch){

        ArrayList<FilmClass> searchList = new ArrayList<>();
        if (searchList.size()>0){
            film_adapter.clear();
        }
        for (FilmClass furniture:film_object){
            Log.e("search",""+furniture.name);
            if (furniture.name.toLowerCase().contains(textSearch.toLowerCase())){
                searchList.add(furniture);
            }

        }
        film_adapter.addlist(searchList);
        film_adapter.notifyDataSetChanged();

    }
}