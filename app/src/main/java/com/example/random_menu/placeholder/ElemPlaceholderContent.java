package com.example.random_menu.placeholder;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;

import java.util.ArrayList;
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
public class ElemPlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static String idSelectGroup;
    public static String nameSelectGroup;
    private static Random randomizer = new Random();
    static public int maxPriority = 0;
    private static final List<PlaceholderItem> ELEMENTS = new ArrayList<PlaceholderItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();
    public static PlaceholderItem getRandom(){
        return ELEMENTS.get(randomizer.nextInt(ELEMENTS.size()));
    }

    public static List<PlaceholderItem> getElements(){
        return ELEMENTS;
    }
    public static void deleteElem(int position){
        ELEMENTS.remove(position);
    }
    public static void addItem(PlaceholderItem item) {
        if(Integer.valueOf(item.priority) > maxPriority){
            maxPriority = Integer.valueOf(item.priority);
        }
        ELEMENTS.add(item);
        ITEM_MAP.put(item.id, item);
    }
    public static void swap(int fromPosition, int toPosition){
        int temp = ELEMENTS.get((int) fromPosition).priority;
        ELEMENTS.get( fromPosition).priority = ELEMENTS.get((int) toPosition).priority;;
        ELEMENTS.get( toPosition).priority = temp;
        ElemPlaceholderContent.PlaceholderItem fromItem = ELEMENTS.get( fromPosition);
        ElemPlaceholderContent.PlaceholderItem toItem = ELEMENTS.get( toPosition);
        ContentValues cv = new ContentValues();
        cv.put(MainBaseContract.Elements.COLUMN_NAME_PRIORITY,fromItem.priority);
        ContentProviderDB.update(MainBaseContract.Elements.TABLE_NAME,cv,"_ID="+fromItem.id,null);
        cv.put(MainBaseContract.Elements.COLUMN_NAME_PRIORITY,toItem.priority);
        ContentProviderDB.update(MainBaseContract.Elements.TABLE_NAME,cv,"_ID="+toItem.id,null);

    }

    public static void clearElements(){
        ELEMENTS.clear();
    }
    public static void loadElements(){
        ELEMENTS.clear();
        try{
            Cursor cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME,null,MainBaseContract.Elements._ID + " IN (SELECT "+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " FROM "+ MainBaseContract.ElemGroup.TABLE_NAME+" WHERE "+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+"="+idSelectGroup+ ")",null,null,null,MainBaseContract.Elements._ID);
            Log.e("Fuck", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{

                addItem(new ElemPlaceholderContent.PlaceholderItem(
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_PRIORITY))
                        ));
            }while(cursor.moveToNext());

        }
        catch (Exception e){
            Log.e("Fuck",e.toString());
        }
    }


    public static class PlaceholderItem {
        public final String id;
        public final String name;
        public Integer priority;

        public PlaceholderItem(String id, String content,Integer priority) {
            this.id = id;
            this.name = content;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}