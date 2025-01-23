package com.example.random_menu.placeholder;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Component;
import com.example.random_menu.Data.Element;
import com.example.random_menu.Data.GroupCheck;
import com.example.random_menu.Reposetory.InterfaceReposetoryComponents;
import com.example.random_menu.Reposetory.InterfaceReposetoryElements;
import com.example.random_menu.Reposetory.InterfaceSharedDataReposetory;
import com.example.random_menu.Reposetory.ReposetoryComponents;
import com.example.random_menu.Reposetory.ReposetoryElements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
@HiltViewModel
public class ComponentPlaceholderContent extends ViewModel {

    private InterfaceReposetoryComponents reposetoryComponents;
    private InterfaceReposetoryElements reposetoryElements;
    private InterfaceSharedDataReposetory sharedDataReposetory;

    //Данные элемента
    public Element selectedElemen;
    public Integer positionSelectElem;
    //Список компонентов
    private MutableLiveData<List<Component>> Components = new MutableLiveData<>();
    //Список групп
    private  List<GroupCheck> Groups = new ArrayList<GroupCheck>();
    //Список групп на обновление в бд
    private  List<GroupCheck> SelectesGroups = new ArrayList<GroupCheck>();
    //мапа хрен знаетзачем, уже не помню
    public  Map<Integer, Component> ITEM_MAP = new HashMap<Integer, Component>();
    //геттеры
    public LiveData<List<Component>> getComponents(){
        return Components;
    }
    public List<GroupCheck> getGroups(){
        return Groups;
    }

    public interface NotifyList{
        void CallNotify();
    }

    @Inject
    public ComponentPlaceholderContent(
            InterfaceReposetoryComponents interfaceReposetoryComponents,
            InterfaceReposetoryElements interfaceReposetoryElements,
            InterfaceSharedDataReposetory sharedDataReposetory
    ){
        this.reposetoryComponents = interfaceReposetoryComponents;
        this.reposetoryElements = interfaceReposetoryElements;
        this.sharedDataReposetory = sharedDataReposetory;
    }

    public Element getShareElement(){
        this.selectedElemen = sharedDataReposetory.getSharedElement();
        return selectedElemen;
    }
    public void setShareElement(Element element){
        this.sharedDataReposetory.setSharedElement(element);
    }


