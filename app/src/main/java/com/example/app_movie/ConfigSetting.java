package com.example.app_movie;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

class contextWrapper extends android.content.ContextWrapper{
    Context context;
    public contextWrapper(Context context){
        super(context);
    }

}
public class ConfigSetting {
    public static contextWrapper configLang(Context context, String lang, float fontSize){
        Locale sysLocale;
        Resources rs = context.getResources();
        Configuration config = rs.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = config.getLocales().get(0);
        } else {
            sysLocale = config.locale;
        }
        if ( config.fontScale != fontSize)
            config.fontScale = fontSize;
        if (!lang.equals("") && !sysLocale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale);
            } else {
                config.locale = locale;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context = context.createConfigurationContext(config);
            } else {
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
        }
        return new contextWrapper(context);
    }
    public static Context adjustFontSize(Context context, float fontSize) {
        Configuration configuration = context.getResources().getConfiguration();
        // This will apply to all text like -> Your given text size * fontScale
        if (configuration.fontScale != fontSize)
            configuration.fontScale = fontSize;//1.0f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context = context.createConfigurationContext(configuration);
        } else {
            context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        }
        return context; //context.createConfigurationContext(configuration);
    }
}
