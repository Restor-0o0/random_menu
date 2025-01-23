package com.example.random_menu.Reposetory;

import android.database.Cursor;

import androidx.lifecycle.LiveData;

import com.example.random_menu.Data.Element;

import java.util.List;

public interface InterfaceReposetoryElements {
    List<Element> getAllElements(int idSelectGroup);
    void dropGroupLink(Integer idElement, Integer idGroup);
    long addGroupLink(Integer idElement, Integer idGroup);
    long add(String name, String commet, Integer priority);
    void update(Integer id,String name, String comment, Integer priority);
    List<Integer> deleteElem(int dbId);

    }
