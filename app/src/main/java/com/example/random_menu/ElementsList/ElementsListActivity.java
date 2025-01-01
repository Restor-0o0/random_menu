package com.example.random_menu.ElementsList;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.random_menu.Data.Item;
import com.example.random_menu.R;
import com.example.random_menu.Utils.ThemeManager;
import com.example.random_menu.databinding.ActivityListElementsBinding;
import com.example.random_menu.databinding.ActivityMainBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ElementsListActivity extends AppCompatActivity {

    private ActivityListElementsBinding binding;
    private ArrayList<Item> ITEMS = new ArrayList<Item>();
    @Inject
    ThemeManager themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListElementsBinding.inflate(getLayoutInflater());
        binding.groupNameTitle.setText(ElemPlaceholderContent.nameSelectGroup);

        setContentView(binding.getRoot());
        themeManager.initDefaultTheme();
        //Objects.requireNonNull(getSupportActionBar()).hide();
        ElemPlaceholderContent.loadElements();

    }



}
