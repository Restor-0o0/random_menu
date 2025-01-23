package com.example.random_menu.Reposetory;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Component;
import com.example.random_menu.Data.Element;
import com.example.random_menu.Data.Group;
import com.example.random_menu.Data.GroupCheck;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class ReposetoryComponents implements InterfaceReposetoryComponents {

    public long addComponent(Integer idElement,String name, String comment, String quantity){
        ContentValues cv = new ContentValues();
        cv.put(MainBaseContract.Components.COLUMN_NAME_NAME, name);
        cv.put(MainBaseContract.Components.COLUMN_NAME_COMMENT, comment);
        cv.put(MainBaseContract.Components.COLUMN_NAME_QUANTITY, quantity);
        cv.put(MainBaseContract.Components.COLUMN_NAME_ELEMENT, idElement);
        return ContentProviderDB.insert(MainBaseContract.Components.TABLE_NAME, null, cv);
    }
    public void updateComponent(Integer id,String name,String comment, String quantity){
        ContentValues contentValues = new ContentValues();
        int counter = 0;
        if(name != null){
            contentValues.put(MainBaseContract.Components.COLUMN_NAME_NAME,name);
            counter += 1;
        }
        if(comment != null){
            contentValues.put(MainBaseContract.Components.COLUMN_NAME_COMMENT,comment);
            counter += 1;
        }
        if(quantity != null){
            contentValues.put(MainBaseContract.Components.COLUMN_NAME_QUANTITY,quantity);
            counter += 1;
        }
        if(counter > 0){
            ContentProviderDB.update(
                    MainBaseContract.Components.TABLE_NAME,
                    contentValues,
                    MainBaseContract.Elements._ID + " = " + id,
                    null
            );
        }
    }
    public void deleteComponent(Integer idComponent) {
        ContentProviderDB.delete(MainBaseContract.Components.TABLE_NAME,MainBaseContract.Components._ID + " = " + idComponent,null);

    }
    public void UpdatedGroupsDB(Integer idElement) {

    }
    public Element loadElementData(Integer idSelectElem) {
        Cursor cursor =  ContentProviderDB.query(
                MainBaseContract.Elements.TABLE_NAME,
                null,
                MainBaseContract.Elements._ID + "=" + idSelectElem,
                null,
                null,
                null,
                null
        );
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return new Element(
                    cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Elements._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_COMMENT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_PRIORITY))
                    );
        }
        return null;
    }
    public List<Component> loadComponentsData(Integer idSelectElem) {
        List<Component> components = new ArrayList<>();
        Cursor cursor = ContentProviderDB.query(
                MainBaseContract.Components.TABLE_NAME,
                null,
                MainBaseContract.Components.COLUMN_NAME_ELEMENT + "=" + idSelectElem,
                null,
                null,
                null,
                null);
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                components.add(new Component(
                        cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Components._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Components.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Components.COLUMN_NAME_COMMENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Components.COLUMN_NAME_QUANTITY))
                ));
            } while (cursor.moveToNext());
            return components;
        }
        return new ArrayList<>();
    }
    public List<GroupCheck> loadGroupsData(Integer idSelectElem) {
        List<GroupCheck> groups = new ArrayList<>();
        Cursor cursor = ContentProviderDB.query(MainBaseContract.Groups.TABLE_NAME,
                new String[]{MainBaseContract.Groups._ID,
                        MainBaseContract.Groups.COLUMN_NAME_NAME,
/*волшебная строка*/            "(SELECT COUNT("+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+") FROM "+MainBaseContract.ElemGroup.TABLE_NAME+" WHERE "+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+"="+MainBaseContract.Groups._ID+" and "+MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT+" = "+idSelectElem+" ) AS Active"},
                null ,
                null,
                null,
                null,
                MainBaseContract.Groups.COLUMN_NAME_PRIORITY);
        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            do {
                Log.e("LoadGroupsInfo",cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_NAME))+ " " +cursor.getString(cursor.getColumnIndexOrThrow("Active")));
                groups.add(new GroupCheck(
                        cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Groups._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow("Active"))
                ));
            } while (cursor.moveToNext());
            return groups;
        }
        return new ArrayList<>();
    }
}
