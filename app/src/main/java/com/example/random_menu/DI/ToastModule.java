package com.example.random_menu.DI;

import android.content.Context;

import com.example.random_menu.Utils.ToastHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ToastModule {

    @Provides
    @Singleton
    public ToastHelper provideToastManager(@ApplicationContext Context context) {
        return new ToastHelper(context);
    }
}
