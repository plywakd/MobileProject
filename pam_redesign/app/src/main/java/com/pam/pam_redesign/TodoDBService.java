package com.pam.pam_redesign;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoDBService extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseService";

    private static final String TABLE_NAME = "TodoTask_table";
    private static final String COL_1 = "todoTask_id"; //integer
    private static final String COL_2 = "done";   //there is no Boolean datatype instead Boolean values are stored as integers 0 (false) and 1 (true)
    private static final String COL_3 = "due_date";  //I think we should use text here
    private static final String COL_4 = "description";    //text
    private static final String COL_5 = "repetition"; //integer day value

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

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public boolean addData(int done, String date, String desc, Integer repetition) {
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
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE due_date= '" + date + "' ";
        return db.rawQuery(query, null);
    }

    public Cursor getDataByDone(Integer isDone, String orderByDateOption) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format(
                "SELECT * FROM %s WHERE done=%d ORDER BY due_date %s",
                TABLE_NAME, isDone, orderByDateOption
        );
        return db.rawQuery(query, null);
    }

    public Cursor getDataBetweenDateRange(String dateFrom, String dateTo) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format(
                "SELECT * FROM %s WHERE due_date BETWEEN '%s' AND '%s' ORDER BY due_date ASC",
                TABLE_NAME, dateFrom, dateTo
        );
        return db.rawQuery(query, null);
    }

    public void deleteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE todoTask_id=" + id;
        db.execSQL(query);
    }

    public void updateData(int id, int done, String date, String desc, Integer repetition) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("UPDATE %s SET done='%d', due_date='%s', description='%s', " +
                "repetition='%d' WHERE todoTask_id=%d", TABLE_NAME, done, date, desc, repetition, id);
        db.execSQL(query);
    }

    public Cursor getDataById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("SELECT * FROM %s WHERE todoTask_id=%d", TABLE_NAME, id);
        return db.rawQuery(query, null);
    }

    public Cursor getDataByParams(String date, String desc, Integer repetition) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format(
                "SELECT * FROM %s WHERE due_date='%s' AND description='%s' AND repetition='%d'",
                TABLE_NAME, date, desc, repetition
        );
        return db.rawQuery(query, null);
    }
}
