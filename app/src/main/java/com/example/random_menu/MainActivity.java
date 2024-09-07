package com.example.random_menu;

import androidx.appcompat.app.AppCompatActivity;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.random_menu.DB.DBController;
import com.example.random_menu.Data.Item;
import com.example.random_menu.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Item> ITEMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.gyt);
        /*DBController dbControl = new DBController(getBaseContext());
        SQLiteDatabase DB = dbControl.getWritableDatabase();
        //DB.query(

        //);
        /*if(savedInstanceState == null){
            ItemFragment itemFrag = new ItemFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("itemsList",ITEMS);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, ItemFragment.class,bundle)
                    .commit();
        }*/

    }
}