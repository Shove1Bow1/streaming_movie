package com.example.app_movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Main extends AppCompatActivity {
    Button button;
    androidx.appcompat.widget.Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    View view_item;

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String lang= sharedPreferences.getString("configLanguage", "vi");//load it from SharedPref
        float f = Float.parseFloat(sharedPreferences.getString("textSize", "1.0f"));
        Context context =ConfigSetting.configLang(newBase, lang,f);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        loadFragment(new HomeFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        view_item=findViewById(R.id.find_toolbar);
        view_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), searchview.class);
                startActivity(intent);
            }
        });

    }
    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()){
                case R.id.home_navigation:
                    fragment=new HomeFragment();
                    Log.e("Check",""+fragment.toString());
                    loadFragment(fragment);
                    return true;
                case R.id.download_navigation:
                    if (firebaseAuth.getCurrentUser()!=null){
                       fragment=new DownloadWithoutLoginFragment();
                    }
                    else {
                        fragment=new DownloadFragment();
                    }
                    Log.e("Check",""+fragment.toString());
                    loadFragment(fragment);
                    return true;
                case R.id.setings_navigation:
                    if (firebaseAuth.getCurrentUser()!=null){
                        fragment=new SettingWithoutLoginFragment();
                    }
                    else {
                        fragment=new AccountFragment();
                    }
                    Log.e("Check",""+fragment.toString());
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Log.e("Check",""+fragment.toString());
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

