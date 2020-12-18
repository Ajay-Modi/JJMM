package com.example.myblogapp.Data;


import android.os.AsyncTask;

import com.example.myblogapp.Model.users_details;
import com.example.myblogapp.Util.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.myblogapp.Util.Constants.EMAIL;
import static com.example.myblogapp.Util.Constants.FULL_NAME;
import static com.example.myblogapp.Util.Constants.GENDER;
import static com.example.myblogapp.Util.Constants.KEY_ID;
import static com.example.myblogapp.Util.Constants.PASSWORD;
import static com.example.myblogapp.Util.Constants.PHONE_NUMBER;
import static com.example.myblogapp.Util.Constants.PROFILE_IMAGE;
import static com.example.myblogapp.Util.Constants.TABLE_NAME;
import static com.example.myblogapp.Util.Constants.USERNAME;

public class MysqlDB {

    private String DB_URL = Constants.DB_URL;
    private String CL_USER = Constants.CL_USER;
    private String CL_PASSWORD = Constants.CL_PASSWORD;

    public void establishConn() {
        class establish extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(DB_URL, CL_USER, CL_PASSWORD);

                    String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " + KEY_ID + " INTEGER PRIMARY KEY, "
                            + EMAIL + " VARCHAR(255) NOT NULL, " + GENDER + " VARCHAR(8), " + USERNAME + " VARCHAR(255) NOT NULL, " + FULL_NAME + " VARCHAR(255), " + PHONE_NUMBER + " VARCHAR(30), "
                            + PASSWORD + " VARCHAR(255) NOT NULL );";

                    Statement statement = connection.createStatement();
                    statement.execute(CREATE_USERS_TABLE);
                    statement.close();
                    connection.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        establish temp = new establish();
        temp.execute();
    }

    // saveUser to Database --> blogapp.user

    public Boolean saveUser(users_details user) {
        class saveuser extends AsyncTask<users_details, Void, Boolean> {

            @Override
            protected Boolean doInBackground(users_details... users) {
                users_details user = users[0];
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(DB_URL, CL_USER, CL_PASSWORD);

                    Statement statement = connection.createStatement();

                    String query = "INSERT INTO " + TABLE_NAME + " ( " + EMAIL + ", " + USERNAME + ", " + PASSWORD + " ) VALUES( '" + user.getEmail() + "', '" + user.getUsername() + "', '" + user.getPassword() + "' );";

                    System.out.println(query);
                    statement.execute(query);
                    System.out.println("Query Executed Successfully");
                    statement.close();
                    return true;

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Error Occurred");
                    return false;
                }
            }
        }
        saveuser save = new saveuser();
        return save.doInBackground(user);
    }

    public void saveCompleteInfo(users_details user) {
        class completeInfo extends AsyncTask<users_details, Void, Void> {
            @Override
            protected Void doInBackground(users_details... users_details) {
                users_details user = users_details[0];
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(DB_URL, CL_USER, CL_PASSWORD);
                    Statement statement = connection.createStatement();

                    String query = "UPDATE" + TABLE_NAME + " SET " + FULL_NAME + " = '" + user.getFull_name() + "', " + GENDER + " = '" + user.getGender() + "', "
                            + PHONE_NUMBER + " = '" + user.getPhone_contact() + "', " + PROFILE_IMAGE + " = '" + user.getProfile_image_address() + "' where " + USERNAME + " = '" + user.getUsername() + "';";

                    statement.execute(query);

                    statement.close();
                    connection.close();

                    System.out.println("COMPLETE INFO SAVED");

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        completeInfo com = new completeInfo();
        com.execute(user);
    }

    public Boolean uniqueUser(String email) {
        class uniqueuser extends AsyncTask<String, Void, Void> {
            Boolean temp = null;

            @Override
            protected Void doInBackground(String... strings) {
                String email = strings[0];

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(DB_URL, CL_USER, CL_PASSWORD);
                    Statement statement = connection.createStatement();
                    String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + EMAIL + " = '" + email + "';";
                    System.out.println(query);
                    ResultSet resultSet = statement.executeQuery(query);

                    if (resultSet.next()) {
                        temp = false;
                    } else {
                        temp = true;
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        uniqueuser usertocheck = new uniqueuser();
        usertocheck.doInBackground(email);
        System.out.println(usertocheck.temp);

        return usertocheck.temp;
    }

    // check user in the database

    public Boolean checkUser(String username, String password) {
        class checkuser extends AsyncTask<String, Void, Boolean> {

            @Override
            protected Boolean doInBackground(String... strings) {
                String username = strings[0];
                String password = strings[1];

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(DB_URL, CL_USER, CL_PASSWORD);
                    Statement statement = connection.createStatement();
                    String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME + " = '" + username + "' and " + PASSWORD + " = '" + password + "' ;" ;
                    System.out.println(query);
                    ResultSet resultSet = statement.executeQuery(query);

                    if (resultSet.next()) {
                        return true;
                    } else {
                        return false;
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Incorrect Username But Due to Exception");
                    return false;
                }
            }
        }

        checkuser usertocheck = new checkuser();
        return usertocheck.doInBackground(username, password);
    }


    public Boolean completeUserInfo(users_details user){
        class completeuserinfo extends AsyncTask<users_details,Void,Boolean>{

            @Override
            protected Boolean doInBackground(users_details... users_details) {
                users_details user = users_details[0];

                try {
                    Class.forName("com.mysql.jdbc.Driver");

                    Connection conn = DriverManager.getConnection(DB_URL, CL_USER, CL_PASSWORD);

                    String query = "UPDATE " + TABLE_NAME + " SET " + FULL_NAME + " ='" + user.getFull_name() + "', " + GENDER + " = " + user.getGender() + ", " + PHONE_NUMBER + " ='" + user.getPhone_contact() + "', " + PROFILE_IMAGE + " = ? WHERE "+
                            USERNAME+" ='"+user.getUsername()+"' ;";

                    PreparedStatement preparedStatement = conn.prepareStatement(query);

                    System.out.println(preparedStatement);
                    InputStream is = null;
                    try {
                        is = new FileInputStream(user.getProfile_image_base64());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    preparedStatement.setBlob(1, is);
                    System.out.println(preparedStatement);

                    preparedStatement.execute();
                    System.out.println("Image saved to database");

                    preparedStatement.close();
                    conn.close();

                    return true;
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        }
        completeuserinfo cm = new completeuserinfo();

        return cm.doInBackground(user);
    }

}



