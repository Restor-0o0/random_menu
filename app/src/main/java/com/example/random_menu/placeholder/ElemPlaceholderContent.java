package com.example.random_menu.placeholder;

import android.content.ContentValues;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Element;
import com.example.random_menu.Data.Group;
import com.example.random_menu.Reposetory.InterfaceReposetoryElements;
import com.example.random_menu.Reposetory.InterfaceSharedDataReposetory;
import com.example.random_menu.Utils.XMLUtils.InterfaceXmlManager;
import com.example.random_menu.Utils.XMLUtils.XMLWrapper;

import org.simpleframework.xml.core.PersistenceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
@HiltViewModel
public class ElemPlaceholderContent extends ViewModel {

    InterfaceReposetoryElements reposetoryElements;
    InterfaceSharedDataReposetory sharedDataReposetory;
    InterfaceXmlManager xmlManager;


    public Group selectedGroup;
    private Random randomizer = new Random();
    public int maxPriority = 0;
    public XMLWrapper xmlResult;
    private final MutableLiveData<List<Element>> ELEMENTS = new MutableLiveData<>();
    public  List<Element> SelectesElements = new ArrayList<>();
    public Map<Integer, Element> ITEM_MAP = new HashMap<Integer, Element>();


    @Inject
    public ElemPlaceholderContent(
            InterfaceReposetoryElements reposetoryElements,
            InterfaceSharedDataReposetory sharedDataReposetory,
            InterfaceXmlManager xmlManager

    ){
        this.reposetoryElements = reposetoryElements;
        this.sharedDataReposetory = sharedDataReposetory;
        this.xmlManager = xmlManager;
    }


    public void setShareElement(Element element){
        this.sharedDataReposetory.setSharedElement(element);
    }


    public Group getShareGroup(){
        this.selectedGroup = sharedDataReposetory.getSharedGroup();
        return this.selectedGroup;
    }

    //Проверка группны на наличие и добавление или удаление
    public void checkElement(Element item){
        if(SelectesElements.remove(item)){
        }
        else{
            SelectesElements.add(item);
        }
    }
    public void checkElementOnEdit(){
        Element element = this.sharedDataReposetory.getSharedElement();
        List<Element> elements = ELEMENTS.getValue();
        for(Element elem : elements){
            if(elem.id.equals(element.id)){
                elem.name = element.name;
                elem.comment = element.comment;
            }
        }
        ELEMENTS.setValue(elements);
    }

    public void deleteSelectedElements(){
        List<Integer> ids = new ArrayList<>();
        for(Element item: SelectesElements){
            ids.add(Integer.valueOf(item.id));
        }
        try {
            List<Element> elements = ELEMENTS.getValue();
            for(Element item: SelectesElements) {
                elements.remove(item);
            }
            ELEMENTS.setValue(elements);
        }
        catch (Exception e){
            Log.e("deleteGroup", e.toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(Integer id: ids){
                    reposetoryElements.deleteElem(id);
                }
            }
        }).start();
    }
    public interface NotifyList{
        void CallNotify();
    }
    public Element getRandom(){
        return ELEMENTS.getValue().get(randomizer.nextInt(ELEMENTS.getValue().size()));
    }

    public LiveData<List<Element>> getElements(){
        return ELEMENTS;
    }
    public void deleteElem(int dbId){
        Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                reposetoryElements.deleteElem(dbId);
            }
        }).start();
        List<Element> elements = ELEMENTS.getValue();
        elements.remove(ITEM_MAP.get(dbId));
        ELEMENTS.setValue(elements);
    }
    public Integer getCount(){
        return ELEMENTS.getValue().size();
    }
    public void addItem(Element item) {
        if(item.priority > maxPriority){
            maxPriority = item.priority;
        }
        List<Element> elements = ELEMENTS.getValue();
        elements.add(item);
        ELEMENTS.postValue(new ArrayList<>(elements));
        ITEM_MAP.put(item.id, item);
    }

    public void updateElem(Integer id, String name,String comment,Integer priority){
        ITEM_MAP.get(id).name = name;
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
    public void swap(int fromPosition, int toPosition){
        int temp = ELEMENTS.getValue().get((int) fromPosition).priority;
        ELEMENTS.getValue().get( fromPosition).priority = ELEMENTS.getValue().get((int) toPosition).priority;;
        ELEMENTS.getValue().get( toPosition).priority = temp;

        new Thread(new Runnable() {
            @Override
            public void run() {
                reposetoryElements.update(
                        Integer.valueOf(ELEMENTS.getValue().get( toPosition).id),
                        null,
                        null,
                        ELEMENTS.getValue().get( toPosition).priority);
                reposetoryElements.update(
                        Integer.valueOf(ELEMENTS.getValue().get( fromPosition).id),
                        null,
                        null,
                        ELEMENTS.getValue().get( fromPosition).priority);
            }
        }).start();
    }
    public void clearElements(){
        ELEMENTS.setValue(new ArrayList<>());
    }
    public void loadElements(){
        ELEMENTS.setValue(new ArrayList<>());
        try{
            List<Element> elements = reposetoryElements.getAllElements(selectedGroup.id);
            for(Element elem: elements){
                addItem(elem);
            }
        }
        catch (Exception e){
            Log.e("Error select ElementsPlaceholder",e.toString());
        }
    }

    public void add(String name, String commet){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int id = (int) reposetoryElements.add(name,commet,maxPriority+1);
                Integer idElem = (int) reposetoryElements.addGroupLink(id,selectedGroup.id);
                if(idElem > 0){
                    Log.e("AddElementInMutable",String.valueOf(id));
                    addItem(new Element(id,
                            name,
                            commet,
                            maxPriority + 1)
                    );
                    maxPriority += 1;
                    sharedDataReposetory.setUpdateGroups(selectedGroup.id,1);
                };
            }
        }).start();
    }



    public void exportSelectedElements(NotifyList callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    xmlResult = xmlManager.exportSelectedElements(selectedGroup,SelectesElements);
                    Log.d("XMLExport"," exportToResultVal" );
                    callBack.CallNotify();
                } catch (Exception e) {
                    Log.e("XMLExportError", e.toString());
                }
            }
        }).start();
    }
    public String groupsToXml(){
        try{
            Log.d("XMLExport",this.xmlManager.groupsToXml(xmlResult));

            return this.xmlManager.groupsToXml(xmlResult);
        } catch (Exception e) {
            Log.e("XMLConwertError",e.toString());
            return "";
        }
    }
    public int xmlToClass(String xmlString){
        try{
            xmlResult = xmlManager.xmlToClass(xmlString);
            return 0;
        }catch (PersistenceException pe){
            Log.e("XMLDeserializerError",pe.toString());
            return 1;
        }catch (Exception e){
            Log.e("XMLDeserializerError",e.toString());
            return 2;
        }
    }
    public void importIntoDB(NotifyList callNotify){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    xmlManager.importElementsIntoDB(
                            xmlResult,
                            selectedGroup
                    );
                }catch (Exception e){
                    Log.e("ImportIntoDBError",e.toString());
                }

            }
        }).start();

    }
}