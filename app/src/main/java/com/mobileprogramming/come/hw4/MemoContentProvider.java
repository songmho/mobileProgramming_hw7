package com.mobileprogramming.come.hw4;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by songmho on 2015-11-16.
 */
public class MemoContentProvider extends ContentProvider {

    SQLiteDatabase db;

    static final int ALL_MEMOS=1;
    static final int ALL_TITLES=2;
    static final int ONE_MEMO=3;

    static final UriMatcher Matcher;
    static {
        Matcher=new UriMatcher(UriMatcher.NO_MATCH);
        Matcher.addURI(MemoContract.AUTHORITY,MemoDBHelper.tableName,ALL_MEMOS);
        Matcher.addURI(MemoContract.AUTHORITY,MemoDBHelper.tableName+"/"+MemoContract.MEMO_TITLE,ALL_TITLES);
        Matcher.addURI(MemoContract.AUTHORITY,MemoDBHelper.tableName+"/#",ONE_MEMO);
    }
    @Override
    public boolean onCreate() {
        MemoDBHelper memoDBHelper=new MemoDBHelper(getContext(),null, 1);
        db=memoDBHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int code=Matcher.match(uri);
        Cursor cursor;

        switch (code){
            case ALL_MEMOS:
                cursor=db.rawQuery("SELECT * FROM"+MemoDBHelper.tableName,null);
                break;
            case ALL_TITLES:
                cursor=db.rawQuery("SELECT " + MemoDBHelper.colID+", "+ MemoDBHelper.colTitle+" FROM "+MemoDBHelper.tableName,null);
                break;
            case ONE_MEMO:
                String id=uri.getPathSegments().get(1);
                cursor=db.rawQuery("SELECT * FROM " + MemoDBHelper.tableName+" WHERE "+MemoDBHelper.colID+" = "+id,null);
                break;
            default:
                cursor=null;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int code=Matcher.match(uri);

        switch (code){
            case ALL_MEMOS:
                return "vnd.memocptest.cursor.item/memos";
            case ALL_TITLES:
                return "vnd.memocptest.cursor.item/titles";
            case ONE_MEMO:
                return "vnd.memocptest.cursor.item/memo";
            default:
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code=Matcher.match(uri);
        switch (code){
            case ALL_MEMOS:
                long row=db.insert(MemoDBHelper.tableName,null,values);
                if(row>0){
                    Uri notiuri= ContentUris.withAppendedId(MemoContract.CONTENT_URI,row);
                    getContext().getContentResolver().notifyChange(notiuri,null);
                    return notiuri;
                }
                break;
            default:
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code=Matcher.match(uri);
        int count=0;

        switch (code){
            case ALL_MEMOS:
                count=db.delete(MemoDBHelper.tableName,selection,selectionArgs);
                break;
            case ONE_MEMO:
                String where=MemoDBHelper.colID+" = "+uri.getPathSegments().get(1)+
                        (selection == null || selection.length()==0 ? "" : " AND "+ selection);
                count=db.delete(MemoDBHelper.tableName,where,selectionArgs);
                break;
            default:
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int code=Matcher.match(uri);
        int count=0;

        switch (code){
            case ONE_MEMO:
                String where=MemoDBHelper.colID+" = "
                        + uri.getPathSegments().get(1) +
                        (selection==null || selection.length() ==0 ? "" :"AND " +selection);
                count=db.update(MemoDBHelper.tableName,values,where,selectionArgs);
                break;
            default:
        }
        return count;
    }
}
