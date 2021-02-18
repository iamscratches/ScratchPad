package com.iamscratches.scratchpad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

import java.util.Date;

public class DBManager {
    private SQLiteDatabase sqlDB;
    static final  String DBName = "ScratchPad";
    static final String TName = "Notepads";
    static final String tableName = "tableName";
    static final String DateTime = " dateTime text ";
    static  final String Item = " item text ";
    static final String Quantity = " quantity text ";
    static final String Amount = " amount real ";
    static final int DBVersion = 1;
    static Context context;

    static  class DatabaseHelper extends SQLiteOpenHelper{
        Context context;
        DatabaseHelper(Context context){
            super(context, DBName, null, DBVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("Create table IF NOT EXISTS " + TName + "(ID integer primary key autoincrement, "
                    + tableName + " text Unique);");
            Toast.makeText(context, "Note Pad is ready to use", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    public  DBManager(Context context){
        DatabaseHelper db = new DatabaseHelper(context);
        sqlDB = db.getWritableDatabase();
        this.context = context;
    }
    public  DBManager(){
        DatabaseHelper db = new DatabaseHelper(context);
        sqlDB = db.getWritableDatabase();
    }

    public Cursor getNotepads(){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TName);
        String selectAll[] = {"*"};
        Cursor cursor = qb.query(sqlDB, selectAll, null, null, null, null, tableName);
        return  cursor;
    }
    public void enterTableData(String dbName, String item, String quantity, float amount){
        sqlDB.execSQL("Create table IF NOT EXISTS " + dbName + "(ID integer primary key autoincrement, "
                + DateTime + "," + Item + "," + Quantity + "," + Amount + ");");
        ContentValues values = new ContentValues();
        values.put(tableName,dbName);

        Insert(TName,values);
        Date d = new Date();

        values = new ContentValues();
        values.put("item", item);
        values.put("quantity", quantity);
        values.put("amount", amount);
        values.put("dateTime",d.toString());

        Insert(dbName,values);
    }

    private long Insert(String tableName, ContentValues values){
        long ID = sqlDB.insert(tableName,"",values);// if fails to insert then ID = 0
        Toast.makeText(context,String.valueOf(ID), Toast.LENGTH_LONG).show();
        return ID;
    }

    public Cursor query(String tableName){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(tableName);
        String selectAll[] = {"*"};

        Cursor cursor = qb.query(sqlDB, selectAll, null, null, null, null, null);
        return  cursor;
    }
    public Cursor query(String tableName, String item, String[] selectionArgs){
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(tableName);
        String selectAll[] = {"*"};
        item = item;
        Cursor cursor = qb.query(sqlDB, selectAll, item, selectionArgs, null, null, null);

        return cursor;
    }

    public int Delete(String tableName, String selection, String[] selectionArgs){
        int count = sqlDB.delete(tableName, selection, selectionArgs);
        return count;
    }
    public void drop(String notepad){
        sqlDB.execSQL("drop table " + notepad);
        String selectionArgs[] = {notepad};
        Delete(TName, "tableName like ? ", selectionArgs);
    }

}
