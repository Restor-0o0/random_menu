package com.example.random_menu.Reposetory;

import android.database.Cursor;

import androidx.lifecycle.LiveData;

import com.example.random_menu.Data.Component;
import com.example.random_menu.Data.Element;
import com.example.random_menu.Data.Group;
import com.example.random_menu.Data.GroupCheck;

import java.util.List;

public interface InterfaceReposetoryComponents {
    long addComponent(Integer idElement,String name, String comment, String quantity);
    List<GroupCheck> loadGroupsData(Integer idSelectElem);
    List<Component> loadComponentsData(Integer idSelectElem);
    Element loadElementData(Integer idSelectElem);
    void UpdatedGroupsDB(Integer idElement);
    void deleteComponent(Integer idComponent) ;
    void updateComponent(Integer id,String name,String comment, String quantity);

}
