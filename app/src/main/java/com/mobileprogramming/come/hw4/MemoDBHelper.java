package com.mobileprogramming.come.hw4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

/**
 * Created by songmho on 2015-10-11.
 */
public class MemoDBHelper extends SQLiteOpenHelper {

    public static final String tableName="memos";
    public static final String colID="_id";
    public static final String colTitle="title";
    public static final String colContent="content";
    public static final String colDate="date";

    public MemoDBHelper(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, tableName+" .db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + tableName + " ( " + colID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
        colTitle + " TEXT, "+
        colContent+ " TEXT, "+
        colDate + " TEXT ) ");

        String date= Calendar.getInstance().getTime().toString();

        db.execSQL("INSERT INTO " + tableName + " VALUES ( NULL, 'Android' , 'activity, view, menu, dialog...' , " +"'"+ date+"'"+" ) ");

        db.execSQL("INSERT INTO " + tableName + " VALUES ( NULL, 'Java' , 'Class, package, Interface...' , " +"'"+ date+"'"+" ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
