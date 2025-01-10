package com.example.random_menu.Utils;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class ToastHelper {
    private Context context;
    private Toast toast;

    @Inject
    public ToastHelper(@ApplicationContext Context context){
        this.context = context;
    }

    public void showMessage(String message){
        cancelToast();
        this.toast = Toast.makeText(this.context,message,Toast.LENGTH_SHORT);
        this.toast.show();
    }

    public void cancelToast(){
        if(this.toast != null){
            this.toast.cancel();
            this.toast = null;
        }
    }

}
