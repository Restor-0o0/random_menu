package com.example.random_menu.DI;

import com.example.random_menu.Reposetory.InterfaceReposetoryComponents;
import com.example.random_menu.Reposetory.ReposetoryComponents;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ComponentsReposetoryModule {
    @Provides
    @Singleton
    public InterfaceReposetoryComponents providerReposetory(){
        return new ReposetoryComponents();
    }
}
