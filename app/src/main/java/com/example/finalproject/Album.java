package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

public class Album extends AppCompatActivity {
    MyOpener opener;
    SQLiteDatabase sqLiteDatabase;
    Cursor results;
    ArrayList<Long> id = new ArrayList<Long>();
    ArrayList<String> images = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        loadData();
    }
    private void loadData(){
        opener = new MyOpener(this);
        sqLiteDatabase = opener.getWritableDatabase();
        String[] columns = {MyOpener.COL_ID, MyOpener.COL_IMAGE};
        results = sqLiteDatabase.query(false, MyOpener.tableName, columns, null, null, null, null, null, null);
        int idIndex = results.getColumnIndex(MyOpener.COL_ID);
        int imageIndex = results.getColumnIndex(MyOpener.COL_IMAGE);
        while (results.moveToNext()) {
            id.add(results.getLong(idIndex));
            images.add(results.getString(imageIndex));
        }
    }
    private void testData(){
        for(int x = 0; x < images.size(); x++){
            Log.i("this", images.get(x));
        }
    }
}