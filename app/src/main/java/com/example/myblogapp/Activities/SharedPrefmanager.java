package com.example.myblogapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myblogapp.Model.users_details;

import static com.example.myblogapp.Util.Constants.*;


public class SharedPrefmanager {
    // the constants
    private static final String SHARED_PREF_NAME = "usersession";



    private static SharedPrefmanager mSharedPrefInstance;
    private static Context ctx;

    public SharedPrefmanager(Context ctx) {
        this.ctx = ctx;
    }

    public static synchronized SharedPrefmanager getInstance(Context context){
        if (mSharedPrefInstance==null){
            return new SharedPrefmanager(context.getApplicationContext());
        }
        return mSharedPrefInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preference

    public void userLogin(users_details user){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID,user.getId());
        editor.putString(USERNAME,user.getUsername());
        editor.putString(EMAIL,user.getEmail());
        editor.putString(PASSWORD,user.getPassword());
        editor.putString(FULL_NAME,user.getFull_name());
        editor.putString(PHONE_NUMBER,user.getPhone_contact());
        editor.putInt(GENDER,user.getGender());
        editor.putString(PROFILE_IMAGE,user.getProfile_image_address());
        editor.apply();

        // if want to add image in the token then do that here only


    }

    // method which will check whether user is already logged in or not
    public Boolean isLoggedIn(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        Log.d("isLoggedIn", "Username of the LoggedIn user: " + sharedPreferences.getString(USERNAME,null));
        return sharedPreferences.getString(USERNAME,null)!=null;
    }

    // this method will give logged in user
    public users_details getUser(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        users_details user = new users_details();
        user.setUsername(sharedPreferences.getString(USERNAME,null));
        user.setEmail(sharedPreferences.getString(EMAIL,null));
        user.setFull_name(sharedPreferences.getString(FULL_NAME,null));
        user.setPhone_contact(sharedPreferences.getString(PHONE_NUMBER,null));
        user.setGender(sharedPreferences.getInt(GENDER,1));
        user.setProfile_image_address(sharedPreferences.getString(PROFILE_IMAGE,null));
        user.setPassword(sharedPreferences.getString(PASSWORD,null));

        return user;
    }

    //this method will logout the user
    public void logout(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


        // write code when user is logout...
    }

}
