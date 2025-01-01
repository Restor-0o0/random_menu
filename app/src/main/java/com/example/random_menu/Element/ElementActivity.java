package com.example.random_menu.Element;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.random_menu.R;
import com.example.random_menu.databinding.ActivityElementBinding;

import java.util.Objects;

public class ElementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);
        //Objects.requireNonNull(getSupportActionBar()).hide();
    }

}