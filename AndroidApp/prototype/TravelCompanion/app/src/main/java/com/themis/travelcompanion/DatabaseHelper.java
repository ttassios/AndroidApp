package com.themis.travelcompanion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "travelcompdb.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USERS(user_id INTEGER PRIMARY KEY AUTOINCREMENT, fullname TEXT, username TEXT, password TEXT)");
        db.execSQL("CREATE TABLE SIGHTS(sight_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, comments TEXT, country TEXT, address TEXT, area TEXT, image TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS USERS");
        db.execSQL("DROP TABLE IF EXISTS SIGHTS");
    }

    public boolean Insert(String fullname, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullname", fullname);
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = db.insert("USERS", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean CheckUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE username=?", new String[]{username});
        if (cursor.getCount() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean CheckLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE username=? AND password=?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public long insertSight(String name, String comments, String country, String address, String area, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("comments", comments);
        values.put("country", country);
        values.put("address", address);
        values.put("area", area);
        values.put("image", image);

        long id = db.insert("SIGHTS", null, values);
        return id;
    }

    public void updateSight(String sight_id, String name, String comments, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("comments", comments);
        values.put("image", image);

        db.update("SIGHTS", values, "sight_id = ?",new String[]{sight_id});
    }




    public void deleteSight(String sight_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("SIGHTS", "sight_id = ?", new String[]{sight_id});
    }

    public ArrayList<UserModel> getAllData() {

        ArrayList<UserModel> arrayList = new ArrayList<>();

        String select_query = "SELECT * FROM SIGHTS";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        if (cursor.moveToNext()) {
            do {
                UserModel model = new UserModel();
                model.setSight_id(cursor.getInt(cursor.getColumnIndex("sight_id")));
                model.setName(cursor.getString(cursor.getColumnIndex("name")));
                model.setComments(cursor.getString(cursor.getColumnIndex("comments")));
                model.setCountry(cursor.getString(cursor.getColumnIndex("country")));
                model.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                model.setArea(cursor.getString(cursor.getColumnIndex("area")));
                model.setImage(cursor.getString(cursor.getColumnIndex("image")));
                arrayList.add(model);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

}
