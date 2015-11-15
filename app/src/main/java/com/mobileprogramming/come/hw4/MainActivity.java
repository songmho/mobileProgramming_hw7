package com.mobileprogramming.come.hw4;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends ListActivity {

  //  SimpleAdapter adapter;
  //  ArrayList<HashMap<String,String>> memos;
    SimpleCursorAdapter adapter;
 //   MemoDBHelper memoDBHelper;
  //  SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver cr=getContentResolver();
        Cursor cursor=cr.query(MemoContract.CONTENT_URI,null,null,null,null);

        String[] selectionArgs={MemoContract.MEMO_TITLE};
        adapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,selectionArgs,new int[]{android.R.id.text1},CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);



//        memoDBHelper=new MemoDBHelper(this,null,1);
    //    db=memoDBHelper.getWritableDatabase();

 //       Cursor cursor=db.rawQuery("SELECT * FROM " + MemoDBHelper.tableName, null);

   //     String[] selectionArgs={MemoDBHelper.colTitle};
 /*       memos=new ArrayList<>();
        HashMap<String,String> memo=new HashMap<>();
        memo.put("title","android");
        memo.put("contents", "Activitiy, View, Menu, Dialog...");
        memo.put("data", Calendar.getInstance().getTime().toString());
        memos.add(memo);

        adapter=new SimpleAdapter(getApplicationContext(),memos,R.layout.memo,new String[]{"title","date"},new int[]{R.id.textView1,R.id.textView2});*/
   //     adapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,selectionArgs,new int[] {android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(adapter);

        getListView().setOnItemLongClickListener(ilcl);
    }


    private AdapterView.OnItemLongClickListener ilcl=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
       //     memos.remove(position);
     //       db.execSQL("DELETE FROM " + MemoDBHelper.tableName + " WHERE " + MemoDBHelper.colID + " = " + id);
            ContentResolver cr=getContentResolver();
            cr.delete(ContentUris.withAppendedId(MemoContract.CONTENT_URI,id),null,null);
            Cursor cursor=cr.query(MemoContract.CONTENT_URI,null,null,null,null);
                    //db.rawQuery("SELECT * FROM " + MemoDBHelper.tableName, null);
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
            return true;
        }
    };

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ContentResolver cr=getContentResolver();

        Cursor cursor=cr.query(MemoContract.CONTENT_URI,null,null,null,null);

        // Cursor cursor=db.rawQuery("SELECT * FROM " + MemoDBHelper.tableName + " WHERE " +MemoDBHelper.colID + " = "+id,null);
        cursor.moveToFirst();

        Intent intent=new Intent();
        intent.setClassName("com.mobileprogramming.come.hw4", "com.mobileprogramming.come.hw4.MemoActivity");
        intent.setAction("android.intent.action.EDIT");
        intent.putExtra("title", cursor.getString(1));
        intent.putExtra("contents", cursor.getString(2));
        intent.putExtra("position",position);
        startActivityForResult(intent, 0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
     //   int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_newMemo) {
            Intent intent=new Intent();
            intent.setClassName("com.mobileprogramming.come.hw4", "com.mobileprogramming.come.hw4.MemoActivity");
            intent.setAction("android.intent.action.CREATE");
            startActivityForResult(intent,0);
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ContentResolver cr=getContentResolver();
        if(resultCode==RESULT_OK){
            if(data.getBooleanExtra("isEdited",true)){
                long posId=adapter.getItemId(data.getIntExtra("position",-1));
              /*  db.execSQL("UPDATE "+MemoDBHelper.tableName + " SET "+MemoDBHelper.colTitle+" = '"
                +data.getStringExtra("title") +"', " +
                MemoDBHelper.colContent + " = '"
                +data.getStringExtra("contents") + "', "+
                MemoDBHelper.colDate+ " = '"+ Calendar.getInstance().getTime().toString() +"'"+" WHERE "+MemoDBHelper.colID+ " = "+posId);*/
                ContentValues cv=new ContentValues();
                cv.put(MemoContract.MEMO_TITLE,data.getStringExtra("title"));
                cv.put(MemoContract.MEMO_CONTENT,data.getStringExtra("contents"));
                cv.put(MemoContract.MEMO_DATE,Calendar.getInstance().getTime().toString());
                cr.update(ContentUris.withAppendedId(MemoContract.CONTENT_URI,posId),cv,null,null);
            }
            else{
           /*     db.execSQL("INSERT INTO "+MemoDBHelper.tableName+" VALUES ( NULL, "+ "'" + data.getStringExtra("title")+"', "+"'"+data.getStringExtra("contents")+"', "+"'"+
                Calendar.getInstance().getTime().toString()+" ') ");
            */
                ContentValues cv=new ContentValues();
                cv.put(MemoContract.MEMO_TITLE,data.getStringExtra("title"));
                cv.put(MemoContract.MEMO_CONTENT,data.getStringExtra("contents"));
                cv.put(MemoContract.MEMO_DATE,Calendar.getInstance().getTime().toString());
                cr.insert(MemoContract.CONTENT_URI,cv);
            }


            Cursor cursor=cr.query(MemoContract.CONTENT_URI,null,null,null,null);
                    //db.rawQuery("SELECT * FROM "+MemoDBHelper.tableName, null);
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
        }
        else if(resultCode==RESULT_CANCELED){

        }

    }
}
