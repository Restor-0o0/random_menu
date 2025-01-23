package com.example.random_menu.DI;

import com.example.random_menu.Reposetory.InterfaceReposetoryGroups;
import com.example.random_menu.Reposetory.ReposetoryGroups;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class GroupsReposetoryModule {
    @Provides
    @Singleton
    public InterfaceReposetoryGroups providerReposetory(){
        return new ReposetoryGroups();
    }
}
