package com.project.samplesmsapplication.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.samplesmsapplication.model.Category;
import com.project.samplesmsapplication.model.Msg;

import junit.framework.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by xitij on 06-03-2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static String TAG = "DATABASE_WRAPPER";
    private  String DB_PATH = "/data/data/com.project.samplesmsapplication/databases/";
    private static String DB_NAME = "msjcom.db";
    private SQLiteDatabase myDataBase = null;
    private Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME,null,1);
        this.myContext = context;
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if(dbExist){
//            Log.v("log_tag", "database does exist");
        }else{
//            Log.v("log_tag", "database does not exist");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkDataBase(){

        File folder = new File(DB_PATH);
        if(!folder.exists()){
            folder.mkdir();
        }
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        myDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return myDataBase != null;

    }
    @Override
    public synchronized void close()
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Category> getAllCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        myDataBase = this.getWritableDatabase();

        //  String wher = "role_name_" + " = ? AND " + "main_line_type_" + " = ?";
        // String[] FIELDS = { "cast_matches_" };

        String table = new String();
        int selectedLanguage = Prefs.with(myContext).getInt("selected_language", 0);
        if(selectedLanguage == 0){
            table = "MSG_CAT_ENG";
        }else{
            table = "MSG_CAT";
        }

        Cursor cursor =   myDataBase.query(table, null, null, null, null, null, null);
        String castMatchesString = null;
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category._id = cursor.getInt(cursor.getColumnIndex("ID"));
                category.category_name = cursor.getString(cursor.getColumnIndex("NAME"));
                category.count = getProfilesCount(cursor.getString(cursor.getColumnIndex("ID")));
                categories.add(category);

            } while (cursor.moveToNext());
        }

        return categories;

    }

    public void setFavourite(int msg_id,boolean what){

        myDataBase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        if(what == true){
            cv.put("isFavourite",1);
        }else {
            cv.put("isFavourite",0);
        }
        String wher = "ID" + " = ?";
        String table = "";
        if(PrefUtils.isEnglish(myContext)){
            table = "MESSAGES_ENG";
        }else{
            table = "MESSAGES";
        }

        myDataBase.update(table,cv,wher,new String[]{"" + msg_id});


    }

    public ArrayList<Msg> getSearchResult(int cat,String text){

        text = "%" + text + "%";

        myDataBase = this.getWritableDatabase();
        ArrayList<Msg> messages = new ArrayList<>();
        String table = new String();
        int selectedLanguage = Prefs.with(myContext).getInt("selected_language",0);
        if(selectedLanguage == 0){
            table = "MESSAGES_ENG";
        }else{
            table = "MESSAGES";
        }

        String selectQuery = " select * from "+table+" where MESSAGE like  '"
                + text
                + "' and MSG_CAT like "
                + cat;

        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                Msg category = new Msg();
                category._id = cursor.getInt(cursor.getColumnIndex("ID"));
                category.cat_id = cursor.getInt(cursor.getColumnIndex("MSG_CAT"));
                category.msg_text = cursor.getString(cursor.getColumnIndex("MESSAGE"));
                int bool = cursor.getInt(cursor.getColumnIndex("isFavourite"));
                if(bool == 0){
                    category.isFavourite = false;
                }else{
                    category.isFavourite = true;
                }

                messages.add(category);
            } while (cursor.moveToNext());
        }

        return messages;
    }

    public ArrayList<Msg> getFavouriteMessages(){

        ArrayList<Msg> categories = new ArrayList<>();
        myDataBase = this.getWritableDatabase();

        String wher = "isFavourite" + " = ?";
        // String[] FIELDS = { "cast_matches_" };

        String table = new String();
        int selectedLanguage = Prefs.with(myContext).getInt("selected_language",0);
        if(selectedLanguage == 0){
            table = "MESSAGES_ENG";
        }else{
            table = "MESSAGES";
        }

        Cursor cursor = myDataBase.query(table, null, wher, new String[]{"" + 1}, null, null, null);
        String castMatchesString = null;
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                Msg category = new Msg();
                category._id = cursor.getInt(cursor.getColumnIndex("ID"));
                category.cat_id = cursor.getInt(cursor.getColumnIndex("MSG_CAT"));
                category.msg_text = cursor.getString(cursor.getColumnIndex("MESSAGE"));
                int bool = cursor.getInt(cursor.getColumnIndex("isFavourite"));
                if(bool == 0){
                    category.isFavourite = false;
                }else{
                    category.isFavourite = true;
                }

                categories.add(category);
            } while (cursor.moveToNext());
        }

        return categories;
    }

    public ArrayList<Msg> getMessages(int cat_id){

        ArrayList<Msg> categories = new ArrayList<>();
        myDataBase = this.getWritableDatabase();

        String wher = "MSG_CAT" + " = ?";
        // String[] FIELDS = { "cast_matches_" };

        String table = new String();
        int selectedLanguage = Prefs.with(myContext).getInt("selected_language",0);
        if(selectedLanguage == 0){
            table = "MESSAGES_ENG";
        }else{
            table = "MESSAGES";
        }

        Cursor cursor = myDataBase.query(table, null, wher, new String[]{"" + cat_id}, null, null, null);
        String castMatchesString = null;
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                Msg category = new Msg();
                category._id = cursor.getInt(cursor.getColumnIndex("ID"));
                category.cat_id = cursor.getInt(cursor.getColumnIndex("MSG_CAT"));
                category.msg_text = cursor.getString(cursor.getColumnIndex("MESSAGE"));

                int bool = cursor.getInt(cursor.getColumnIndex("isFavourite"));
                if(bool == 0){
                    category.isFavourite = false;
                }else{
                    category.isFavourite = true;
                }

                categories.add(category);
            } while (cursor.moveToNext());
        }

        return categories;
    }


    public int getProfilesCount(String category) {


        String table = new String();
        int selectedLanguage = Prefs.with(myContext).getInt("selected_language",0);
        if(selectedLanguage == 0){
            table = "MESSAGES_ENG";
        }else{
            table = "MESSAGES";
        }





        String countQuery = "SELECT  * FROM " + table+" where MSG_CAT = "+category;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();

        return cnt;
    }

}
