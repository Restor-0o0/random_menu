package com.example.random_menu.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.random_menu.R;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Singleton
public class ThemeManager {
    private SharedPreferences saveManager;
    private Context context;

    @Inject
    public ThemeManager(@ApplicationContext Context context, SharedPreferences saveManager){
        this.context = context;
        this.saveManager = saveManager;
    }

    public void toggleTheme(){
        SharedPreferences.Editor editor = saveManager.edit();
        if(saveManager.getString("theme", "light").equals("dark")){
            Log.e("ToggleTheme","1_____");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Log.e("ToggleTheme1",saveManager.getString("theme","light"));
            //this.context.getApplicationContext().setTheme(R.style.LightTheme);
            editor.putString("theme","light");
        }else{
            Log.e("ToggleTheme","2____");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Log.e("ToggleTheme2",saveManager.getString("theme","light"));
            //this.context.getApplicationContext().setTheme(R.style.DarkTheme);
            editor.putString("theme","dark");
        }
        editor.apply();
    }
    public void initDefaultTheme(){

        if(saveManager.getString("theme", "light").equals("dark")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
