package com.example.random_menu.placeholder;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Reposetory.ReposetoryGroups;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public static  List<PlaceholderItem> SelectesGroups = new ArrayList<PlaceholderItem>();

    public static final Map<String, PlaceholderItem> ITEM_MAP = new HashMap<String, PlaceholderItem>();
    static public int maxPriority = 0;


    public interface NotifyList{
        void CallNotify();
    }

    //запрос случайной группы
    public static PlaceholderItem getRandom(){
        if(noEmptyGROUPS.size() > 0){
            return noEmptyGROUPS.get(randomizer.nextInt(noEmptyGROUPS.size()));
        }
        else{
            return null;
        }
    }
    //запрос количества
    public static Integer getCount(){
        return GROUPS.size();
    }
    //геттер
    public static List<PlaceholderItem> getGroups(){
        return GROUPS;
    }


    //Проверка группны на наличие и добавление или удаление
    public static void checkGroups(PlaceholderItem item){
        if(SelectesGroups.remove(item)){
         return;
        }
        else{
            SelectesGroups.add(item);
        }
        /*for(PlaceholderItem sitem : SelectesGroups){

        }
        for(int i = 0; i < SelectesGroups.size(); i++){

        }*/
    }
    public static void deleteSelectedGroups(){
        List<Integer> ids = new ArrayList<>();
        for(PlaceholderItem item: SelectesGroups){
            ids.add(Integer.valueOf(item.id));
        }
        try {
            for(PlaceholderItem item: SelectesGroups) {
                GROUPS.remove(item);
            }
        }
        catch (Exception e){
            Log.e("deleteGroup", e.toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(Integer id: ids){
                    ReposetoryGroups.deleteGroupAndNoHavingMoreLinksElements(id);
                }
            }
        }).start();
    }
    public static void deleteGroup(int dbId){
        try {
            GROUPS.remove(ITEM_MAP.get(String.valueOf(dbId)));
        }
        catch (Exception e){
            Log.e("deleteGroup", e.toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReposetoryGroups.deleteGroupAndNoHavingMoreLinksElements(dbId);
            }
        }).start();
    }

    public static void swap(int fromPosition, int toPosition){
        int temp = GROUPS.get((int) fromPosition).priority;
        GROUPS.get( fromPosition).priority = GROUPS.get((int) toPosition).priority;;
        GROUPS.get( toPosition).priority = temp;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReposetoryGroups.update(
                        Integer.valueOf(GROUPS.get( toPosition).id),
                        null,
                        null,
                        GROUPS.get( toPosition).priority,
                        null);
                ReposetoryGroups.update(
                        Integer.valueOf(GROUPS.get( fromPosition).id),
                        null,
                        null,
                        GROUPS.get( fromPosition).priority,
                        null);

            }
        }).start();

    }
    public static void updateGroup(Integer id, String name, String comment){
        try{
            Objects.requireNonNull(ITEM_MAP.get(String.valueOf(id))).name = name;
            Objects.requireNonNull(ITEM_MAP.get(String.valueOf(id))).comment = comment;
        }
        catch (Exception e){
            Log.e("UpdateGroup",e.toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues cv = new ContentValues();

                cv.put(MainBaseContract.Groups.COLUMN_NAME_NAME, name);
                cv.put(MainBaseContract.Groups.COLUMN_NAME_COMMENT, comment);
                ContentProviderDB.update(
                        MainBaseContract.Groups.TABLE_NAME,
                        cv,
                        MainBaseContract.Groups._ID + " = " + id,
                        null
                );
            }
        }).start();
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
            Cursor cursor = ReposetoryGroups.getAll();
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
                    if(it.countElems > 0){
                        noEmptyGROUPS.add(it);
                    }
                }
            );
        }
        catch (Exception e){
            Log.e("ErrorLoadAllGroups",e.toString());
        }
    }
    public static void deleteElement(int idGroup){
        if(ITEM_MAP.containsKey(String.valueOf(idGroup))){
            try{
                if(Objects.requireNonNull(ITEM_MAP.get(String.valueOf(idGroup))).countElems > 0){
                    Objects.requireNonNull(ITEM_MAP.get(String.valueOf(idGroup))).countElems -= 1;
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            ReposetoryGroups.update(
                                    idGroup,
                                    null,
                                    null,
                                    null,
                                    Objects.requireNonNull(ITEM_MAP.get(String.valueOf(idGroup))).countElems);
                        }
                    }).start();
                    if(Objects.requireNonNull(ITEM_MAP.get(String.valueOf(idGroup))).countElems.equals(0)){
                        noEmptyGROUPS.remove(ITEM_MAP.get(String.valueOf(idGroup)));
                    }
                }
            }
            catch (Exception e){
                Log.e("deleteElement",e.toString());
            }

        }
    }
    public static void addElement(int idGroup){

        if(ITEM_MAP.containsKey(String.valueOf(idGroup))){
            try{
                Objects.requireNonNull(ITEM_MAP.get(String.valueOf(idGroup))).countElems += 1;
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        ReposetoryGroups.update(
                                idGroup,
                                null,
                                null,
                                null,
                                Objects.requireNonNull(ITEM_MAP.get(String.valueOf(idGroup))).countElems);
                    }
                }).start();
                GroupPlaceholderContent.makeNoEmpty(idGroup);

            }
            catch (Exception e){
                Log.e("addElement",e.toString());
            }

        }

    }
    public static void add(String name, String commet,Runnable notify ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReposetoryGroups.add(name,commet);
                notify.run();
            }
        }).start();

    }

    public static void exportSelectedGroups(){

    }


    public static class PlaceholderItem {
        public final String id;
        public String name;
        public String comment;
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