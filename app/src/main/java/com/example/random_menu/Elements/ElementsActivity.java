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
            //ItemFragment itemFrag = new ItemFragment();
            //Bundle bundle = new Bundle();
            ElemPlaceholderContent.loadElements();
            //bundle.putSerializable("itemsList",ITEMS);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.frameMain, ElementsRecycleFragment.class,null)
                    .add(R.id.barFragment, BottomBarElementsFragment.class,null)
                    .commit();

        }
        //PlaceholderContent.ITEMS.add(new PlaceholderContent.PlaceholderItem("20","20","20"));
        Log.e("itp", "6");
    }

    public void rand(){

    }
}
/*
<TextView
android:id="@+id/item_name"
android:layout_width="330dp"
android:layout_height="50dp"
android:layout_gravity="center"
android:layout_marginStart="20dp"
android:layout_weight="1"
android:gravity="center"
android:text=""
android:textColor="?attr/colorFont"
android:textSize="20sp" />
*/


/*
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
android:layout_width="390dp"
android:layout_height="70dp"
android:background="@drawable/back"
android:foregroundGravity="center"
android:gravity="center"
android:orientation="horizontal">

 */