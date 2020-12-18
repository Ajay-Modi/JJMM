package com.example.myblogapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myblogapp.Data.MysqlDB;
import com.example.myblogapp.Model.users_details;
import com.example.myblogapp.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CompleteUserInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageView profile_image;
    private TextInputEditText full_name, phone_number;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button save_button;
    private MaterialCardView profile_image_cardView;
    private final static int gallery_code = 1;
    private String path=null;
    private MysqlDB mysqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_info);

        profile_image = (ImageView) findViewById(R.id.profile_image);
        full_name = (TextInputEditText) findViewById(R.id.set_full_name_ID);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupID);

        phone_number = (TextInputEditText) findViewById(R.id.set_phone_number_ID);
        save_button = (Button) findViewById(R.id.save_button_ID);
        profile_image_cardView = (MaterialCardView) findViewById(R.id.profile_image_cardView);


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

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                users_details user = new users_details();
                String fullname = full_name.getText().toString().trim();
                String contact = phone_number.getText().toString().trim();

                user.setFull_name(fullname);
                user.setPhone_contact(contact);

                int gender = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(gender);
                String selected = radioButton.getText().toString().trim();

                System.out.println(selected);

                if (selected=="Male"){
                    user.setGender(0);
                } else if (selected=="Female"){
                    user.setGender(1);
                } else {
                    user.setGender(2);
                }

                System.out.println(user.getGender());
                Bundle intent = getIntent().getExtras();
                String us = intent.getString("username");

                user.setUsername(us);

                mysqlDB = new MysqlDB();

                mysqlDB.completeUserInfo(user);

                Intent blogfeed_intent = new Intent(CompleteUserInfo.this,BlogFeed.class);
                startActivity(blogfeed_intent);

            }
        });
    }

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
                Uri resultUri = result.getUri();
                path = resultUri.getPath();
                System.out.println(resultUri);
                System.out.println(resultUri.getPath());

                // set image
                profile_image.setImageURI(resultUri);
                String aj = "Sample_4.png";

                profile_image_cardView.setStrokeColor(getResources().getColor(R.color.colorPrimary));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}