package com.example.codetribe.reportcardapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    Button login, register;
    EditText user, pass;

    //SQLite Variables
    protected SQLiteDatabase db;
    protected Cursor cursor;
    ConnectDB connectDB = new ConnectDB();
    static int whoAreYou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (Button)findViewById(R.id.register);
        login = (Button)findViewById(R.id.login);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);

        db = this.openOrCreateDatabase(connectDB.DATABASE, MODE_PRIVATE, null);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choosePerson();
                loginAuth();
            }
        });
    }

    private void choosePerson() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("Login");
        alertDialog.setMessage("Login as a Admin or Student?")
                .setCancelable(false)
                .setPositiveButton("ADMIN",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        })
                .setNegativeButton("STUDENT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), Students.class));
                    }
                });
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }

    void loginAuth(){

        String getUser = user.getText().toString();
        String getPass = user.getText().toString();

        String[] checkData = {getUser};


        if(user.getText().toString().equals("")){
            user.setError("Fill in your username or email");
        }
        else if(pass.getText().toString().equals("")) {
            pass.setError("Fill in your password");
        }
        else{
            cursor = db.rawQuery(connectDB.loginAuth, checkData);
            int x = cursor.getCount();

            if(x > 0 || (getUser.equalsIgnoreCase("Admin") && getPass.equalsIgnoreCase("Admin"))){
                if(getUser.equalsIgnoreCase("Admin") && getPass.equalsIgnoreCase("Admin")){
                    whoAreYou = 0;
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    whoAreYou = 1;
                    startActivity(new Intent(getApplicationContext(), Students.class));
                }

            }else {
                Toast.makeText(Login.this, "Login Failed!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
