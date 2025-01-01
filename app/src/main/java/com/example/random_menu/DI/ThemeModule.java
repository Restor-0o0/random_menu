package com.example.random_menu.DI;

import com.example.random_menu.Utils.ThemeManager;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ThemeModule {

    /*@Provides
    public static ThemeManager getThemeManager(){
        return new ThemeManager();
    }*/
}
