package com.example.random_menu.placeholder;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.ElementsList.DialogFragments.MoreElemDialogFragment;
import com.example.random_menu.Reposetory.ReposetoryElements;
import com.example.random_menu.Reposetory.ReposetoryGroups;

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
                ReposetoryElements.deleteElem(dbId);
                handler.post(()->{
                    //Log.e("DeleteGrouperror", String.valueOf(GroupPlaceholderContent.getGroups().size()));
                    callNotify.CallNotify();
                });
            }
        }).start();
        ELEMENTS.remove(ITEM_MAP.get(String.valueOf(dbId)));
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                ReposetoryElements.update(
                        Integer.valueOf(ELEMENTS.get( toPosition).id),
                        null,
                        null,
                        ELEMENTS.get( toPosition).priority);
                ReposetoryElements.update(
                        Integer.valueOf(ELEMENTS.get( fromPosition).id),
                        null,
                        null,
                        ELEMENTS.get( fromPosition).priority);
            }
        }).start();
    }
    public static void clearElements(){
        ELEMENTS.clear();
    }
    public static void loadElements(){
        ELEMENTS.clear();
        try{
            Cursor cursor = ReposetoryElements.getAllElements(Integer.valueOf(idSelectGroup));
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
                int id = (int) ReposetoryElements.add(name,commet,maxPriority+1);
                Integer idElem = (int) ReposetoryElements.addGroupLink(id,Integer.valueOf(idSelectGroup));
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