package com.example.codetribe.reportcardapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView full_names;
    Button prev, next, delete, update;
    ImageView image;
    EditText score1, score2, score3,subject1, subject2, subject3;

    ConnectDB connectDB = new ConnectDB();

    //SQLite Variables
    protected SQLiteDatabase db;
    protected Cursor cursor, cursorMarks;

    int from = Login.whoAreYou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        full_names = (TextView) findViewById(R.id.full_names);
        next = (Button) findViewById(R.id.next);
        prev = (Button) findViewById(R.id.prev);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        image = (ImageView)findViewById(R.id.logo);

        score1 = (EditText)findViewById(R.id.score1);
        score2 = (EditText)findViewById(R.id.score2);
        score3 = (EditText)findViewById(R.id.score3);

        subject1 = (EditText)findViewById(R.id.Subject1);
        subject2 = (EditText)findViewById(R.id.Subject2);
        subject3 = (EditText)findViewById(R.id.Subject3);

        openDB();
        cursor = db.rawQuery(connectDB.getAllStudents, null);
        cursor.moveToFirst();

        cursorMarks = db.rawQuery(connectDB.getAllMarks
                , null);
        cursorMarks.moveToFirst();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNext();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movePrev();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMarks();
            }
        });

        if((Login.whoAreYou == 0) && (Students.getId == 0)){
            Toast.makeText(this, from+" From Admin...", Toast.LENGTH_LONG).show();
            showRecords();
        }else{
            Toast.makeText(this, from+" From Students...", Toast.LENGTH_LONG).show();
            showReport();
        }
    }

    protected void openDB(){
        db = openOrCreateDatabase(connectDB.DATABASE, Context.MODE_PRIVATE, null);
    }

    void showRecords(){
            //Student Names
            String fn = cursor.getString(1);
            String ln = cursor.getString(2);
            byte[] img = cursor.getBlob(7);

            //Subjects
            String sb1 = cursorMarks.getString(1);
            String sb2 = cursorMarks.getString(2);
            String sb3 = cursorMarks.getString(3);

            //Marks
            int sc1 = cursorMarks.getInt(4);
            int sc2 = cursorMarks.getInt(5);
            int sc3 = cursorMarks.getInt(6);

//        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
//        ByteArrayOutputStream boas = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas ); //bm is the bitmap object
//        byte[] byteArrayImage = boas.toByteArray();
//        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

//        System.out.println(img.toString());
 //       System.out.println(encodedImage.toString());

            subject1.setText(sb1);
            subject2.setText(sb2);
            subject3.setText(sb3);

            score1.setText(String.valueOf(sc1));
            score2.setText(String.valueOf(sc2));
            score3.setText(String.valueOf(sc3));

            full_names.setText(fn + " " + ln);
    }

    void showReport(){
        String[] id = {String.valueOf(Students.getId)};

        cursor = db.rawQuery(connectDB.queryID, id);
        cursorMarks = db.rawQuery(connectDB.queryMarkID, id);
        cursorMarks.moveToFirst();
        cursor.moveToFirst();

        subject1.setText(cursorMarks.getString(1));
        subject2.setText(cursorMarks.getString(2));
        subject3.setText(cursorMarks.getString(3));

        score1.setText(String.valueOf(cursorMarks.getInt(4)));
        score2.setText(String.valueOf(cursorMarks.getInt(5)));
        score3.setText(String.valueOf(cursorMarks.getInt(6)));

        String fn = cursor.getString(1);
        String ln = cursor.getString(2);
        byte[] img = cursor.getBlob(7);

        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        image.setImageBitmap(bitmap);
        System.out.println("data byte " + bitmap);
        System.out.println("byte " + img);


//        if(uri.getPath() != null){
//            image.setImageResource(R.mipmap.default_img);
//        }else
//        {
//            image.setImageURI(uri);
//        }

        full_names.setText(fn + " " + ln);
        score1.setEnabled(false);
        score2.setEnabled(false);
        score3.setEnabled(false);
        next.setVisibility(View.GONE);
        prev.setVisibility(View.GONE);
        update.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
    }

    void moveNext(){
        if(!cursor.isLast() && !cursorMarks.isLast()){
            cursor.moveToNext();
            cursorMarks.moveToNext();
            showRecords();
        }else if(cursor.isLast()){
            Toast.makeText(MainActivity.this, "No More Data!!", Toast.LENGTH_SHORT).show();
        }
    }

    void movePrev(){
        if(!cursor.isFirst() && !cursorMarks.isFirst()){
            cursor.moveToPrevious();
            cursorMarks.moveToPrevious();
            showRecords();
        }else if(cursor.isFirst()){
            Toast.makeText(MainActivity.this, "No More Data!!", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteRecord(){

        int x = cursor.getInt(0);
        String[] id = {String.valueOf(x)};

        cursor = db.rawQuery(connectDB.queryID, id);
        cursorMarks = db.rawQuery(connectDB.queryMarkID, id);

        Toast.makeText(MainActivity.this, "Deleted!!!", Toast.LENGTH_SHORT).show();
        cursorMarks.moveToPosition(cursorMarks.getPosition()+1);
        cursor.moveToPosition(cursor.getPosition()+1);

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    void addMarks(){ //UPDATE

        int x = cursor.getInt(0);
        String updateQuery = "UPDATE "+connectDB.TABLE_MARKS+" SET score1 = '"+score1.getText().toString()+
                "', score2 = '"+score2.getText().toString()+"', score3 = '"+score3.getText().toString()+
                "' WHERE id = "+x+";";
        db.execSQL(updateQuery);

        cursorMarks = db.rawQuery(connectDB.getAllMarks, null);
        cursor = db.rawQuery(connectDB.getAllStudents, null);

        Toast.makeText(MainActivity.this, "Updated!!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);

        cursorMarks.moveToPosition(x);
        cursor.moveToPosition(x);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Log Out?");
            alertDialog.setMessage("Are you sure you want to log out??")
                    .setCancelable(false)
                    .setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getApplicationContext(), Login.class));
                                    Students.getId = 0;
                                }
                            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //startActivity(new Intent(getApplicationContext(), Students.class));
                        }
                    });
            AlertDialog alertDialog1 = alertDialog.create();
            alertDialog1.show();
            return true;
        }
        return false;
    }
}
