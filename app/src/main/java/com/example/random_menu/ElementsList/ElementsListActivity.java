package com.example.random_menu.ElementsList;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.random_menu.Data.Item;
import com.example.random_menu.Reposetory.InterfaceSharedDataReposetory;
import com.example.random_menu.Utils.ThemeManager;
import com.example.random_menu.databinding.ActivityListElementsBinding;
import com.example.random_menu.placeholder.ElemPlaceholderContent;
import com.example.random_menu.Reposetory.SharedDataReposetory;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ElementsListActivity extends AppCompatActivity {

    private ElemPlaceholderContent viewModel;
    private ActivityListElementsBinding binding;
    private ArrayList<Item> ITEMS = new ArrayList<Item>();
    @Inject
    ThemeManager themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListElementsBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(this).get(ElemPlaceholderContent.class);
        binding.groupNameTitle.setText(viewModel.getShareGroup().name);


        setContentView(binding.getRoot());
        themeManager.initDefaultTheme();
        //Objects.requireNonNull(getSupportActionBar()).hide();


    }


    @Override
    protected void onResume() {
        super.onResume();
        //viewModel.checkElementOnEdit();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
