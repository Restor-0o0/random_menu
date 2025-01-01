package com.example.random_menu.placeholder;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.ElementsList.DialogFragments.MoreElemDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ElemPlaceholderContent {

    public static String idSelectGroup;
    public static String nameSelectGroup;
    private static Random randomizer = new Random();
    static public int maxPriority = 0;
    private static final List<PlaceholderItem> ELEMENTS = new ArrayList<PlaceholderItem>();

    public interface NotifyList{
        void CallNotify();
    }
    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();
    public static PlaceholderItem getRandom(){
        return ELEMENTS.get(randomizer.nextInt(ELEMENTS.size()));
    }

    public static List<PlaceholderItem> getElements(){
        return ELEMENTS;
    }
    public static void deleteElem(int position, int dbId,NotifyList callNotify ){
        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Cursor cursor = ContentProviderDB.query(MainBaseContract.ElemGroup.TABLE_NAME,null,MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + "=" + String.valueOf(dbId),null,null,null,null);
                    if(cursor.getCount() > 0){
                        cursor.moveToFirst();
                        synchronized (GroupPlaceholderContent.GROUPS){
                            do{
                                GroupPlaceholderContent.deleteElement(cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP)));
                            }while(cursor.moveToNext());
                        }
                    }
                    ContentProviderDB.delete(
                            MainBaseContract.Components.TABLE_NAME,
                            MainBaseContract.Components.COLUMN_NAME_ELEMENT + " = " + String.valueOf(dbId),
                            null
                    );
                    ContentProviderDB.delete(
                            MainBaseContract.ElemGroup.TABLE_NAME,
                            MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " = " + String.valueOf(dbId),
                            null
                    );
                    ContentProviderDB.delete(
                            MainBaseContract.Elements.TABLE_NAME,
                            MainBaseContract.Elements._ID + " = " + String.valueOf(dbId),
                            null
                    );

                    handler.post(()->{
                        //Log.e("DeleteGrouperror", String.valueOf(GroupPlaceholderContent.getGroups().size()));
                        callNotify.CallNotify();
                    });
                }catch (Exception e){
                    Log.e("DeleteGrouperror",e.toString());
                }
            }
        }).start();
        ELEMENTS.remove(position);
    }
    public static Integer getCount(){
        return ELEMENTS.size();
    }
    public static void addItem(PlaceholderItem item) {
        if(Integer.valueOf(item.priority) > maxPriority){
            maxPriority = Integer.valueOf(item.priority);
        }
        ELEMENTS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void updateElem(Integer id, String name){
        ITEM_MAP.get(String.valueOf(id)).name = name;
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
            Log.e("Start select ElementsPlaceholder", String.valueOf(cursor.getCount()));
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
            Log.e("Error select ElementsPlaceholder",e.toString());
        }
    }

    public static void add(String name, String commet,Runnable notify ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues cv = new ContentValues();
                cv.put(MainBaseContract.Elements.COLUMN_NAME_NAME, name);
                cv.put(MainBaseContract.Elements.COLUMN_NAME_COMMENT, commet);
                cv.put(MainBaseContract.Elements.COLUMN_NAME_PRIORITY,maxPriority+1);
                int id = (int) ContentProviderDB.insert(MainBaseContract.Elements.TABLE_NAME,null,cv);
                cv.clear();
                cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT,id);
                cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP, ElemPlaceholderContent.idSelectGroup);
                Integer idElem = (int) ContentProviderDB.insert(MainBaseContract.ElemGroup.TABLE_NAME,null,cv);


                if(idElem > 0){
                    addItem(new PlaceholderItem(String.valueOf(idElem),
                            name,
                            maxPriority + 1));
                    maxPriority += 1;
                    GroupPlaceholderContent.addElement(Integer.valueOf(idSelectGroup));
                };
                notify.run();
            }
        }).start();

    }


    public static class PlaceholderItem {
        public final String id;
        public String name;
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