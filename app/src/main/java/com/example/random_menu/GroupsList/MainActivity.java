package com.example.random_menu.GroupsList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.DBController;
import com.example.random_menu.R;
import com.example.random_menu.Utils.ThemeManager;
import com.example.random_menu.databinding.ActivityMainBinding;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    //@Inject
    //SharedPreferences saveManager;

    @Inject
    ThemeManager themeManager;
    private boolean init = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //getWindow().setStatusBarColor(R.attr.colorBackground);
        setContentView(R.layout.activity_main);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        //this.deleteDatabase("RandDB.db");
        //setTheme(themeManager.getCurrentTheme());

        themeManager.initDefaultTheme();

        ContentProviderDB.init(getBaseContext());
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if(savedInstanceState == null){
            GroupPlaceholderContent.loadGroups();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.frameMain, GroupsRecycleFragment.class,null)
                    .add(R.id.barFragment, BottomBarGroupsFragment.class,null)
                    .commit();
        }
    }

    public void rand(){

    }
}