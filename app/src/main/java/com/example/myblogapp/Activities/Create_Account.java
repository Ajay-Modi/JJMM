package com.example.myblogapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myblogapp.Data.VolleyMultipartRequest;
import com.example.myblogapp.Data.volleyWservices;
import com.example.myblogapp.Model.users_details;
import com.example.myblogapp.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.myblogapp.Util.Constants.BaseURL;


public class Create_Account extends AppCompatActivity {
    // variable for reference
    private TextInputEditText set_email;
    private TextInputEditText set_username;
    private TextInputEditText password;
    private TextInputEditText fullname_signup;
    private TextInputLayout emailLayout, passwordLayout, con_passLayout, usernameLayout;
    private ImageView profile_image;
    private Button click_create;
    private final static int gallery_code = 1;
    private Bitmap bitmap=null;

    public static final String TAG = "MainActivity";
    //    private DataBaseHandler dataBaseHandler;
    private volleyWservices volley = null;
    private MaterialCardView profile_image_cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__account);


        // find reference of each variable
        set_email = (TextInputEditText) findViewById(R.id.set_email_ID);
        fullname_signup = (TextInputEditText) findViewById(R.id.fullname_signup);
        password = (TextInputEditText) findViewById(R.id.password_create_ID);
        emailLayout = (TextInputLayout) findViewById(R.id.set_emailLayout_ID);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_createLayout_ID);
        con_passLayout = (TextInputLayout) findViewById(R.id.set_retypePasswordLayout_ID);
        set_username = (TextInputEditText) findViewById(R.id.set_username_ID);
        usernameLayout = (TextInputLayout) findViewById(R.id.set_usernameLayout_ID);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        profile_image_cardView = (MaterialCardView) findViewById(R.id.profile_image_cardView);

        volley = new volleyWservices(Create_Account.this);


        click_create = (Button) findViewById(R.id.create_Account_1_ID);

        //checking the permission for read external storage, if not given then we will open setting
        //to add permission else app will not open

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,Uri.parse("package:"
//                        + getPackageName()));
//                finish();
//                startActivity(intent);
//                return;
//        }

        // set profile pic from gallery
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a intent to pick a image from gallery
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,gallery_code);
            }
        });

        // set onClick on the fields
        click_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = set_email.getText().toString().trim();
                String username = set_username.getText().toString().trim();
                String fullname = fullname_signup.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (email.isEmpty()) {
                    emailLayout.setError("enter email");
                    emailLayout.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailLayout.setError("invalid email");
                    emailLayout.requestFocus();
                    return;
                }

                if (fullname.isEmpty()) {
                    fullname_signup.setError("enter full name");
                    fullname_signup.requestFocus();
                    return;
                }

                if (username.isEmpty()) {
                    usernameLayout.setError("enter username");
                    usernameLayout.requestFocus();
                    return;
                }

                if (username.length() > 18) {
                    usernameLayout.setError("username too long, must be up to 18 chars");
                    usernameLayout.requestFocus();
                    return;
                }

                if (pass.length() < 6) {
                    passwordLayout.setError("password too short");
                    passwordLayout.requestFocus();
                    return;

                }

                // add data to user
                System.out.println("Ready to create new user Section");
                emailLayout.setError(null);
                usernameLayout.setError(null);
                passwordLayout.setError(null);
                fullname_signup.setError(null);

                users_details user = new users_details();
                user.setEmail(email);
                user.setFull_name(fullname);
                user.setPassword(pass);
                user.setUsername(username);

