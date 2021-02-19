package com.iamscratches.scratchpad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

public class FreeDBManager {
    private SQLiteDatabase sqlDB;
    static final  String DBName = "FreePad";
    static final String TableName = "Notepads";
    static final String colFreepads = "freepads";
    static final String colContents = "contents";
    static final String colCreated = "created";
    static  final String colModified = "modified";
    static final int DBVersion = 1;
    static final String CreateTable = "Create table IF NOT EXISTS " + TableName
            + " (ID integer primary key autoincrement, "
            + colFreepads + " text Unique, "
            + colContents + " text, "
            + colCreated + " text, "
            + colModified + " text "
            + ");";
    static  class DatabaseHelper extends SQLiteOpenHelper {
        Context context;
        DatabaseHelper(Context context){
            super(context, DBName, null, DBVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CreateTable);
            }catch (Exception e){
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
            Toast.makeText(context, "Free Pad is ready to use", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop table if exists " + TableName);
            onCreate(db);
        }
    }

    public FreeDBManager(Context context){
        FreeDBManager.DatabaseHelper db = new FreeDBManager.DatabaseHelper(context);
        sqlDB = db.getWritableDatabase();
    }
    public Cursor getNotepads(){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TableName);
        String selectAll[] = {colFreepads};
        Cursor cursor = qb.query(sqlDB, selectAll, null, null, null, null, null);
        return  cursor;
    }
    public long Insert(ContentValues values){
        long ID = sqlDB.insert(TableName,"",values);// if fails to insert then ID = 0
        return ID;
    }
    public int Update(ContentValues values, String selection, String[] selectionArgs){
        int count = sqlDB.update(TableName, values, selection, selectionArgs);
        return  count;
    }
    public Cursor query(String[] projection, String selection, String[] selectionArgs) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TableName);
        Cursor cursor = qb.query(sqlDB, projection, selection, selectionArgs, null, null, null);
        return cursor;
    }

    public int Delete(String freepad){
        String selection = colFreepads + " like ?";
        String selectionArgs[] = {freepad};
        int count = sqlDB.delete(TableName, selection, selectionArgs);
        return count;
    }

}