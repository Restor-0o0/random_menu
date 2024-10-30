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

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class
ComponentPlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static String idSelectElem;
    public static String nameSelectElem;
    public static String commentSelectElem;
    private static final List<ComponentsPlaceholderItem> Components = new ArrayList<ComponentsPlaceholderItem>();
    private static final List<GroupsPlaceholderItem> Groups = new ArrayList<GroupsPlaceholderItem>();
    private static final List<GroupsPlaceholderItem> UpdateGroups = new ArrayList<GroupsPlaceholderItem>();

    public static final Map<String, ComponentsPlaceholderItem> ITEM_MAP = new HashMap<String, ComponentsPlaceholderItem>();

    public static List<ComponentsPlaceholderItem> getComponents(){
        return Components;
    }
    public static List<GroupsPlaceholderItem> getGroups(){
        return Groups;
    }

    public static void addElementsItem(ComponentsPlaceholderItem item) {
        Components.add(item);
        ITEM_MAP.put(item.id, item);
    }
    public static void addGroupsItem(GroupsPlaceholderItem item) {
        Groups.add(item);
    }
    public static void clearComponents(){
        Components.clear();
    }
    public static void clearGroups(){
        Groups.clear();
    }
    public static void clearUpdateGroups(){
        UpdateGroups.clear();
    }
    public static void checkUpdateGroups(GroupsPlaceholderItem item){
        for(int i = 0;i < UpdateGroups.size();i++){
            if(UpdateGroups.get(i).id == item.id){
                UpdateGroups.remove(i);
                return;
            }
        }
        UpdateGroups.add(item);

    }
    public static void UpdatedGroupsDB(){
        ContentValues cv = new ContentValues();
        for(GroupsPlaceholderItem item: UpdateGroups){
            Log.e("GroupsUpdates",item.id);
            if(item.active){
                Log.e("GroupsUpdates",item.id);
             cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP,item.id);
             cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT,idSelectElem);
             ContentProviderDB.insert(MainBaseContract.ElemGroup.TABLE_NAME,null,cv);
             cv.clear();
            }
            else{
                ContentProviderDB.delete(MainBaseContract.ElemGroup.TABLE_NAME,MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = "+item.id + " and "+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " = "+ idSelectElem,null);
            }
        }
    }
    public static void loadGroupsData() {
        clearGroups();

        try{
            //выхватили данные элемента
            Cursor cursor = ContentProviderDB.query(MainBaseContract.Groups.TABLE_NAME,
                    new String[]{MainBaseContract.Groups._ID,
                                MainBaseContract.Groups.COLUMN_NAME_NAME,
/*волшебная строка*/            "(SELECT COUNT("+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+") FROM "+MainBaseContract.ElemGroup.TABLE_NAME+" WHERE "+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+"="+MainBaseContract.Groups._ID+" and "+MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT+" = "+idSelectElem+" ) AS Active"},
                        null ,
                     null,
                         null,
                          null,
                         MainBaseContract.Groups.COLUMN_NAME_PRIORITY);
            if(cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                do {
                    Log.e("LoadGroupsInfo",cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_NAME))+ " " +cursor.getString(cursor.getColumnIndexOrThrow("Active")));
                    addGroupsItem(new GroupsPlaceholderItem(
                            cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_NAME)),
                            cursor.getInt(cursor.getColumnIndexOrThrow("Active"))
                    ));
                } while (cursor.moveToNext());
            }
            Log.e("LoadGroupsInfo",Groups.toString());
        }
        catch (Exception e){
            Log.e("LoadGroupsInfoError", e.toString());
        }
    }
    public static void loadElementData() {
        try{

            //выхватили данные элемента
            Cursor cursor = ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME, null, MainBaseContract.Elements._ID + "=" + idSelectElem, null, null, null, null);
            if(cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                nameSelectElem = cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_NAME));
                commentSelectElem = cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_COMMENT));
            }
        }
        catch (Exception e){
            Log.e("LoadElemInfoError", e.toString());
        }
    }
    public static void loadComponentsData() {
        clearComponents();
        Components.clear();
        try{
            //выхватили список компонентов
            Cursor cursor = ContentProviderDB.query(MainBaseContract.Components.TABLE_NAME, null, MainBaseContract.Components.COLUMN_NAME_ELEMENT + "=" + idSelectElem, null, null, null, MainBaseContract.Components.COLUMN_NAME_QUANTITY + " DESC");
            if(cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                do {

                    addElementsItem(new ComponentsPlaceholderItem(
                            cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Components._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Components.COLUMN_NAME_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Components.COLUMN_NAME_COMMENT)),
                            cursor.getFloat(cursor.getColumnIndexOrThrow(MainBaseContract.Components.COLUMN_NAME_QUANTITY))
                    ));
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            Log.e("LoadComponentslistError", e.toString());
        }
    }

    public static String getActiveGroupsStr(){
        String str = "";
        for (GroupsPlaceholderItem item: Groups) {
            if(item.active){
                str+= item.name+",";
            }
        }
        return str.substring(0,str.length()-1);
    }

    public static void loadData() {
        loadElementData();
        loadGroupsData();
        loadComponentsData();
    }


    public static class ComponentsPlaceholderItem {
        public final String id;
        public final String name;
        public final String comment;
        public Float quantity;

        public ComponentsPlaceholderItem(String id, String name, String comment, Float quantity) {
            this.id = id;
            this.name = name;
            this.comment = comment;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class GroupsPlaceholderItem {
        public final String id;
        public final String name;
        public boolean active;


        public GroupsPlaceholderItem(String id, String name,int active) {
            this.id = id;
            this.name = name;
            if(active == 1)
            {
                this.active = true;
            }
            else{
                this.active = false;
            }
        }

        @Override
        public String toString() {
            return name + " " + active;
        }

    }


}