//                createUser(user); // create user on server using volley services
                uploadBitmap(bitmap, user);

                }
        });
    }

    // getting result from image intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == gallery_code && resultCode == RESULT_OK){
            // add functionality to crop a image .. using a library (android-image-cropper)
            Uri mImage = data.getData();

            CropImage.activity(mImage).setAspectRatio(1,1).setGuidelines(CropImageView.Guidelines.ON).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                // getting image uri
                Uri imageURI = result.getUri();

                try {
                    //getting bitmap object from uri
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageURI);

                    //display selected image to imageView
                    profile_image.setImageBitmap(bitmap);

                    //calling method uploadBitmap to upload bitmap

                } catch ( IOException e) {
                    e.printStackTrace();
                }

                profile_image_cardView.setStrokeColor(getResources().getColor(R.color.colorPrimary));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("ERROR IN CROP IMAGE",error.getMessage());
                error.printStackTrace();

            }
        }


    }

    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    //upload bitmap and some other info with this
    private void uploadBitmap( final Bitmap bitmap, final users_details user) {
        final String URL = BaseURL;

        //custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultresponse = new String(response.data);

                        Log.d("RESPONSE",resultresponse);

                        try {
                            JSONObject jsonObject = new JSONObject(resultresponse);
                            int status = jsonObject.getInt("success");
                            String message = jsonObject.getString("message");

                            if (status == 1) {
                                Toast.makeText(Create_Account.this, "user created!", Toast.LENGTH_SHORT).show();

                                Log.d("CREATE_ACCOUNT", message);

                                JSONObject userDB = jsonObject.getJSONObject("userDB");
                                users_details user = new users_details();
                                user.setEmail(userDB.getString("email"));
                                user.setFull_name(userDB.getString("full_name"));
                                user.setPhone_contact(userDB.getString("phone_number"));
                                user.setUsername(userDB.getString("user_name"));
                                user.setGender(userDB.getInt("gender"));
                                user.setProfile_image_address(userDB.getString("image"));

                                SharedPrefmanager.getInstance(Create_Account.this).userLogin(user);
                                startActivity(new Intent(Create_Account.this, BlogFeed.class));

                            } else {
                                Toast.makeText(Create_Account.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            Log.d("JSONException", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("VOLLEY ERROR",error.getMessage());
                                error.getStackTrace();
                                Toast.makeText(Create_Account.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("TASK","signup_user");
                params.put("email",user.getEmail());
                params.put("fullname",user.getFull_name());
                params.put("username",user.getUsername());
                params.put("password",user.getPassword());
                return params;
            }

            //passing image by rename it uniquely

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws AuthFailureError {
                Map<String,DataPart> params = new HashMap<>();
                String imagename = user.getUsername();
                Log.d("Bitmap",bitmap.toString());
                params.put("pic", new DataPart(imagename+".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        volleySingleton.getInstance(Create_Account.this).getRequestQueue().add(volleyMultipartRequest);

        }

    private void createUser(final users_details user) {

        final String URL = BaseURL + "?TASK=singup_user";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("success") == 1) {
                        Toast.makeText(Create_Account.this, "user created!", Toast.LENGTH_SHORT).show();
                        
                        Log.d("CREATE_ACCOUNT","ready to get jsonobject");
                        Log.d("CREATE_ACCOUNT",jsonObject.getJSONObject("userDB").toString());

                        JSONObject userDB = jsonObject.getJSONObject("userDB");
                        users_details user = new users_details();
                        user.setEmail(userDB.getString("email"));
                        user.setFull_name(userDB.getString("full_name"));
                        user.setPhone_contact(userDB.getString("phone_number"));
                        user.setUsername(userDB.getString("user_name"));
                        user.setGender(userDB.getInt("gender"));

                        SharedPrefmanager.getInstance(Create_Account.this).userLogin(user);
                        startActivity(new Intent(Create_Account.this,BlogFeed.class));
                    } else {
                        Toast.makeText(Create_Account.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                Toast.makeText(Create_Account.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("TASK","signup_user");
                params.put("email",user.getEmail());
                params.put("fullname",user.getFull_name());
                params.put("username",user.getUsername());
                params.put("password",user.getPassword());
                return params;
            }
        };

//        volleySingleton.getInstance(Create_Account.this).getRequestQueue().add(stringRequest);

    }
}
