package com.example.myblogapp.Util;

public class Constants {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "blogapp";
    public static final String TABLE_NAME = "users";

    // URLs for api call

    // virus IP : 192.168.3.156, chirag IP : 192.168.43.40, ajaymodi ID : 192.168.43.94
    public static final String IP = "192.168.3.156" ;
    public static final String BaseURL = "http://"+ IP + "/phptesting/wservices/registerUser.php";

    // Table column
    public static final String KEY_ID = "id";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String FULL_NAME = "full_Name";
    public static final String GENDER = "gender";
    public static final String PHONE_NUMBER = "phone_Number";
    public static final String PROFILE_IMAGE = "profile_image";

    //client user details
    public static final String DB_URL = "jdbc:mysql://192.168.43.94:3306/"+DB_NAME+"?allowPublicKeyRetrieval=true&useSSL=false";
    public static final String CL_USER = "root";
    public static final String CL_PASSWORD= "chirag_soni";

}
