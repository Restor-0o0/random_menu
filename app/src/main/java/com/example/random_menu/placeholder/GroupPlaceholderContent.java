package com.example.random_menu.placeholder;

import android.content.ContentValues;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Element;
import com.example.random_menu.Data.Group;
import com.example.random_menu.Reposetory.InterfaceReposetoryGroups;
import com.example.random_menu.Reposetory.InterfaceSharedDataReposetory;
import com.example.random_menu.Utils.XMLUtils.InterfaceXmlManager;
import com.example.random_menu.Utils.XMLUtils.XMLWrapper;

import org.simpleframework.xml.core.PersistenceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.lifecycle.HiltViewModel;


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
@HiltViewModel
public class GroupPlaceholderContent extends ViewModel {

    InterfaceSharedDataReposetory sharedDataReposetory;
    InterfaceReposetoryGroups reposetoryGroups;
    InterfaceXmlManager xmlManager;
    private Random randomizer = new Random();

    public MutableLiveData<List<Group>> GROUPS = new MutableLiveData<>();
    public List<Group> noEmptyGROUPS = new ArrayList<>();
    public List<Group> SelectesGroups = new ArrayList<>();
    public XMLWrapper xmlResult;
    public final Map<Integer, Group> ITEM_MAP = new HashMap<Integer, Group>();
    public int maxPriority = 0;



    @Inject
    public GroupPlaceholderContent(
            InterfaceReposetoryGroups reposetoryGroups,
            InterfaceXmlManager xmlManager,
            InterfaceSharedDataReposetory sharedDataReposetory
    ){
        this.reposetoryGroups = reposetoryGroups;
        this.xmlManager = xmlManager;
        this.sharedDataReposetory = sharedDataReposetory;
    }

    public interface NotifyList{
        void CallNotify();
    }


    public void setShareGroup(Group group){
        this.sharedDataReposetory.setSharedGroup(group);
    }
    public void setShareElement(Element element){
        this.sharedDataReposetory.setSharedElement(element);
    }
    /*public Element getShareElement(){
        Element elem = sharedDataReposetory.getSharedElement();
        return
    }
    public Group getShareGroup(){

    }*/
    public void makeNoEmpty(Group group){
        for(int i = 0;i < noEmptyGROUPS.size();i++){
            if(Integer.valueOf(noEmptyGROUPS.get(i).id).equals(group.id)){
                return;
            }
        }
        if(group != null){
            noEmptyGROUPS.add(group);
        }
    }
    //запрос случайной группы
    public Group getRandom(){
        if(noEmptyGROUPS.size() > 0){
            return noEmptyGROUPS.get(randomizer.nextInt(noEmptyGROUPS.size()));
        }
        else{
            return null;
        }
    }
    //запрос количества
    public Integer getCount(){
        return GROUPS.getValue().size();
    }
    //геттер
    public LiveData<List<Group>> getGroups(){
        if(GROUPS.getValue() != null){
            Log.d("LoadGroup",String.valueOf(GROUPS.getValue().size()));
        }
        return GROUPS;
    }

