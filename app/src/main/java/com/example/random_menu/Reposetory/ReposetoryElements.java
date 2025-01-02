package com.example.random_menu.Reposetory;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.placeholder.ElemPlaceholderContent;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

public class ReposetoryElements {
    public static Cursor getAllElements(int idSelectGroup) {
        return ContentProviderDB.query(
                MainBaseContract.Elements.TABLE_NAME,
                null,
                MainBaseContract.Elements._ID + " IN (SELECT "+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " FROM "+ MainBaseContract.ElemGroup.TABLE_NAME+" WHERE "+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+"="+idSelectGroup+ ")",
                null,
                null,
                null,
                MainBaseContract.Elements._ID);

    }
    public static void deleteElem(int dbId) {
        try {
            Cursor cursor = ContentProviderDB.query(MainBaseContract.ElemGroup.TABLE_NAME,null,MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + "=" + String.valueOf(dbId),null,null,null,null);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                synchronized (GroupPlaceholderContent.GROUPS){
                    do{
                        GroupPlaceholderContent.deleteElement(cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP)));
                    }while(cursor.moveToNext());
                }
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


        }catch (Exception e){
            Log.e("DeleteGrouperror",e.toString());
        }
    }
    public static void update(Integer id,String name, String comment, Integer priority){
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
    public static long add(String name, String commet, Integer priority){
        ContentValues cv = new ContentValues();
        cv.put(MainBaseContract.Elements.COLUMN_NAME_NAME, name);
        cv.put(MainBaseContract.Elements.COLUMN_NAME_COMMENT, commet);
        cv.put(MainBaseContract.Elements.COLUMN_NAME_PRIORITY,priority);
        return ContentProviderDB.insert(MainBaseContract.Elements.TABLE_NAME,null,cv);
    }
    public static long addGroupLink(Integer idElement, Integer idGroup){
        ContentValues cv = new ContentValues();
        cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT,idElement);
        cv.put(MainBaseContract.ElemGroup.COLUMN_NAME_GROUP, idGroup);
        return ContentProviderDB.insert(MainBaseContract.ElemGroup.TABLE_NAME,null,cv);
    }
    public static void dropGroupLink(Integer idElement, Integer idGroup){
        ContentProviderDB.delete(MainBaseContract.ElemGroup.TABLE_NAME,MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = "+idGroup + " and "+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " = "+ idElement,null);
    }
}
