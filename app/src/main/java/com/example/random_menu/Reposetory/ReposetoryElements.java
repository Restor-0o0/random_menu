package com.example.random_menu.Reposetory;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Element;
import com.example.random_menu.placeholder.ElemPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class ReposetoryElements implements InterfaceReposetoryElements {
    public List<Element> getAllElements(int idSelectGroup) {
        List<Element> elements = new ArrayList<>();
        Cursor cursor = ContentProviderDB.query(
                MainBaseContract.Elements.TABLE_NAME,
                null,
                MainBaseContract.Elements._ID + " IN (SELECT "+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " FROM "+ MainBaseContract.ElemGroup.TABLE_NAME+" WHERE "+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+"="+idSelectGroup+ ")",
                null,
                null,
                null,
                MainBaseContract.Elements._ID);
        cursor.moveToFirst();
        do{
            elements.add(new Element(
                    cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Elements._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_COMMENT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Elements.COLUMN_NAME_PRIORITY))
            ));
        }while(cursor.moveToNext());
        return  elements;
    }
    public List<Integer> deleteElem(int dbId) {
        try {
            List<Integer> ids = new ArrayList<>();
            Cursor cursor = ContentProviderDB.query(MainBaseContract.ElemGroup.TABLE_NAME,null,MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + "=" + String.valueOf(dbId),null,null,null,null);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                    do{
                        ids.add(cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP)));
                    }while(cursor.moveToNext());
            }
            ContentProviderDB.delete(
                    MainBaseContract.Components.TABLE_NAME,
                    MainBaseContract.Components.COLUMN_NAME_ELEMENT + " = " + String.valueOf(dbId),
                    null
            );
            ContentProviderDB.delete(
                    MainBaseContract.ElemGroup.TABLE_NAME,
                    MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " = " + String.valueOf(dbId),
                    null
            );
            ContentProviderDB.delete(
                    MainBaseContract.Elements.TABLE_NAME,
                    MainBaseContract.Elements._ID + " = " + String.valueOf(dbId),
                    null
            );

            return ids;
        }catch (Exception e){
            Log.e("DeleteGrouperror",e.toString());
            return new ArrayList<>();
        }
    }
    public void update(Integer id,String name, String comment, Integer priority){
        ContentValues contentValues = new ContentValues();
        int counter = 0;
        if(name != null){
            contentValues.put(MainBaseContract.Elements.COLUMN_NAME_NAME,name);
            counter += 1;
        }
        if(comment != null){
            contentValues.put(MainBaseContract.Elements.COLUMN_NAME_COMMENT,comment);
            counter += 1;
        }
        if(priority != null){
            contentValues.put(MainBaseContract.Elements.COLUMN_NAME_PRIORITY,priority);
            counter += 1;
        }
        if(counter > 0){
            ContentProviderDB.update(
                    MainBaseContract.Elements.TABLE_NAME,
                    contentValues,
                    MainBaseContract.Elements._ID + " = " + id,
                    null
            );
        }
    }
    public long add(String name, String commet, Integer priority){
        ContentValues cv = new ContentValues();
        cv.put(MainBaseContract.Elements.COLUMN_NAME_NAME, name);
        cv.put(MainBaseContract.Elements.COLUMN_NAME_COMMENT, commet);
        cv.put(MainBaseContract.Elements.COLUMN_NAME_PRIORITY,priority);
        return ContentProviderDB.insert(MainBaseContract.Elements.TABLE_NAME,null,cv);
    }
    public long addGroupLink(Integer idElement, Integer idGroup){
        ContentValues cv = new ContentValues();
        cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT,idElement);
        cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP, idGroup);
        return ContentProviderDB.insert(MainBaseContract.ElemGroup.TABLE_NAME,null,cv);
    }
    public void dropGroupLink(Integer idElement, Integer idGroup){
        ContentProviderDB.delete(MainBaseContract.ElemGroup.TABLE_NAME,MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = "+idGroup + " and "+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " = "+ idElement,null);
    }
}
