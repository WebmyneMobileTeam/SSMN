package com.project.samplesmsapplication.ui;

import android.app.Application;

import com.project.samplesmsapplication.helpers.DatabaseHelper;

/**
 * Created by xitij on 06-03-2015.
 */
public class MyApplication extends Application{

    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        databaseHelper = new DatabaseHelper(this.getApplicationContext());

        try{
            databaseHelper.createDataBase();
        }catch(Exception e){
                e.printStackTrace();
        }

    }
}
