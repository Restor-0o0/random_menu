package com.example.random_menu.Reposetory;

import android.database.Cursor;

import com.example.random_menu.ContentProvider.ContentProviderDB;
import com.example.random_menu.DB.MainBaseContract;
import com.example.random_menu.placeholder.ComponentPlaceholderContent;

import java.util.List;

public class ReposetoryComponents {
    public static void deleteComponent(Integer idComponent) {
        ContentProviderDB.delete(MainBaseContract.Components.TABLE_NAME,MainBaseContract.Components._ID + " = " + idComponent,null);

    }
    public static void UpdatedGroupsDB(Integer idElement) {

    }
    public static Cursor loadElementData(Integer idSelectElem) {
        return ContentProviderDB.query(MainBaseContract.Elements.TABLE_NAME, null, MainBaseContract.Elements._ID + "=" + idSelectElem, null, null, null, null);
    }
    public static Cursor loadComponentsData(Integer idSelectElem) {
        return ContentProviderDB.query(MainBaseContract.Components.TABLE_NAME, null, MainBaseContract.Components.COLUMN_NAME_ELEMENT + "=" + idSelectElem, null, null, null, MainBaseContract.Components.COLUMN_NAME_QUANTITY + " DESC");
    }
    public static Cursor loadGroupsData(Integer idSelectElem) {
        return ContentProviderDB.query(MainBaseContract.Groups.TABLE_NAME,
                new String[]{MainBaseContract.Groups._ID,
                        MainBaseContract.Groups.COLUMN_NAME_NAME,
/*волшебная строка*/            "(SELECT COUNT("+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+") FROM "+MainBaseContract.ElemGroup.TABLE_NAME+" WHERE "+MainBaseContract.ElemGroup.COLUMN_NAME_GROUP+"="+MainBaseContract.Groups._ID+" and "+MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT+" = "+idSelectElem+" ) AS Active"},
                null ,
                null,
                null,
                null,
                MainBaseContract.Groups.COLUMN_NAME_PRIORITY);
    }
}
