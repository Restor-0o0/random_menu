package com.example.random_menu.Reposetory;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.ArrayList;
import java.util.List;

public class ReposetoryGroups {



    public static void deleteGroupAndNoHavingMoreLinksElements(int dbId){
        try {
            Cursor curr;
            curr = ContentProviderDB.query(
                    MainBaseContract.ElemGroup.TABLE_NAME,
                    null,
                    MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = " + dbId + " and " + MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " NOT IN (SELECT " + MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " FROM " + MainBaseContract.ElemGroup.TABLE_NAME + " WHERE " + MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " != " + dbId + " and " + MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " IN (SELECT " + MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " FROM " + MainBaseContract.ElemGroup.TABLE_NAME + " WHERE " + MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = " + dbId + "))",
                    null,
                    null,
                    null,
                    null
            );
            List<String> ids = new ArrayList<String>();

            if (curr.getCount() > 0) {
                curr.moveToFirst();
                do {
                    ids.add(curr.getString(curr.getColumnIndexOrThrow(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT)));
                    //Log.e("coooon", "_" + curr.getString(curr.getColumnIndexOrThrow(MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT)));
                } while (curr.moveToNext());
            }
            ContentProviderDB.delete(
                    MainBaseContract.ElemGroup.TABLE_NAME,
                    MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " = " + String.valueOf(dbId),
                    null
            );

            if (ids.size() > 0) {
                for (int i = 0; i < ids.size(); i++) {
                   // Log.e("coooon", "_" + ids.get(i));
                    ContentProviderDB.delete(
                            MainBaseContract.Components.TABLE_NAME,
                            MainBaseContract.Components.COLUMN_NAME_ELEMENT + " = " + ids.get(i),
                            null
                    );
                    ContentProviderDB.delete(
                            MainBaseContract.Elements.TABLE_NAME,
                            MainBaseContract.Elements._ID + " = " + ids.get(i),
                            null
                    );
                }

            }
            ContentProviderDB.delete(
                    MainBaseContract.Groups.TABLE_NAME,
                    MainBaseContract.Groups._ID + " = " + String.valueOf(dbId),
                    null
            );


        }catch (Exception e){
            Log.e("DeleteGrouperror",e.toString());
        }
    }
    public static Cursor getAll(){
        return ContentProviderDB.query(MainBaseContract.Groups.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MainBaseContract.Groups.COLUMN_NAME_PRIORITY);

    }
    public static void update(Integer id,String name, String comment, Integer priority, Integer countElements){
        ContentValues contentValues = new ContentValues();
        int counter = 0;
        if(name != null){
            contentValues.put(MainBaseContract.Groups.COLUMN_NAME_NAME,name);
            counter += 1;
        }
        if(comment != null){
            contentValues.put(MainBaseContract.Groups.COLUMN_NAME_COMMENT,comment);
            counter += 1;
        }
        if(priority != null){
            contentValues.put(MainBaseContract.Groups.COLUMN_NAME_PRIORITY,priority);
            counter += 1;
        }
        if(countElements != null){
            contentValues.put(MainBaseContract.Groups.COLUMN_NAME_COUNT_ELEMS,countElements);
            counter += 1;
        }
        if(counter > 0){
            ContentProviderDB.update(
                    MainBaseContract.Groups.TABLE_NAME,
                    contentValues,
                    MainBaseContract.Groups._ID + " = " + id,
                    null
            );
        }
    }
    public static void add(String name, String commet){
        ContentValues cv = new ContentValues();
        cv.put(MainBaseContract.Groups.COLUMN_NAME_NAME, name);
        cv.put(MainBaseContract.Groups.COLUMN_NAME_COMMENT, commet);
        cv.put(MainBaseContract.Groups.COLUMN_NAME_PRIORITY,String.valueOf(GroupPlaceholderContent.maxPriority + 1));
        ContentProviderDB.insert(MainBaseContract.Groups.TABLE_NAME,null,cv);
    }
}
