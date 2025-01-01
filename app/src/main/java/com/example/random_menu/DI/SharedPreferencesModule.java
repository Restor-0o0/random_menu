package com.example.random_menu.DI;


import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;


@Module
@InstallIn(SingletonComponent.class)
public class SharedPreferencesModule {

    @Provides
    @Singleton
    public SharedPreferences getSharedPreferences(@ApplicationContext Context context){
        return context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
    }

}
