package com.example.random_menu.Element;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.random_menu.R;
import com.example.random_menu.databinding.ActivityElementBinding;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ElementActivity extends AppCompatActivity {

    private ComponentPlaceholderContent viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);
        viewModel = new ViewModelProvider(this).get(ComponentPlaceholderContent.class);
        //Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.setShareElement(viewModel.selectedElemen);
    }
}