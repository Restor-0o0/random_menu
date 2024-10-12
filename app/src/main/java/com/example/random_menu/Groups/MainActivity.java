package com.example.random_menu.Groups;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Item;
import com.example.random_menu.R;
import com.example.random_menu.databinding.ActivityMainBinding;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

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
        Log.e("itp", "0");

        ContentProviderDB.init(getBaseContext());

        //DBController dbControl = new DBController(getBaseContext());
        //SQLiteDatabase DB = dbControl.getWritableDatabase();
        //Log.e("Name",dbControl.getDatabaseName());
        Log.e("itp", "1");
        ContentValues cv = new ContentValues();
        for(int i = 1; i < 3 ; i++){
            cv.put(MainBaseContract.Groups.COLUMN_NAME_NAME,"Главная " + String.valueOf(i));
            cv.put(MainBaseContract.Groups.COLUMN_NAME_COMMENT,"Главная группа(тестовая)");
            cv.put(MainBaseContract.Groups.COLUMN_NAME_PRIORITY,String.valueOf(i));
            ContentProviderDB.insert(MainBaseContract.Groups.TABLE_NAME,null,cv);
        }
        cv.clear();

        GroupPlaceholderContent.loadGroups();

        Log.e("itp", "2");
        for(int i = 1; i < 10 ; i++){
            cv.put(MainBaseContract.Elements.COLUMN_NAME_NAME,"Elem "+ String.valueOf(i));
            cv.put(MainBaseContract.Elements.COLUMN_NAME_COMMENT,"Elem "+ String.valueOf(i));
            ContentProviderDB.insert(MainBaseContract.Elements.TABLE_NAME,null,cv);


        }
        Cursor curs = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME,null,"_ID NOT IN(select Element from ElemGroup)",null,null,null,null);
        curs.moveToFirst();
        cv.clear();
        Log.e("itp", "3");
        try {
            do{
                Log.e("itp", "31");
                Log.e("curssss",String.valueOf(curs.getString(curs.getColumnIndexOrThrow(MainBaseContract.Elements._ID))));
                Log.e("itp", "32");
                cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT,String.valueOf(curs.getString(curs.getColumnIndexOrThrow(MainBaseContract.Elements._ID))));
                Log.e("itp", "33");
                cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP, GroupPlaceholderContent.getRandom().id);
                ContentProviderDB.insert(MainBaseContract.ElemGroup.TABLE_NAME,null,cv);
                cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP, GroupPlaceholderContent.getRandom().id);
                ContentProviderDB.insert(MainBaseContract.ElemGroup.TABLE_NAME,null,cv);
                Log.e("itp", "34");
            }while(curs.moveToNext());
        }catch (Exception e){
            Log.e("itp", String.valueOf(e));
        }

        Log.e("itp", "4");

        Log.e("itp", "5");

        //PlaceholderContent.ITEMS.add(new PlaceholderContent.PlaceholderItem("1","pidr",true));
        //PlaceholderContent.ITEMS.add(new PlaceholderContent.PlaceholderItem("2","pidr",false));

        if(savedInstanceState == null){
            //ItemFragment itemFrag = new ItemFragment();
            //Bundle bundle = new Bundle();

            //bundle.putSerializable("itemsList",ITEMS);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.frameMain, GroupsRecycleFragment.class,null)
                    .add(R.id.barFragment, BottomBarGroupsFragment.class,null)
                    .commit();


            //binding.frameMain.getFragment().
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