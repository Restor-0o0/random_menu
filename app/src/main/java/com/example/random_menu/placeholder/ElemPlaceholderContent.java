package com.example.random_menu.placeholder;

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
    private static Random randomizer = new Random();
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

    public static void addItem(PlaceholderItem item) {
        ELEMENTS.add(item);
        ITEM_MAP.put(item.id, item);
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
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_COMMENT))));
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

        public PlaceholderItem(String id, String content, String comment) {
            this.id = id;
            this.name = content;
            this.comment = comment;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}