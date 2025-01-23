package com.example.random_menu.Reposetory;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.Data.Group;
import com.example.random_menu.placeholder.GroupPlaceholderContent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class ReposetoryGroups implements InterfaceReposetoryGroups {


    @Override
    public void deleteGroupAndNoHavingMoreLinksElements(int dbId){
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
    @Override
    public List<Group> getAll(){
        List<Group> groups = new ArrayList<>();
        Cursor cursor = ContentProviderDB.query(MainBaseContract.Groups.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MainBaseContract.Groups.COLUMN_NAME_PRIORITY);
        cursor.moveToFirst();
        do{
            groups.add(new Group(
                    cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Groups._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_COMMENT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_PRIORITY)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_COUNT_ELEMS))
            ));
            Log.e("LoadAllGroups",
                    String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_PRIORITY)))
                    + " " + String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(MainBaseContract.Groups.COLUMN_NAME_COUNT_ELEMS)))
            );
        }while(cursor.moveToNext());
        return groups;
    }
    @Override
    public void update(Integer id,String name, String comment, Integer priority, Integer countElements){
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
    @Override
    public long add(String name, String commet,Integer maxPriority){
        ContentValues cv = new ContentValues();
        cv.put(MainBaseContract.Groups.COLUMN_NAME_NAME, name);
        cv.put(MainBaseContract.Groups.COLUMN_NAME_COMMENT, commet);
        cv.put(MainBaseContract.Groups.COLUMN_NAME_PRIORITY,(maxPriority + 1));
        return ContentProviderDB.insert(MainBaseContract.Groups.TABLE_NAME,null,cv);
    }
}
