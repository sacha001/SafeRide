package com.example.ssilance.saferide;

/**
 * Created by Sacha on 2018-04-01.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MyDatabase";

    // Database table name
    private static final String TABLE_LIST = "destinations";
    private static final String RIDER_ADDRESSES = "addresses";


    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LIST_TABLE = "CREATE TABLE "+ TABLE_LIST + "(" + KEY_ID
                + " INTEGER, " + KEY_NAME + " TEXT, "+ KEY_ADDRESS +" TEXT)";

        String CREATE_RIDER_ADDRESSES = "CREATE TABLE "+ RIDER_ADDRESSES + "(" + KEY_ID
                + " INTEGER, " + KEY_ADDRESS + " TEXT)";

        db.execSQL(CREATE_LIST_TABLE);
        db.execSQL(CREATE_RIDER_ADDRESSES);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + RIDER_ADDRESSES);

        // Create tables again
        onCreate(db);
    }



    void addListItem(Map<String, String> listItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

            values.put(KEY_NAME, listItem.get("name"));
            values.put(KEY_ADDRESS, listItem.get("address"));
            db.insert(TABLE_LIST, null, values);

        db.close(); // Closing database connection
    }

    void addRiderAddress(String listItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ADDRESS, listItem);

        db.insert(RIDER_ADDRESSES, null, values);

        db.close(); // Closing database connection
    }
    ArrayList<String> getRiderAddressesList(){
        String selectQuery = "SELECT  * FROM " + RIDER_ADDRESSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<String>();
        while(cursor.moveToNext()) {

            list.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
        return list;
    }

    ArrayList<Map<String, String>> getList(){
        String selectQuery = "SELECT  * FROM " + TABLE_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        while(cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put("name", cursor.getString(1));
            map.put("address", cursor.getString(2));
            list.add(map);
        }
        cursor.close();
        db.close();
        return list;
    }

    void deleteRiderAddressItem(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+ RIDER_ADDRESSES + " WHERE "+ KEY_ADDRESS +" = ?";
        db.execSQL(query , new String[]{item});
        db.close();
    }

    void deleteItem(Map<String, String> item){
        SQLiteDatabase db = this.getWritableDatabase();
        String name = item.get("name");
        String address = item.get("address");
        String query = "DELETE FROM "+ TABLE_LIST + " WHERE "+ KEY_NAME + " = ? AND "+ KEY_ADDRESS +" = ?";
        db.execSQL(query , new String[]{name, address});
        db.close();
    }

}