    public Group getGroupByID(Integer id){
        return ITEM_MAP.get(id);
    }
    //Проверка группны на наличие и добавление или удаление
    public void checkGroups(Group item){
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
    public void deleteSelectedGroups(){
        List<Integer> ids = new ArrayList<>();
        for(Group item: SelectesGroups){
            ids.add(Integer.valueOf(item.id));
        }
        try {
            for(Group item: SelectesGroups) {
                List<Group> groups = GROUPS.getValue();
                groups.remove(item);
                GROUPS.setValue(groups);
            }
        }
        catch (Exception e){
            Log.e("deleteGroup", e.toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(Integer id: ids){
                    reposetoryGroups.deleteGroupAndNoHavingMoreLinksElements(id);
                }
            }
        }).start();
    }
    public void deleteGroup(int dbId){
        try {
            Group item = ITEM_MAP.get(dbId);
            Log.d("deleteGroup",String.valueOf(item.id));
            List<Group> groups = GROUPS.getValue();
            Log.d("deleteGroup",String.valueOf(groups.remove(item)));
            GROUPS.setValue(new ArrayList<>(groups));

        }
        catch (Exception e){
            Log.e("deleteGroup", e.toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                reposetoryGroups.deleteGroupAndNoHavingMoreLinksElements(dbId);
            }
        }).start();
    }

    public void swap(int fromPosition, int toPosition){
        List<Group> groups = GROUPS.getValue();
        int temp = groups.get(fromPosition).priority;
        groups.get(fromPosition).priority = groups.get(toPosition).priority;;
        groups.get(toPosition).priority = temp;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reposetoryGroups.update(
                            groups.get(toPosition).id,
                            null,
                            null,
                            groups.get(toPosition).priority,
                            null);
                    reposetoryGroups.update(
                            groups.get(fromPosition).id,
                            null,
                            null,
                            groups.get(fromPosition).priority,
                            null);
                }
                catch (Exception e){
                    Log.e("UpdateGroupsError",e.toString());
                }
            }
        }).start();
        //GROUPS.setValue(groups);
    }
    public void updateGroup(Integer id, String name, String comment){
        try{
            Objects.requireNonNull(ITEM_MAP.get(id)).name = name;
            Objects.requireNonNull(ITEM_MAP.get(id)).comment = comment;
        }
        catch (Exception e){
            Log.e("UpdateGroup",e.toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                reposetoryGroups.update(
                        id,
                        name,
                        comment,
                        null,
                        null
                );
                /*ContentValues cv = new ContentValues();
                cv.put(MainBaseContract.Groups.COLUMN_NAME_NAME, name);
                cv.put(MainBaseContract.Groups.COLUMN_NAME_COMMENT, comment);
                ContentProviderDB.update(
                        MainBaseContract.Groups.TABLE_NAME,
                        cv,
                        MainBaseContract.Groups._ID + " = " + id,
                        null
                );*/
            }
        }).start();
    }

    public void addItem(Group item) {
        List<Group> groups = GROUPS.getValue();
        //проверка на обновление приоритета
        if(Integer.valueOf(item.priority) > maxPriority){
            maxPriority = Integer.valueOf(item.priority);
        }
        groups.add(item);
        ITEM_MAP.put(item.id, item);
        GROUPS.setValue(groups);
    }
    public void clearGroups(){
        GROUPS.setValue(new ArrayList<>());
    }
    public void loadGroups(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    List<Group> groups = reposetoryGroups.getAll();
                    //Log.e("LoadGroupsStart",String.valueOf(GROUPS.getValue().size()) + String.valueOf(groups.size()));
                    noEmptyGROUPS.clear();
                    for(Group item : groups){
                        ITEM_MAP.put(item.id, item);
                        if(item.priority > maxPriority){
                            maxPriority = item.priority;
                        }
                        if(item.countElems > 0){
                            noEmptyGROUPS.add(item);
                        }
                    }
                    GROUPS.postValue(groups);
                    //Log.e("LoadGroupsEnd",String.valueOf(GROUPS.getValue().size()));
                }
                catch (Exception e){
                    Log.e("LoadGroupsError",e.toString());

                }
            }
        }).start();

    }
    public void deleteElement(int idGroup){
        if(ITEM_MAP.containsKey(idGroup)){
            try{
                if(Objects.requireNonNull(ITEM_MAP.get(idGroup)).countElems > 0){
                    Objects.requireNonNull(ITEM_MAP.get(idGroup)).countElems -= 1;
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            reposetoryGroups.update(
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
    public void addElement(int idGroup){

        if(ITEM_MAP.containsKey(idGroup)){
            try{
                Objects.requireNonNull(ITEM_MAP.get(idGroup)).countElems += 1;
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        reposetoryGroups.update(
                                idGroup,
                                null,
                                null,
                                null,
                                Objects.requireNonNull(ITEM_MAP.get(String.valueOf(idGroup))).countElems);
                    }
                }).start();
                makeNoEmpty(ITEM_MAP.get(idGroup));

            }
            catch (Exception e){
                Log.e("addElement",e.toString());
            }

        }

    }
    public void add(String name, String commet){
        new Thread(new Runnable() {
            @Override
            public void run() {
                reposetoryGroups.add(name,commet,maxPriority);
                maxPriority+=1;
                loadGroups();
            }
        }).start();
    }

    public void exportSelectedGroups(NotifyList callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    xmlResult = xmlManager.exportSelectedGroups(SelectesGroups);
                    callBack.CallNotify();
                }catch (Exception e){
                    Log.e("XMLExportError",e.toString());
                }
            }
        }).start();
    }
    public String groupsToXml(){
        try{
            return this.xmlManager.groupsToXml(xmlResult);
        } catch (Exception e) {
            Log.e("XMLConwertError",e.toString());
            return "";
        }
    }
    public void xmlToClass(String xmlString){
        try{
            this.xmlResult = this.xmlManager.xmlToClass(xmlString);
        }catch (PersistenceException pe){
            Log.e("XMLDeserializerError",pe.toString());

        }catch (Exception e){
            Log.e("XMLDeserializerError",e.toString());

        }
    }
    public void importIntoDB(){
        try{
            this.xmlManager.importGroupsIntoDB(xmlResult,maxPriority);
        }catch (Exception e){
            Log.e("ImportIntoDBError",e.toString());
        }
    }
}