package com.example.random_menu.Reposetory;

import com.example.random_menu.Data.Element;
import com.example.random_menu.Data.Group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Singleton;

import dagger.hilt.android.lifecycle.HiltViewModel;

@Singleton
public class SharedDataReposetory implements InterfaceSharedDataReposetory {
        private Integer positionSelectElem;
        private Integer idSelectElem;
        private Group sharedGroup;
        private Element sharedElement;
        private Map<Integer, Integer> updateGroups = new HashMap<Integer,Integer>();
        private Random randomizer = new Random();
        private Group selectedGroup;

        @Override
        public void setSharedGroup(Group group) {
                this.sharedGroup = group;
        }

        @Override
        public Group getSharedGroup() {
                return this.sharedGroup;
        }

        @Override
        public void setSharedElement(Element element) {
                this.sharedElement = element;
        }

        @Override
        public Element getSharedElement() {
                return this.sharedElement;
        }

        @Override
        public void setUpdateGroups(Integer id, Integer update) {
                if(updateGroups.containsKey(id)){
                        updateGroups.put(id,updateGroups.get(id) + update);
                }
                else{
                        updateGroups.put(id,update);
                }
        }

        @Override
        public Map<Integer, Integer> getUpdateGroups() {
                return updateGroups;
        }


}
