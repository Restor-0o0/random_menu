package com.example.random_menu.Elements;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Item;
import com.example.random_menu.Groups.BottomBarGroupsFragment;
import com.example.random_menu.Groups.GroupsRecycleFragment;
import com.example.random_menu.R;
import com.example.random_menu.databinding.ActivityMainBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.ArrayList;
import java.util.Objects;

public class ElementsActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Item> ITEMS = new ArrayList<Item>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_elements);
        Objects.requireNonNull(getSupportActionBar()).hide();

        if(savedInstanceState == null){
            ElemPlaceholderContent.loadElements();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.frameMain, ElementsRecycleFragment.class,null)
                    .add(R.id.barFragment, BottomBarElementsFragment.class,null)
                    .commit();

        }

    }

    public void rand(){

    }
}
