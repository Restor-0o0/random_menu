package com.example.random_menu.Reposetory;

import com.example.random_menu.Data.Element;
import com.example.random_menu.Data.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public interface InterfaceSharedDataReposetory {


    void setSharedGroup(Group group);
    Group getSharedGroup();
    void setSharedElement(Element element);
    Element getSharedElement();
    void setUpdateGroups(Integer id,Integer update);
    Map<Integer, Integer> getUpdateGroups();
}
