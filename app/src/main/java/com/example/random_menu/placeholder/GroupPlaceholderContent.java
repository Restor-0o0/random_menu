package com.example.random_menu.placeholder;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Reposetory.ReposetoryComponents;
import com.example.random_menu.Reposetory.ReposetoryElements;
import com.example.random_menu.Reposetory.ReposetoryGroups;
import com.example.random_menu.Utils.XMLUtils.Component;
import com.example.random_menu.Utils.XMLUtils.Element;
import com.example.random_menu.Utils.XMLUtils.Group;
import com.example.random_menu.Utils.XMLUtils.XMLWrapper;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Handler;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.lifecycle.HiltViewModel;


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
    public static XMLWrapper xmlResult;
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

    public static PlaceholderItem getGroupByID(Integer id){
        return ITEM_MAP.get(String.valueOf(id)) ;
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

    public static void exportSelectedGroups(NotifyList callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (SelectesGroups) {
                        XMLWrapper xmlExport = new XMLWrapper();
                        List<Group> groups = new ArrayList<>();
                        for (PlaceholderItem currentGroup : SelectesGroups) {
                            Group xmlGroup = new Group();

                            xmlGroup.id = currentGroup.id;
                            xmlGroup.name = currentGroup.name;
                            xmlGroup.comment = currentGroup.comment;

                            List<Element> elements = new ArrayList<>();

                            Cursor cursorElements = ReposetoryElements.getAllElements(Integer.valueOf(currentGroup.id));
                            if (cursorElements.getCount() > 0) {
                                cursorElements.moveToFirst();
                                do {
                                    Element xmlElem = new Element();

                                    xmlElem.id = cursorElements.getString(cursorElements.getColumnIndexOrThrow(MainBaseContract.Elements._ID));
                                    xmlElem.name = cursorElements.getString(cursorElements.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_NAME));
                                    xmlElem.comment = cursorElements.getString(cursorElements.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_COMMENT));
                                    xmlElem.priority = cursorElements.getString(cursorElements.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_PRIORITY));
                                    List<Component> components = new ArrayList<>();

                                    Cursor cursorComponents = ReposetoryComponents.loadComponentsData(Integer.valueOf(xmlElem.id));
                                    if (cursorComponents.getCount() > 0) {
                                        cursorComponents.moveToFirst();
                                        do {
                                            Component xmlComponent = new Component();

                                            xmlComponent.id = cursorComponents.getString(cursorComponents.getColumnIndexOrThrow(MainBaseContract.Components._ID));
                                            xmlComponent.name = cursorComponents.getString(cursorComponents.getColumnIndexOrThrow(MainBaseContract.Components.COLUMN_NAME_NAME));
                                            xmlComponent.comment = cursorComponents.getString(cursorComponents.getColumnIndexOrThrow(MainBaseContract.Components.COLUMN_NAME_COMMENT));
                                            xmlComponent.count = cursorComponents.getString(cursorComponents.getColumnIndexOrThrow(MainBaseContract.Components.COLUMN_NAME_QUANTITY));

                                            components.add(xmlComponent);
                                        } while (cursorComponents.moveToNext());
                                    }
                                    xmlElem.components = components;
                                    cursorComponents.close();
                                    elements.add(xmlElem);
                                } while (cursorElements.moveToNext());

                            }
                            xmlGroup.elements = elements;
                            cursorElements.close();
                            groups.add(xmlGroup);
                        }

                        xmlExport.groups = groups;
                        xmlResult = xmlExport;
                    }
                    callBack.CallNotify();
                }catch (Exception e){
                    Log.e("XMLExportError",e.toString());
                }
            }
        }).start();
    }
    public static String groupsToXml(){
        try {
            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(xmlResult, writer);

            //String xml = writer.toString();
            //Log.d("SimpleXML", xml);

            return writer.toString();
        } catch (Exception e) {
            Log.e("XMLConwertError",e.toString());
            return null;
        }
    }
    public static int xmlToClass(String xmlString){
        try{
            Serializer serializer = new Persister();
            XMLWrapper root = serializer.read(XMLWrapper.class, xmlString);

            xmlResult = root;

            Log.d("RESULTTTT", String.valueOf(root.groups.get(0).name));
        }catch (PersistenceException pe){
            Log.e("XMLDeserializerError",pe.toString());
            return 1;
        }catch (Exception e){
            Log.e("XMLDeserializerError",e.toString());
            return 2;
        }
        return 0;
    }
    public static void importIntoDB(NotifyList callNotify){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    int groupID,elemID;
                    if(xmlResult.groups != null) {
                        for (Group group : xmlResult.groups) {
                            groupID = (int) ReposetoryGroups.add(group.name, group.comment);
                            if (group.elements != null) {


                                for (Element elem : group.elements) {
                                    elemID = (int) ReposetoryElements.add(elem.name, elem.comment, Integer.valueOf(elem.priority));
                                    ReposetoryElements.addGroupLink(elemID, groupID);
                                    if (elem.components != null) {
                                        for (Component component : elem.components) {
                                            ReposetoryComponents.addComponent(
                                                    Integer.valueOf(elem.id),
                                                    component.name,
                                                    component.comment,
                                                    component.count);
                                        }
                                    }

                                }
                            }
                        }
                    }
                    callNotify.CallNotify();
                }catch (Exception e){
                    Log.e("ImportIntoDBError",e.toString());
                }

            }
        }).start();

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