package com.example.random_menu.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBController extends SQLiteOpenHelper {

    public static final int DATABASE_VERRSION = 1;
    public static final String DATABASE_NAME = "RandDB.db";
    private static final String SQL_CREATE_TEST_Groups =
            "CREATE TABLE IF NOT EXISTS " + MainBaseContract.Groups.TABLE_NAME +
                    "("+
                    MainBaseContract.Groups._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MainBaseContract.Groups.COLUMN_NAME_NAME + " TEXT," +
                    MainBaseContract.Groups.COLUMN_NAME_COMMENT + " TEXT," +
                    MainBaseContract.Groups.COLUMN_NAME_PRIORITY + " INTEGER" +
                    "); ";
    private static final String SQL_CREATE_TEST_Elements =
            "CREATE TABLE IF NOT EXISTS "  +  MainBaseContract.Elements.TABLE_NAME +
                    "(" +
                    MainBaseContract.Elements._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MainBaseContract.Elements.COLUMN_NAME_NAME + " TEXT," +
                    MainBaseContract.Elements.COLUMN_NAME_COMMENT + " TEXT," +
                    MainBaseContract.Elements.COLUMN_NAME_PRIORITY + " INTEGER" +
                    "); ";
    private static final String SQL_CREATE_TEST_Components =
            "CREATE TABLE IF NOT EXISTS " + MainBaseContract.Components.TABLE_NAME +
                    "(" +
                    MainBaseContract.Components._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MainBaseContract.Components.COLUMN_NAME_ELEMENT + " INTEGER," +
                    MainBaseContract.Components.COLUMN_NAME_NAME + " TEXT," +
                    MainBaseContract.Components.COLUMN_NAME_COMMENT + " TEXT," +
                    "FOREIGN KEY(" + MainBaseContract.Components.COLUMN_NAME_ELEMENT +") " + "REFERENCES " + MainBaseContract.Elements.TABLE_NAME + "(" + MainBaseContract.Elements._ID + ")" +
                    ");";
    private static final String SQL_CREATE_TEST_ElemGroup =
            "CREATE TABLE IF NOT EXISTS " + MainBaseContract.ElemGroup.TABLE_NAME +
                    "(" +
                    MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + " INTEGER," +
                    MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + " INTEGER," +
                    "PRIMARY KEY (" + MainBaseContract.ElemGroup.COLUMN_NAME_GROUP + ","+ MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT + "),"+
                    "FOREIGN KEY(" + MainBaseContract.ElemGroup.COLUMN_NAME_ELEMENT +") " + "REFERENCES " + MainBaseContract.Elements.TABLE_NAME + "(" + MainBaseContract.Elements._ID + ")," +
                    "FOREIGN KEY(" + MainBaseContract.ElemGroup.COLUMN_NAME_GROUP +") " + "REFERENCES " + MainBaseContract.Groups.TABLE_NAME + "(" + MainBaseContract.Groups._ID + ")" +
                    ");";

    private static final String SQL_INSERT_TEST =
            "INSERT INTO " +
                    MainBaseContract.Elements.TABLE_NAME +
                    "(" +
                        MainBaseContract.Elements.COLUMN_NAME_NAME + "," +
                        MainBaseContract.Elements.COLUMN_NAME_COMMENT +
                    ") VALUES (" +
                        "testElem," + "test" +
                    ");" +
            "INSERT INTO " +
                    MainBaseContract.Components.TABLE_NAME +
                    "(" +
                    MainBaseContract.Components.COLUMN_NAME_ELEMENT + "," +
                    MainBaseContract.Components.COLUMN_NAME_NAME + "," +
                    MainBaseContract.Components.COLUMN_NAME_COMMENT +
                    ") VALUES (" +
                        "1," + "testComp," + "testComm" +
                    ");" +
            "INSERT INTO " +
                    MainBaseContract.Groups.TABLE_NAME +
                    "(" +
                    MainBaseContract.Groups.COLUMN_NAME_NAME+ "," +
                    MainBaseContract.Groups.COLUMN_NAME_COMMENT + "," +
                    MainBaseContract.Groups.COLUMN_NAME_PRIORITY +
                    ") VALUES (" +
                        "testGr," + "testComm," + "1"+
                    ");";
    /*private static final String priority_groups_trigger = "CREATE TRIGGER " + MainBaseContract.Groups.TRIGGER_NAME_PRIORITY +
            " BEFORE " +
            "ON " + MainBaseContract.Groups.TABLE_NAME +
            "BEGIN " +
            "(SELECT MAX(" + MainBaseContract.Groups.COLUMN_NAME_PRIORITY + ") FROM " + MainBaseContract.Groups.TABLE_NAME + ")" +
            "END";*/

    public DBController(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERRSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("Fuck", "in");
        try {
            sqLiteDatabase.execSQL(SQL_CREATE_TEST_Groups);
            sqLiteDatabase.execSQL(SQL_CREATE_TEST_Elements);
            sqLiteDatabase.execSQL(SQL_CREATE_TEST_Components);
            sqLiteDatabase.execSQL(SQL_CREATE_TEST_ElemGroup);
        }
        catch (Exception e){
            Log.e("Fuck", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    
}
