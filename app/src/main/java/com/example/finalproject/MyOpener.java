package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyOpener extends SQLiteOpenHelper {
    protected final static String databaseName = "ImageDB";
    protected final static int versionNum = 1;
    public final static String tableName = "Images";
    public final static String COL_ID = "ImageId";
    public final static String COL_IMAGE = "Image";

    public MyOpener(Context context) {
        super(context, databaseName, null, versionNum);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + tableName + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_IMAGE + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + tableName);
        onCreate(sqLiteDatabase);
    }

    public String getTableName(){
        return tableName;
    }
}
