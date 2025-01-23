package com.example.random_menu.DI;


import com.example.random_menu.Reposetory.InterfaceSharedDataReposetory;
import com.example.random_menu.Reposetory.SharedDataReposetory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class SharedDataReposetoryModule {

    @Provides
    @Singleton
    public InterfaceSharedDataReposetory proviewSharedDataReposetory(
    ){
        return new SharedDataReposetory();
    }
}
