package com.example.random_menu.ElementsList;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.random_menu.Data.Item;
import com.example.random_menu.R;
import com.example.random_menu.databinding.ActivityMainBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

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
