package com.example.codetribe.reportcardapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Register extends AppCompatActivity {

    Button submit, button;
    EditText fname, lname, password, email, number, username;
    ImageView image;
    Spinner course;

//    protected SQLiteDatabase db;
//    protected Cursor cursor;
//    protected final String STUDENTS = "Students";
//    protected final String STUDENT_MARKS = "Marks";

    //Image variables
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    Uri selectedImage;

    //Course
    static String sub1;
    static String sub2;
    static String sub3;

    ConnectDB connectDB = new ConnectDB();
    SQLiteDatabase db;
    Cursor cursor;
    final int REQUEST_CODE_GALLARY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
//         {
        setContentView(R.layout.activity_register);
//         }else{
//         setContentView(R.layout.landscape);
//        }

//        button = (Button) findViewById(R.id.button);
        submit = (Button) findViewById(R.id.submit);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        number = (EditText) findViewById(R.id.number);
        username = (EditText) findViewById(R.id.username);
        image = (ImageView) findViewById(R.id.pro_pic);
        course = (Spinner) findViewById(R.id.course);

        createTable();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteTable();
//            }
//        });
    }

    void createTable() {
        //Open database
        db = this.openOrCreateDatabase(ConnectDB.DATABASE, MODE_PRIVATE, null);
        try {
            //fname,lname,username,password,email,number,img_dir
            db.execSQL(connectDB.createTableStudents);
            System.out.println("STUDENT TABLE CREATED");
            //subject1,subject2,subject3,score1,score2,score3,uid
            db.execSQL(connectDB.createTableMarks);
            System.out.println("STUDENT MARKS TABLE CREATED");

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    void insertData() {
        db = this.openOrCreateDatabase(connectDB.DATABASE, MODE_PRIVATE, null);
        try {
            String fn = fname.getText().toString();
            String ln = lname.getText().toString();
            String pw = password.getText().toString();
            String em = email.getText().toString();
            String nm = fname.getText().toString();
            String un = fname.getText().toString();

            if(course.getSelectedItem().toString().equalsIgnoreCase("Systems Development")){
                //Java, C# .Net, Database
                sub1 = "Java";
                sub2 = "C# .Net";
                sub3 = "Database";
            }else if(course.getSelectedItem().toString().equalsIgnoreCase("Mobile Development")){
                //Android, XML, FireBase
                sub1 = "Android";
                sub2 = "XML";
                sub3 = "FireBase";
            }else if(course.getSelectedItem().toString().equalsIgnoreCase("Systems Support")){
                //Networking, End User, A+/N+
                sub1 = "Networking";
                sub2 = "End User";
                sub3 = "A+/N+";
            }else if(course.getSelectedItem().toString().equalsIgnoreCase("Science")){
                //Mathematics, English, Science
                sub1 = "Mathematics";
                sub2 = "English";
                sub3 = "Science";
            }else if(course.getSelectedItem().toString().equalsIgnoreCase("Engineering")){
                //Mathematics, Electric, Mechanic
                sub1 = "Mathematics";
                sub2 = "Electric";
                sub3 = "Mechanic";
            }

            //fname,lname,username,password,email,number,img_dir
            byte[] image = imageViewToByte();
            String[] myStud = {fn,ln,un,pw,em,nm, String.valueOf(image)};
            db.execSQL(connectDB.insertStudents,myStud);

            int count;
            cursor = db.rawQuery(connectDB.getAllStudents, null);
            if(cursor.getCount() == 0){
                count = 1;
            }else{
                count = cursor.getCount();
            }

            String[] myMarks = {sub1,sub2,sub3,"0","0","0", String.valueOf(count)};
            db.execSQL(connectDB.insertMarks,myMarks);
            success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    void deleteTable() {
        db = this.openOrCreateDatabase(connectDB.DATABASE, MODE_PRIVATE, null);
        db.execSQL(connectDB.dropStudentTbl);
        db.execSQL(connectDB.dropMarksTbl);
        System.out.println("TABLE'S DROPPED");
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
                                startActivity(new Intent(getApplicationContext(), Students.class));
                            }
                        });
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }

    public void loadImage() {
        //create an intent to open Image applications like gallery
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        //start the intent
//        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        ActivityCompat.requestPermissions(Register.this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLARY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLARY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLARY);
            }else{
                Toast.makeText(this, "Failed to pick Image, Allow Permission first",Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            //If an Image is picked
//            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {
//                //Get the Image from data
//                selectedImage = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                //Get the cursor
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null,
//                        null, null);
//                // Move to first row
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imgDecodableString = cursor.getString(columnIndex);
//                cursor.close();
//
//                //set the image in imageView after decoding the string
//                //image.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
//                //Another way using file path
//                image.setImageURI(selectedImage);
//                Toast.makeText(this, imgDecodableString,
//                        Toast.LENGTH_LONG).show();
//            } else {
//                selectedImage = null;
//                Toast.makeText(this, "You haven't picked Image",
//                        Toast.LENGTH_LONG).show();
//                System.out.println(selectedImage);
//            }
//        } catch (Exception ex) {
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
//                    .show();
//            System.out.println(ex);
//        }
//    }

    private byte[] imageViewToByte() {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        System.out.println(byteArray.toString());
        return byteArray;
    }


    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_GALLARY){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
//        Uri uri = data.getData();
//        try {
//            InputStream inputStream = getContentResolver().openInputStream(uri);
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            image.setImageBitmap(bitmap);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
