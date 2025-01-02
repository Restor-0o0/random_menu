package com.example.random_menu.placeholder;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Reposetory.ReposetoryComponents;
import com.example.random_menu.Reposetory.ReposetoryElements;

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
    //Данные элемента
    public static String idSelectElem;
    public static String nameSelectElem;
    public static String commentSelectElem;
    public static Integer positionSelectElem;
    //Список компонентов
    private static final List<ComponentsPlaceholderItem> Components = new ArrayList<ComponentsPlaceholderItem>();
    //Список групп
    private static final List<GroupsPlaceholderItem> Groups = new ArrayList<GroupsPlaceholderItem>();
    //Список групп на обновление в бд
    private static final List<GroupsPlaceholderItem> SelectesGroups = new ArrayList<GroupsPlaceholderItem>();
    //мапа хрен знаетзачем, уже не помню
    public static final Map<String, ComponentsPlaceholderItem> ITEM_MAP = new HashMap<String, ComponentsPlaceholderItem>();
    //геттеры
    public static List<ComponentsPlaceholderItem> getComponents(){
        return Components;
    }
    public static List<GroupsPlaceholderItem> getGroups(){
        return Groups;
    }

    public interface NotifyList{
        void CallNotify();
    }


    //добавление компонента в список
    public static void addComponentsItem(ComponentsPlaceholderItem item) {
        Components.add(item);
        ITEM_MAP.put(item.id, item);
    }
    //добавление Группы в список
    public static void addGroupsItem(GroupsPlaceholderItem item) {
        Groups.add(item);
    }
    //отчистки
    public static void clearComponents(){
        Components.clear();
    }
    public static void clearGroups(){
        Groups.clear();
    }
    public static void clearUpdateGroups(){
        for(GroupsPlaceholderItem item: SelectesGroups){
            item.active = !item.active;
        }
        SelectesGroups.clear();
    }
    public static void deleteComponent(int position,NotifyList callNotify){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReposetoryComponents.deleteComponent(Integer.valueOf(Components.get(position).id));
            }
        }).start();
        Components.remove(position);
        callNotify.CallNotify();
    }
    //Проверка группны на наличие и добавление или удаление
    public static void checkGroups(GroupsPlaceholderItem item){
        for(int i = 0; i < SelectesGroups.size(); i++){
            if(SelectesGroups.get(i).id == item.id){
                SelectesGroups.remove(i);
                return;
            }
        }
        SelectesGroups.add(item);
    }
    //обновление групп из списка в бд
    public static void UpdatedGroupsDB(NotifyList callNotify){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //ContentValues cv = new ContentValues();
                //int counterActiveGroups = 0;
                for(GroupsPlaceholderItem item: SelectesGroups){
                    //Log.e("GroupsUpdates",item.id);
                    if(item.active){
                        //counterActiveGroups += 1;
                        Log.e("GroupsUpdates",item.id);
                        ReposetoryElements.addGroupLink(
                                Integer.valueOf(idSelectElem),
                                Integer.valueOf(item.id)
                        );
                        GroupPlaceholderContent.addElement(Integer.valueOf(item.id));
                        /*cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP,item.id);
                        cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT,idSelectElem);
                        ContentProviderDB.insert(MainBaseContract.ElemGroup.TABLE_NAME,null,cv);
                        cv.clear();*/
                    }
                    else{
                        GroupPlaceholderContent.deleteElement(Integer.valueOf(item.id));
                        ReposetoryElements.dropGroupLink(
                                Integer.valueOf(idSelectElem),
                                Integer.valueOf(item.id)
                        );

                        //ContentProviderDB.delete(MainBaseContract.ElemGroup.TABLE_NAME,MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = "+item.id + " and "+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " = "+ idSelectElem,null);
                    }
                }
                callNotify.CallNotify();
            }
        }).start();


    }
    //загрузка групп с бд
    public static void loadGroupsData() {
        clearGroups();
        try{
            //выхватили данные элемента
            Cursor cursor = ReposetoryComponents.loadGroupsData(Integer.valueOf(idSelectElem));
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

    //Удалит элемент если он не закреплен ни за какой группой
    public static void deleteIfNoGroups(){
        Log.e("ELEMDELETE","TYT");
        int countActive = 0;
        for (GroupsPlaceholderItem item: Groups) {
            if(item.active){
                countActive+=1;
            }
        }
        if(countActive == 0){
            Log.e("ELEMDELETE","SNESLO");
            ElemPlaceholderContent.deleteElem(positionSelectElem,Integer.valueOf(idSelectElem),()->{

            });
        }
    }
    //загрузка информации элемента из бд
    public static void loadElementData() {
        try{
            Cursor cursor = ReposetoryComponents.loadElementData(Integer.valueOf(idSelectElem));
            //выхватили данные элемента
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
    //загрузка списка компонентов с бд
    public static void loadComponentsData() {
        clearComponents();
        Components.clear();
        try{
            Cursor cursor = ReposetoryComponents.loadComponentsData(Integer.valueOf(idSelectElem));
            //выхватили список компонентов
            if(cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                do {
                    addComponentsItem(new ComponentsPlaceholderItem(
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
    //возвращает группы в которых состоит элемент в строчку через ","
    public static String getActiveGroupsStr(){
        String str = "";
        for (GroupsPlaceholderItem item: Groups) {
            if(item.active){
                str+= item.name+", ";
            }
        }
        if(!str.isEmpty()){
            return str.substring(0,str.length()-2);
        }
        else{
            return str;
        }
    }
    //загрузка всех данных для экрана
    public static void loadData() {
        loadElementData();
        loadGroupsData();
        loadComponentsData();
    }

    //Компонент списка элементов
    public static class ComponentsPlaceholderItem {
        public final String id;
        public String name;
        public String comment;
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
    //компонент списка групп
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
            return name;
        }

    }


}