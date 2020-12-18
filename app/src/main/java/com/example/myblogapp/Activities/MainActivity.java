package com.example.myblogapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myblogapp.Data.DataBaseHandler;
import com.example.myblogapp.Data.MysqlDB;
import com.example.myblogapp.Model.users_details;
import com.example.myblogapp.R;
import com.example.myblogapp.Util.LoaderDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.myblogapp.Util.Constants.BaseURL;

public class MainActivity extends AppCompatActivity {

    // variable for widgets
    private TextInputLayout username_login;
    private TextInputLayout password_login;
    private Button login_login, create_account_login;
    private DataBaseHandler dataBaseHandler;
    // for mysql class
    private MysqlDB mysqlDB;

    private LinearLayout mainLayout;
    private CheckBox remember_me;
    private TextInputEditText userEdittext;
    private TextInputEditText passEdittext;

    // shared preferences
    private SharedPreferences myPrefs;
    private static final String PREFS_NAME = "myPrefsFile";

    private LoaderDialog loaderDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loaderDialog = new LoaderDialog(MainActivity.this);

//        getActionBar().setTitle(R.string.app_name);

        // find the reference of widgets in this activity
        username_login = (TextInputLayout) findViewById(R.id.username_login_ID);
        password_login = (TextInputLayout) findViewById(R.id.password_login_ID);
        login_login = (Button) findViewById(R.id.login_button_ID);
        create_account_login = (Button) findViewById(R.id.create_account_button_ID);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        remember_me = (CheckBox) findViewById(R.id.remember_me);
        userEdittext = (TextInputEditText) findViewById(R.id.username_ID);
        passEdittext = (TextInputEditText) findViewById(R.id.password_ID);
        mysqlDB = new MysqlDB();


        if (SharedPrefmanager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this,BlogFeed.class));
            return;
        }

        login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = username_login.getEditText().getText().toString().trim();
                final String password = password_login.getEditText().getText().toString().trim();

                if (username.isEmpty()) {
                    username_login.setError("Please enter your username");
                    username_login.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    password_login.setError("Please enter your username");
                    passEdittext.requestFocus();
                    return;
                }

                username_login.setError(null);
                password_login.setError(null);

                // add progressBar here

//                loaderDialog.startLoader();
                userLogin(username, password);
//                loaderDialog.startLoader();
            }
        });


        // for create_account_login
        create_account_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Create_Account.class));
            }
        });

    }

    private void userLogin(String username, String password) {
        // if everything is fine then make a api request

        final String URL = BaseURL + "?TASK=VALIDATE_USER&username="+username+"&password="+password;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // if no error in the response

                    if (jsonObject.getInt("success")==1){

                        JSONObject jsonUser = jsonObject.getJSONObject("userDB");

                        //getting user from response
                        users_details user = new users_details();

                        //setting user field from response
                        user.setEmail(jsonUser.getString("email"));
                        user.setUsername(jsonUser.getString("user_name"));
                        user.setGender(jsonUser.getInt("gender"));
                        user.setFull_name(jsonUser.getString("full_name"));
                        user.setPhone_contact(jsonUser.getString("phone_number"));
                        user.setProfile_image_address(jsonUser.getString("image"));

                        //storing the user in shared preference
                        SharedPrefmanager.getInstance(MainActivity.this).userLogin(user);

                        Log.d("RESPONSE", String.valueOf(jsonObject.getJSONObject("userDB")));
                        startActivity(new Intent(MainActivity.this,BlogFeed.class));

                    } else {
                        username_login.setError("Incorrect username");
                        password_login.setError("Incorrect password");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("ERROR",e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        volleySingleton.getInstance(this).getRequestQueue().add(stringRequest);
    }

}

