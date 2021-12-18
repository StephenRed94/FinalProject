package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    DatePicker datePicker;
    String date;
    String url;
    String day;
    String month;
    String year;
    String apiUrl;
    MyOpener opener;
    SQLiteDatabase sqLiteDatabase;
    Cursor results;
    String imageUrl;
    long id;
    String imgUrl;
    String spacePic;
    SpaceImage image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView picDate = findViewById(R.id.pic_date);
        datePicker = findViewById(R.id.datePicker);
        ContentValues newRowValues = new ContentValues();
        ((Button) findViewById(R.id.select_button)).setOnClickListener(clk -> {
            year = String.valueOf(datePicker.getYear());
            month = String.valueOf(datePicker.getMonth() + 1);
            day = String.valueOf(datePicker.getDayOfMonth());
            if (Integer.parseInt(month) < 10) {
                month = "0" + month;
            }
            if (Integer.parseInt(day) < 10) {
                day = "0" + day;
            }
            date = year + "-" + month + "-" + day;
            picDate.setText(date);
            url = "https://api.nasa.gov/planetary/apod?api_key=qKyY93zeVGr6DArDM6MlBy4UWYfVYZD0k7sct7SH&date=" + date;
            NASAQuery query = new NASAQuery();
            query.execute(url);
        });
        ((Button) findViewById(R.id.save_pic_button)).setOnClickListener(clk -> {
            imageUrl = ((TextView) findViewById(R.id.pic_url)).getText().toString();
            image = new SpaceImage(id, imgUrl);
            image.setImgUrl(imageUrl);
            newRowValues.put(MyOpener.COL_IMAGE, image.getImgUrl());
            opener = new MyOpener(this);
            sqLiteDatabase = opener.getWritableDatabase();
            id = sqLiteDatabase.insert(MyOpener.tableName, null, newRowValues);
            Toast.makeText(this, "Picture Saved", Toast.LENGTH_SHORT).show();
        });


    }


    private class NASAQuery extends AsyncTask<String, Integer, String> {
        ImageView imageView;
        Bitmap bm2;
        String picUrl;
        TextView picUrlOut;
        @Override
        protected String doInBackground(String... strings) {
            Bitmap image = null;
            String spacePic = "SpacePic.png";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream stream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null){
                    builder.append(line + "\n");
                }
                String result = builder.toString();
                JSONObject picObject = new JSONObject(result);
                picUrl = picObject.getString("url");
                Log.i("this", "The pic url is " + picUrl);
                URL picUrl2 = new URL(picUrl);
                HttpURLConnection url2Connection = (HttpURLConnection) picUrl2.openConnection();
                int responseCode = url2Connection.getResponseCode();
                url2Connection.connect();
                if(responseCode == 200){
                    if(fileExistance(spacePic)){
                        FileInputStream fis = null;
                        try {
                            deleteFile(spacePic);
                            image = BitmapFactory.decodeStream(url2Connection.getInputStream());
                            FileOutputStream outputStream = openFileOutput(spacePic, Context.MODE_PRIVATE);
                            image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i("File downloaded ", spacePic);
                            //fis = openFileInput(spacePic);
                            //Log.i("File found locally", spacePic);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Bitmap bm = BitmapFactory.decodeStream(fis);
                    }
                    else {
                        image = BitmapFactory.decodeStream(url2Connection.getInputStream());
                        FileOutputStream outputStream = openFileOutput(spacePic, Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        Log.i("File downloaded ", spacePic);
                    }
                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return picUrl;
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
        @Override
        public void onPostExecute(String doInBackground){
            bm2 = BitmapFactory.decodeFile("/data/data/com.example.finalproject/files/SpacePic.png");
            imageView = findViewById(R.id.space_pic);
            picUrlOut = findViewById(R.id.pic_url);
            imageView.setImageBitmap(bm2);
            picUrlOut.setText(picUrl);
        }
    }
}

