package com.example.random_menu.DI;

import com.example.random_menu.Reposetory.InterfaceReposetoryElements;
import com.example.random_menu.Reposetory.ReposetoryElements;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ElementsReposetoryModule {
    @Provides
    @Singleton
    public InterfaceReposetoryElements providerReposetory(){
        return new ReposetoryElements();
    }
}
