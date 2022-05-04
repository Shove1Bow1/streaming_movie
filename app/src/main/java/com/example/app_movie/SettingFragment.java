package com.example.app_movie;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingFragment extends PreferenceFragmentCompat {
    Context SettingContext;
    private String textSize,language;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_screen,rootKey);
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        ListPreference textSizeList=(ListPreference) findPreference("textSize");
        String langSupport= sharedPreferences.getString("configLanguage", "default");//load it from SharedPref
        float fSize = Float.parseFloat(sharedPreferences.getString("textSize", "1.0f"));
        ConfigSetting.configLang(getActivity().getBaseContext(),langSupport,fSize);
        textSizeList.setOnPreferenceChangeListener((preference, newValue) -> {
            textSize =  String.valueOf(newValue);
            if (textSize.contains("0.5f")) {
                Toast.makeText(getContext(), R.string.small_font, Toast.LENGTH_SHORT).show();
            }
            else if (textSize.contains("0.7f")) {
                Toast.makeText(getContext(), R.string.medium_font, Toast.LENGTH_SHORT).show();
            }
            else if (textSize.contains("0.9f")) {
                Toast.makeText(getContext(), R.string.large_font, Toast.LENGTH_SHORT).show();
            }
            getActivity().recreate();
            return true;
        });
        ListPreference Language = (ListPreference) findPreference("configLanguage");
        Language.setOnPreferenceChangeListener((preference, newValue) -> {
            language =  String.valueOf(newValue);
            if (language.contains("VI")) {
                Toast.makeText(getContext(), R.string.vietnamese_language, Toast.LENGTH_SHORT).show();
            }
            if (language.contains("EN")) {
                Toast.makeText(getContext(), R.string.english_language, Toast.LENGTH_SHORT).show();
            }
            getActivity().recreate();
            return true;
        });
    }

    @Override
    public void onAttach(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        float Size = Float.parseFloat(sharedPreferences.getString("textSize", "0.5f"));
        super.onAttach(ConfigSetting.adjustFontSize(context,Size));
    }
}
