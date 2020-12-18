package com.example.myblogapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myblogapp.Model.users_details;

import static com.example.myblogapp.Util.Constants.DB_NAME;
import static com.example.myblogapp.Util.Constants.DB_VERSION;
import static com.example.myblogapp.Util.Constants.EMAIL;
import static com.example.myblogapp.Util.Constants.FULL_NAME;
import static com.example.myblogapp.Util.Constants.GENDER;
import static com.example.myblogapp.Util.Constants.KEY_ID;
import static com.example.myblogapp.Util.Constants.PASSWORD;
import static com.example.myblogapp.Util.Constants.PHONE_NUMBER;
import static com.example.myblogapp.Util.Constants.TABLE_NAME;
import static com.example.myblogapp.Util.Constants.USERNAME;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class DataBaseHandler extends SQLiteOpenHelper {

    private Context ctx;

    public DataBaseHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // query to create a table in database
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + EMAIL + " TEXT, " + GENDER + " VARCHAR(8), " + USERNAME + " TEXT NOT NULL, " + FULL_NAME + " TEXT, " + PHONE_NUMBER + "INTEGER, "
                + PASSWORD + " TEXT NOT NULL );";

        // command to execute sql query
        db.execSQL(CREATE_USERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    // here write all CRUD operations
    public void addUsers(users_details user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL,user.getEmail());
        values.put(PASSWORD,user.getPassword());
        values.put(USERNAME,user.getUsername());

        // Inserting row
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public void updateUsers(users_details user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME,user.getUsername());
        values.put(PHONE_NUMBER,user.getPhone_contact());
        values.put(GENDER,user.getGender());
        values.put(FULL_NAME,user.getFull_name());

        // update user
        db.update(TABLE_NAME,values,KEY_ID + " = ?",new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // delete a user
    public void deleteUser(users_details user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //check user
    public boolean uniqueUser(String email){
        // array of string (columns) to fetch
        String[] columns = {KEY_ID};

        // create database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // condition for where clause
        String condition = EMAIL + " =?";

        // selection argument
        String[] selectionArgs = {email};

        //Equivalent SQL query for this :- select id from userDetailsTBL where email = 'ajayjayy4@gmail.com';

        Cursor cursor = db.query(TABLE_NAME,columns,condition,selectionArgs,null,null,null);

        int cursorCount = cursor.getCount();
        db.close();

        if (cursorCount >= 1 ){
            return false;
        } else {
            return  true;
        }

    }

    // user exit or not for login page
    public boolean userExist(String username, String password){
        // columns to fetch
        String[] columns = { KEY_ID };

        // condition for where clause
        String condition = USERNAME + " =? and " + PASSWORD + " =?";

        // argument for condition
        String[] agrument = {username, password};

        // Equivalent SQL query :- select id from userDetailsTBL where username = 'ajaymodi' and password = 'chigu';

        // db instance
        SQLiteDatabase db = this.getWritableDatabase();

        // create a cursor to have all matching row
        Cursor cursor = db.query(TABLE_NAME,columns,condition,agrument,null,null,null);

        int cursorCount = cursor.getCount();
        db.close();

        if (cursorCount >= 1){
            return true;
        } else {
            return false;
        }

    }

}
