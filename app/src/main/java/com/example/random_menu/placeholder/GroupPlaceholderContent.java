package com.example.random_menu.placeholder;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class GroupPlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    private static Random randomizer = new Random();
    public static List<PlaceholderItem> GROUPS = new ArrayList<PlaceholderItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();
    static public int maxPriority = 0;
    public static PlaceholderItem getRandom(){
        return GROUPS.get(randomizer.nextInt(GROUPS.size()));
    }

    public static List<PlaceholderItem> getGroups(){
        return GROUPS;
    }
    public static void deleteGroup(int position){
        GROUPS.remove(position);
    }
    public static void swap(int fromPosition, int toPosition){
        int temp = GROUPS.get((int) fromPosition).priority;
        GROUPS.get( fromPosition).priority = GROUPS.get((int) toPosition).priority;;
        GROUPS.get( toPosition).priority = temp;
        PlaceholderItem fromItem = GROUPS.get( fromPosition);
        PlaceholderItem toItem = GROUPS.get( toPosition);
        ContentValues cv = new ContentValues();
        cv.put(MainBaseContract.Groups.COLUMN_NAME_PRIORITY,fromItem.priority);
        ContentProviderDB.update(MainBaseContract.Groups.TABLE_NAME,cv,"_ID="+fromItem.id,null);
        cv.put(MainBaseContract.Groups.COLUMN_NAME_PRIORITY,toItem.priority);
        ContentProviderDB.update(MainBaseContract.Groups.TABLE_NAME,cv,"_ID="+toItem.id,null);

    }

    public static void addItem(PlaceholderItem item) {
        //проверка на обновление приоритета
        if(Integer.valueOf(item.priority) > maxPriority){
            maxPriority = Integer.valueOf(item.priority);
        }
        GROUPS.add(item);
        ITEM_MAP.put(String.valueOf(item.priority), item);
    }
    public static void clearGroups(){
        GROUPS.clear();
    }
    public static void loadGroups(){
        GROUPS.clear();
        try{
            Cursor cursor = ContentProviderDB.query(MainBaseContract.Groups.TABLE_NAME,null,null,null,null,null,MainBaseContract.Groups.COLUMN_NAME_PRIORITY);
            Log.e("Fuck", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{
                //cursor.getColumnIndex(MainBaseContract.Groups.COLUMN_NAME_NAME);
                //cursor.getColumnIndex(MainBaseContract.Groups.COLUMN_NAME_COMMENT);
                //cursor.getColumnIndex(MainBaseContract.Groups.COLUMN_NAME_PRIORITY);
                addItem(new GroupPlaceholderContent.PlaceholderItem(
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_COMMENT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_PRIORITY))));
            }while(cursor.moveToNext());

        }
        catch (Exception e){
            Log.e("Fuck",e.toString());
        }
    }


    public static class PlaceholderItem {
        public final String id;
        public final String name;
        public final String comment;
        public Integer priority;

        public PlaceholderItem(String id, String content, String comment, Integer priority) {
            this.id = id;
            this.name = content;
            this.comment = comment;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}