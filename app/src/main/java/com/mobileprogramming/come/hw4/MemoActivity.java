package com.mobileprogramming.come.hw4;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by songmho on 2015-10-05.
 */
public class MemoActivity extends Activity implements View.OnClickListener {
    EditText title;
    EditText memo;
    Button done;
    Button cancel;
    boolean flag;
    int position;

    MemoDBHelper memoDBHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        title=(EditText)findViewById(R.id.editText1);
        memo=(EditText)findViewById(R.id.editText2);
        done=(Button)findViewById(R.id.button1);
        cancel=(Button)findViewById(R.id.button2);

        Intent intent=getIntent();
        String action =intent.getAction();

        if("android.intent.action.CREATE".equals(action)){
            title.setHint("title");
            memo.setHint("Type your memo here.");
            flag=false;
        }
        else if("android.intent.action.EDIT".equals(action)){
            title.setText(intent.getStringExtra("title"));
            memo.setText(intent.getStringExtra("contents"));
            position=intent.getIntExtra("position",-1);
            flag=true;
        }
        else {
            Toast.makeText(getApplicationContext(),"잘못된 접근 입니다.",Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        if(v==done){
            memoDBHelper=new MemoDBHelper(this,null,1);
            db=memoDBHelper.getWritableDatabase();

            Intent intent=new Intent();
            intent.putExtra("title",title.getText().toString());
            intent.putExtra("contents",memo.getText().toString());
            intent.putExtra("isEdited",flag);
            if(flag)
                intent.putExtra("position",position);

            setResult(RESULT_OK,intent);
            finish();
        }
        else if(v==cancel){
            Intent intent=new Intent();
            setResult(RESULT_CANCELED,intent);
            finish();
        }
    }
}
