package com.example.random_menu.placeholder;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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


    private static Random randomizer = new Random();
    public static List<PlaceholderItem> GROUPS = new ArrayList<PlaceholderItem>();
    public static List<PlaceholderItem> noEmptyGROUPS = new ArrayList<PlaceholderItem>();

    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();
    static public int maxPriority = 0;
    public static PlaceholderItem getRandom(){
        if(noEmptyGROUPS.size() > 0){
            return noEmptyGROUPS.get(randomizer.nextInt(noEmptyGROUPS.size()));
        }
        else{
            return null;
        }
    }
    public static Integer getCount(){
        return GROUPS.size();
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

    public static void makeNoEmpty(Integer idGroup){
        for(int i = 0;i < noEmptyGROUPS.size();i++){
            if(Integer.valueOf(noEmptyGROUPS.get(i).id).equals(idGroup)){
                return;
            }
        }
        for(int i = 0;i < GROUPS.size();i++){
            if(Integer.valueOf(GROUPS.get(i).id) == idGroup){
                noEmptyGROUPS.add(GROUPS.get(i));
            }
        }
    }


    public static void addItem(PlaceholderItem item) {
        //проверка на обновление приоритета
        if(Integer.valueOf(item.priority) > maxPriority){
            maxPriority = Integer.valueOf(item.priority);
        }
        GROUPS.add(item);
        ITEM_MAP.put(String.valueOf(item.id), item);
    }
    public static void clearGroups(){
        GROUPS.clear();
    }
    public static void loadGroups(){

        GROUPS.clear();
        try{
            Cursor cursor = ContentProviderDB.query(MainBaseContract.Groups.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    MainBaseContract.Groups.COLUMN_NAME_PRIORITY);
            Log.e("Fuck", String.valueOf(cursor.getCount()));
            cursor.moveToFirst();
            do{

                addItem(new GroupPlaceholderContent.PlaceholderItem(
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_COMMENT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_PRIORITY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_COUNT_ELEMS))
                ));
            }while(cursor.moveToNext());
            GROUPS.forEach(
                it ->{
                    //Log.e("COUNTTTTTTT",String.valueOf(it.countElems));
                    if(it.countElems > 0){
                        noEmptyGROUPS.add(it);
                    }
                }
            );
        }
        catch (Exception e){
            Log.e("Fuck",e.toString());
        }
    }
    public static void deleteElement(int idGroup){
        if(ITEM_MAP.containsKey(String.valueOf(idGroup))){
            if(ITEM_MAP.get(String.valueOf(idGroup)).countElems > 0){
                ITEM_MAP.get(String.valueOf(idGroup)).countElems -= 1;
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        ContentValues cv = new ContentValues();
                        cv.put(MainBaseContract.Groups.COLUMN_NAME_COUNT_ELEMS,(ITEM_MAP.get(String.valueOf(idGroup)).countElems));
                        ContentProviderDB.update(MainBaseContract.Groups.TABLE_NAME,cv,MainBaseContract.Groups._ID + " = " + String.valueOf(idGroup),null );
                    }
                }).start();
                if(ITEM_MAP.get(String.valueOf(idGroup)).countElems.equals(0)){
                    noEmptyGROUPS.remove(ITEM_MAP.get(String.valueOf(idGroup)));
                }
            }
        }
    }
    public static void addElement(int idGroup){

        if(ITEM_MAP.containsKey(String.valueOf(idGroup))){
            ITEM_MAP.get(String.valueOf(idGroup)).countElems += 1;
            new Thread(new Runnable(){
                @Override
                public void run() {
                    ContentValues cv = new ContentValues();
                    cv.put(MainBaseContract.Groups.COLUMN_NAME_COUNT_ELEMS,(ITEM_MAP.get(String.valueOf(idGroup)).countElems));
                    ContentProviderDB.update(MainBaseContract.Groups.TABLE_NAME,cv,MainBaseContract.Groups._ID + " = " + String.valueOf(idGroup),null );

                }
            }).start();
            GroupPlaceholderContent.makeNoEmpty(idGroup);

        }

    }
    public static void add(String name, String commet,Runnable notify ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues cv = new ContentValues();
                cv.put(MainBaseContract.Groups.COLUMN_NAME_NAME, name);
                cv.put(MainBaseContract.Groups.COLUMN_NAME_COMMENT, commet);
                cv.put(MainBaseContract.Groups.COLUMN_NAME_PRIORITY,String.valueOf(GroupPlaceholderContent.maxPriority + 1));
                ContentProviderDB.insert(MainBaseContract.Groups.TABLE_NAME,null,cv);
                notify.run();
            }
        }).start();

    }

    public static class PlaceholderItem {
        public final String id;
        public final String name;
        public final String comment;
        public Integer priority;
        public Integer countElems;

        public PlaceholderItem(String id, String content, String comment, Integer priority,Integer countElems) {
            this.id = id;
            this.name = content;
            this.comment = comment;
            this.priority = priority;
            this.countElems = countElems;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}