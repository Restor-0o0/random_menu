package com.example.random_menu.GroupsList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.DBController;
import com.example.random_menu.R;
import com.example.random_menu.Reposetory.InterfaceReposetoryGroups;
import com.example.random_menu.Reposetory.ReposetoryGroups;
import com.example.random_menu.Utils.ThemeManager;
import com.example.random_menu.databinding.ActivityMainBinding;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private GroupPlaceholderContent viewModel;
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
        //viewModel = new ViewModelProvider(this).get(GroupPlaceholderContent.class);
        themeManager.initDefaultTheme();

        ContentProviderDB.init(getBaseContext());
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if(savedInstanceState == null){
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