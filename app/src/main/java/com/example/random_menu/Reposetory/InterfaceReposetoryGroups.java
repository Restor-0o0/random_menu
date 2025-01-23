package com.example.random_menu.Reposetory;

import androidx.lifecycle.LiveData;

import com.example.random_menu.Data.Group;

import java.util.List;

public interface InterfaceReposetoryGroups {
    void deleteGroupAndNoHavingMoreLinksElements(int dbId);
    long add(String name, String commet,Integer maxPriority);
    void update(Integer id,String name, String comment, Integer priority, Integer countElements);
    List<Group> getAll();
    }
