package com.example.random_menu.DI;


import com.example.random_menu.Reposetory.InterfaceReposetoryComponents;
import com.example.random_menu.Reposetory.InterfaceReposetoryElements;
import com.example.random_menu.Reposetory.InterfaceReposetoryGroups;
import com.example.random_menu.Utils.XMLUtils.InterfaceXmlManager;
import com.example.random_menu.Utils.XMLUtils.XmlManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class XmlManagerModule {

    @Provides
    @Singleton
    public InterfaceXmlManager provideXmlManage(
            InterfaceReposetoryGroups interfaceReposetoryGroups,
            InterfaceReposetoryElements interfaceReposetoryElements,
            InterfaceReposetoryComponents interfaceReposetoryComponents
    ){
        return new XmlManager(
                interfaceReposetoryGroups,
                interfaceReposetoryElements,
                interfaceReposetoryComponents
        );
    }
}