    public void addComponent(String name, String comment,String qantity){

        new Thread(new Runnable() {
            @Override
            public void run() {
                int id = (int) reposetoryComponents.addComponent(selectedElemen.id,name,comment,qantity);
                List<Component> components = Components.getValue();
                components.add(new Component(
                        id,
                        name,
                        comment,
                        qantity
                ));
                Components.postValue(new ArrayList<>(components));
            }
        }).start();
    }
    //добавление компонента в список
    public void addComponentsItem(Component item) {
        List<Component> components = Components.getValue();
        components.add(item);
        Components.setValue(components);
        ITEM_MAP.put(item.id, item);
    }
    //добавление Группы в список
    public void addGroupsItem(GroupCheck item) {
        Groups.add(item);
    }
    //отчистки
    public void clearComponents(){
        Components.postValue(new ArrayList<>());
    }
    public void clearGroups(){
        Groups.clear();
    }
    public void clearUpdateGroups(){
        SelectesGroups.clear();
    }
    public void deleteComponent(Component component){
        new Thread(new Runnable() {
            @Override
            public void run() {
                reposetoryComponents.deleteComponent(component.id);
            }
        }).start();
        List<Component> components = Components.getValue();
        components.remove(component);
        Components.setValue(components);
    }
    //Проверка группны на наличие и добавление или удаление
    public void checkGroups(GroupCheck item){
        for(int i = 0; i < SelectesGroups.size(); i++){
            if(SelectesGroups.get(i).id == item.id){
                SelectesGroups.remove(i);
                return;
            }
        }
        SelectesGroups.add(item);
    }
    //обновление групп из списка в бд
    public void UpdatedGroupsDB(NotifyList callNotify){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //ContentValues cv = new ContentValues();
                //int counterActiveGroups = 0;
                for(GroupCheck item: SelectesGroups){
                    //Log.e("GroupsUpdates",item.id);
                    if(item.active){
                        //counterActiveGroups += 1;
                        Log.e("GroupsUpdates",String.valueOf(item.id));
                        reposetoryElements.addGroupLink(
                                selectedElemen.id,
                                item.id
                        );
                        //GroupPlaceholderContent.addElement(Integer.valueOf(item.id));

                        /*cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP,item.id);
                        cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT,idSelectElem);
                        ContentProviderDB.insert(MainBaseContract.ElemGroup.TABLE_NAME,null,cv);
                        cv.clear();*/
                    }
                    else{
                        //GroupPlaceholderContent.deleteElement(Integer.valueOf(item.id));
                        reposetoryElements.dropGroupLink(
                                selectedElemen.id,
                                item.id
                        );

                        //ContentProviderDB.delete(MainBaseContract.ElemGroup.TABLE_NAME,MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = "+item.id + " and "+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " = "+ idSelectElem,null);
                    }
                }
                callNotify.CallNotify();
            }
        }).start();


    }
    public void updateComponent(Integer id,String name,String comment, String quantity){
        Component item = ITEM_MAP.get(String.valueOf(id));
        if(item != null) {
            item.name = name;
            item.comment = comment;
            item.quantity = quantity;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    reposetoryComponents.updateComponent(id, name, comment, quantity);
                }
            }).start();
        }
    }
    public void updateElem(Integer id, String name,String comment,Integer priority){
        selectedElemen.name = name;
        selectedElemen.comment = comment;

        //ITEM_MAP.get(String.valueOf(id)).name = name;
        new Thread(new Runnable() {
            @Override
            public void run() {
                reposetoryElements.update(
                        id,
                        name,
                        comment,
                        priority
                );

            }
        }).start();
    }
    //загрузка групп с бд
    public void loadGroupsData() {
        clearGroups();
        try{
            //выхватили данные элемента
            List<GroupCheck> groups = reposetoryComponents.loadGroupsData(selectedElemen.id);
            Groups = groups;
            Log.e("LoadGroupsInfo",Groups.toString());
        }
        catch (Exception e){
            Log.e("LoadGroupsInfoError", e.toString());
        }
    }

    //Удалит элемент если он не закреплен ни за какой группой
    public boolean deleteIfNoGroups(){
        Log.e("ELEMDELETE","TYT");
        int countActive = 0;
        for (GroupCheck item: Groups) {
            if(item.active){
                countActive+=1;
            }
        }
        if(countActive == 0){
            Log.e("ELEMDELETE","SNESLO");
            return true;
            //ElemPlaceholderContent.deleteElem(Integer.valueOf(idSelectElem));
        }
        return false;
    }
    //загрузка информации элемента из бд
    public void loadElementData() {
        try{
            Element element = reposetoryComponents.loadElementData(selectedElemen.id);
            //выхватили данные элемента

        }
        catch (Exception e){
            Log.e("LoadElemInfoError", e.toString());
        }
    }
    //загрузка списка компонентов с бд
    public void loadComponentsData() {

                clearComponents();
                try{
                    List<Component> components = reposetoryComponents.loadComponentsData(selectedElemen.id);
                    if(components.size() > 0 && components != null){
                        Components.postValue(components);
                    }
                    Log.e("LoadComponentslistError", String.valueOf(components.size()));

                    //выхватили список компонентов
                }
                catch (Exception e){
                    Log.e("LoadComponentslistError", e.toString());
                }

    }
    //возвращает группы в которых состоит элемент в строчку через ","
    public String getActiveGroupsStr(){
        String str = "";
        for (GroupCheck item: Groups) {
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
    public void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadElementData();
                loadGroupsData();
                loadComponentsData();
            }
        }).start();

    }



}