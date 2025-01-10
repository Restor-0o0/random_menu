package com.example.random_menu.placeholder;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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
import java.util.Random;

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
    public static XMLWrapper xmlResult;
    private static final List<PlaceholderItem> ELEMENTS = new ArrayList<PlaceholderItem>();
    public static  List<PlaceholderItem> SelectesElements = new ArrayList<PlaceholderItem>();

    //Проверка группны на наличие и добавление или удаление
    public static void checkElement(PlaceholderItem item){
        if(SelectesElements.remove(item)){
        }
        else{
            SelectesElements.add(item);
        }
    }
    public static void deleteSelectedElements(){
        List<Integer> ids = new ArrayList<>();
        for(PlaceholderItem item: SelectesElements){
            ids.add(Integer.valueOf(item.id));
        }
        try {
            for(PlaceholderItem item: SelectesElements) {
                ELEMENTS.remove(item);
            }
        }
        catch (Exception e){
            Log.e("deleteGroup", e.toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(Integer id: ids){
                    ReposetoryElements.deleteElem(id);
                }
            }
        }).start();
    }
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
    public static void deleteElem(int dbId){
        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReposetoryElements.deleteElem(dbId);
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
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_COMMENT)),
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
                            commet,
                            maxPriority + 1));
                    maxPriority += 1;
                    GroupPlaceholderContent.addElement(Integer.valueOf(idSelectGroup));
                };
                notify.run();
            }
        }).start();
    }



    public static void exportSelectedElements(NotifyList callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (SelectesElements) {
                        GroupPlaceholderContent.PlaceholderItem groupItem = GroupPlaceholderContent.getGroupByID(Integer.valueOf(idSelectGroup));
                        if (groupItem != null) {

                            XMLWrapper xmlExport = new XMLWrapper();
                            List<Group> groups = new ArrayList<>();
                            Group xmlGroup = new Group();


                            xmlGroup.id = groupItem.id;
                            xmlGroup.name = groupItem.name;
                            xmlGroup.comment = groupItem.comment;

                            List<Element> elements = new ArrayList<>();

                            for (PlaceholderItem elem : SelectesElements) {

                                Element xmlElem = new Element();

                                xmlElem.id = elem.id;
                                xmlElem.name = elem.name;
                                xmlElem.comment = elem.comment;
                                xmlElem.priority = String.valueOf(elem.priority);
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
                            }
                            xmlGroup.elements = elements;
                            groups.add(xmlGroup);


                            xmlExport.groups = groups;
                            xmlResult = xmlExport;
                        }
                    }
                    callBack.CallNotify();
                } catch (Exception e) {
                    Log.e("XMLExportError", e.toString());
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
                int groupID = Integer.valueOf(idSelectGroup);
                int elemID;
                if(xmlResult.groups != null) {
                    for (Group group : xmlResult.groups) {
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

        public PlaceholderItem(String id, String content,String comment,Integer priority) {
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