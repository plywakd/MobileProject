package com.pam.pam_redesign;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoDBService extends SQLiteOpenHelper {

    //  TODO this whole section looks good but needs to be checked, maybe more methods are needed?
    private static final String TAG = "DatabaseService";

    private static final String TABLE_NAME = "TodoTask_table";
    private static final String COL_1 = "todoTask_id"; //integer
    private static final String COL_2 = "done";   //there is no Boolean datatype instead Boolean values are stored as integers 0 (false) and 1 (true)
    private static final String COL_3 = "due_date";  //I think we should use text here
    private static final String COL_4 = "description";    //text
    private static final String COL_5 = "repetition"; //text

    public TodoDBService(Context context) {
        super(context, TABLE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (todoTask_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " INTEGER, " + COL_3 + " TEXT, " + COL_4 + " TEXT, " + COL_5 + " TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addData(int done, String date, String desc, String repetition) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, done);
        contentValues.put(COL_3, date);
        contentValues.put(COL_4, desc);
        contentValues.put(COL_5, repetition);
        Log.d(TAG, "addData: Adding" + COL_4 + " " + COL_3);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public Cursor getDataByDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE due_date=" + date;
        return db.rawQuery(query, null);
    }

    public void deleteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE todoTask_id=" + id;
        db.execSQL(query);
    }

    public void updateData(int id, int done, String date, String desc, String repetition) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("UPDATE %s SET done='%d', due_date='%s', description='%s', " +
                "repetition='%s' WHERE receipt_id=%d", TABLE_NAME, done, date, desc, repetition, id);
        db.execSQL(query);
    }
}
