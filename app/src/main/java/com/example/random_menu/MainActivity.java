package com.example.random_menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.random_menu.DB.DBController;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Item;
import com.example.random_menu.databinding.ActivityMainBinding;
import com.example.random_menu.placeholder.PlaceholderContent;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Item> ITEMS = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.deleteDatabase("RandDB.db");
        DBController dbControl = new DBController(getBaseContext());
        SQLiteDatabase DB = dbControl.getWritableDatabase();
        //Log.e("Name",dbControl.getDatabaseName());

        ContentValues cv = new ContentValues();
        for(int i = 1; i < 20 ; i++){
            cv.put(MainBaseContract.Groups.COLUMN_NAME_NAME,"Главная " + String.valueOf(i));
            cv.put(MainBaseContract.Groups.COLUMN_NAME_COMMENT,"Главная группа(тестовая)");
            cv.put(MainBaseContract.Groups.COLUMN_NAME_PRIORITY,String.valueOf(i));
            DB.insert(MainBaseContract.Groups.TABLE_NAME,null,cv);
        }



        try{
            //DB.rawQuery("SELECT MAX(" + MainBaseContract.Groups.COLUMN_NAME_PRIORITY + ") FROM " + MainBaseContract.Groups.TABLE_NAME,null);

            Cursor cursor = DB.query(MainBaseContract.Groups.TABLE_NAME,null,null,null,null,null,MainBaseContract.Groups.COLUMN_NAME_PRIORITY);
            Log.e("Fuck", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{
                cursor.getColumnIndex(MainBaseContract.Groups.COLUMN_NAME_NAME);
                cursor.getColumnIndex(MainBaseContract.Groups.COLUMN_NAME_COMMENT);
                cursor.getColumnIndex(MainBaseContract.Groups.COLUMN_NAME_PRIORITY);
                PlaceholderContent.ITEMS.add(new PlaceholderContent.PlaceholderItem(
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_PRIORITY))));
            }while(cursor.moveToNext());

        }
        catch (Exception e){
            Log.e("Fuck",e.toString());
        }


        //PlaceholderContent.ITEMS.add(new PlaceholderContent.PlaceholderItem("1","pidr",true));
        //PlaceholderContent.ITEMS.add(new PlaceholderContent.PlaceholderItem("2","pidr",false));

        if(savedInstanceState == null){
            //ItemFragment itemFrag = new ItemFragment();
            //Bundle bundle = new Bundle();

            //bundle.putSerializable("itemsList",ITEMS);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.frameMain, ItemFragment.class,null)
                    .add(R.id.barFragment, BottomBarFragment.class,null)
                    .commit();

        }

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