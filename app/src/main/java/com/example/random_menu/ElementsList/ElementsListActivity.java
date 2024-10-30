package com.example.random_menu.ElementsList;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.random_menu.Data.Item;
import com.example.random_menu.R;
import com.example.random_menu.databinding.ActivityMainBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import java.util.ArrayList;
import java.util.Objects;

public class ElementsListActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Item> ITEMS = new ArrayList<Item>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_elements);
        Objects.requireNonNull(getSupportActionBar()).hide();
        ElemPlaceholderContent.loadElements();
    }
}
