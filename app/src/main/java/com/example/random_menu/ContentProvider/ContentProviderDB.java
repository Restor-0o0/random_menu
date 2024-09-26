package com.example.random_menu.ContentProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.random_menu.DB.DBController;

public class ContentProviderDB {


    public static final int DATABASE_VERRSION = 1;
    public static final String DATABASE_NAME = "RandDB.db";
    public static final String Authority = "content://com.example.random_menu.ContentProvider/";

    static DBController dbControl ;
    public static SQLiteDatabase DB ;

    public static void init(Context context){
        dbControl = new DBController(context);
        DB = dbControl.getWritableDatabase();
    }

    public static Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit){
        Cursor curs;
        curs = DB.query(table, columns, selection, selectionArgs,groupBy,having,orderBy,limit);
        return curs;
    }

    public static Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        Cursor curs;
        curs = DB.query(table, columns, selection, selectionArgs,groupBy,having,orderBy);
        return curs;
    }

    public static long insert(String table, String nullColumnHack, ContentValues values){
        return DB.insert(table, nullColumnHack, values);
    }
}
