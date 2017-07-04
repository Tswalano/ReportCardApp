package com.example.codetribe.reportcardapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView full_names;
    Button prev, next;

    private final String TABLE_NAME = "Students";
    private final String DATABASE = "School.db";
    SQLiteDatabase db;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        full_names = (TextView)findViewById(R.id.full_names);
        next = (Button)findViewById(R.id.next);
        prev = (Button)findViewById(R.id.prev);

        getInfo();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);
                c = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
                c.moveToFirst();
                for(int i = 0; i < c.getCount(); i++){
                    if(c.isFirst()){
                        c.moveToNext();
                        String fn = c.getString(1);
                        full_names.setText(fn);
                    }
                }
            }
        });
    }

    public Cursor moveNext(){
        db = openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);
        c = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        //c.moveToNext();
        c.getString(1);
        return c;
    }

    void setText(){
        String fn = c.getString(1);
        full_names.setText(fn);
    }

    void getInfo(){
        db = openOrCreateDatabase(DATABASE, Context.MODE_PRIVATE, null);
        Cursor resultsSet = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        resultsSet.moveToFirst();
        String fn = resultsSet.getString(1);
        full_names.setText(fn);
        db.close();
    }
}
