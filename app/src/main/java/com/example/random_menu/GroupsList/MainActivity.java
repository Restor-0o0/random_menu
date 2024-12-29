package com.example.random_menu.GroupsList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.os.Bundle;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.DBController;
import com.example.random_menu.R;
import com.example.random_menu.databinding.ActivityMainBinding;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        //this.deleteDatabase("RandDB.db");
        //setTheme(R.style.DarkTheme);

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