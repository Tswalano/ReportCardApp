package com.example.codetribe.reportcardapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    Button submit;
    EditText fname, lname, password, email, number;
    ImageView image;

    protected SQLiteDatabase db;
    private Context myContext;
    protected Cursor cursor;
    String query;
    protected final String TABLE = "Students";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        submit = (Button)findViewById(R.id.submit);
        fname = (EditText)findViewById(R.id.fname);
        lname = (EditText)findViewById(R.id.lname);
        password = (EditText)findViewById(R.id.password);
        email = (EditText)findViewById(R.id.email);
        number = (EditText)findViewById(R.id.number);
        image = (ImageView)findViewById(R.id.profilepic);

        createTable();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                //deleteTable();
            }
        });
    }

    void createTable(){
        //Open database
        db = this.openOrCreateDatabase("School.db", MODE_PRIVATE, null);
        try{
            //fname,lname,password,email,number,img_path
            db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "fname TEXT(50) NOT NULL, lname TEXT(50) NOT NULL, password TEXT(50) NOT NULL," +
                    "email TEXT(50) NOT NULL, number INTEGER NOT NULL);");
            System.out.println("TABLE CREATED");

        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
    }

    void insertData(){
        db = this.openOrCreateDatabase("School.db", MODE_PRIVATE, null);
        try{
            String fn = fname.getText().toString();
            String ln = lname.getText().toString();
            String pw = password.getText().toString();
            String em = email.getText().toString();
            String nm = fname.getText().toString();

                db.execSQL("INSERT INTO "+TABLE+"(id,fname,lname,password,email,number) VALUES(null," +
                     "'"+fn+"','"+ln+"','"+pw+"','"+em+"','"+nm+"');");
            System.out.println("DATA INSERTED");
            success();
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();
    }

    void deleteTable(){
        db = this.openOrCreateDatabase("School.db", MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        System.out.println("TABLE DROPPED");
        db.close();
    }

    private void success() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Register.this);
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Thank You For Joining Report Card App")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        });
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }
}
