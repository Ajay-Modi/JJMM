package com.example.myblogapp.createblog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myblogapp.Activities.BlogFeed;
import com.example.myblogapp.Activities.Create_Account;
import com.example.myblogapp.Activities.SharedPrefmanager;
import com.example.myblogapp.Activities.volleySingleton;
import com.example.myblogapp.Data.VolleyMultipartRequest;
import com.example.myblogapp.Model.blog_details;
import com.example.myblogapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.hootsuite.nachos.NachoTextView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.myblogapp.Util.Constants.BaseURL;

public class CreateBlog extends AppCompatActivity implements View.OnClickListener {
    private ImageView create_blog_image;
    private TextView add_title;
    private TextView add_content;
    private MaterialButton add_tags;
    private NachoTextView tags_ntv;
    private final static int gallery_code=11;
    private Bitmap image_bitmap=null;
    private  Toolbar toolbar;
    private ImageView cancel;
    private ImageView next;

    // pay load variables
    private String title="";
    private String content="";
    private List<String> tags;
    private final static int title_code=1;
    private final static int content_code=3;
    private final static int tags_code=4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);

        create_blog_image = (ImageView) findViewById(R.id.create_blog_image);
        add_title = (TextView) findViewById(R.id.title_add_button);
        add_content = (TextView) findViewById(R.id.content_add_button);
        add_tags = (MaterialButton) findViewById(R.id.add_tags_B_create_blog);
        tags_ntv = (NachoTextView) findViewById(R.id.tags_NTV_create_blog);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        String suggestion[] = {"Technology", "Mathematics", "Android", "AndroidDev", "Mind Peace", "Machines"};
        ArrayAdapter adapter = new ArrayAdapter(CreateBlog.this,R.layout.support_simple_spinner_dropdown_item,suggestion);
        tags_ntv.setAdapter(adapter);


        cancel = (ImageView) findViewById(R.id.cancel_button);
        next = (ImageView) findViewById(R.id.next_button);

        create_blog_image.setOnClickListener(this);
        add_title.setOnClickListener(this);
        add_content.setOnClickListener(this);
        add_tags.setOnClickListener(this);
        cancel.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == gallery_code && resultCode == RESULT_OK) {
            Log.d("CreateBlog", requestCode + " request code");
            Uri imgaeuri = data.getData();
            CropImage.activity(imgaeuri).setAspectRatio(4, 2).setGuidelines(CropImageView.Guidelines.ON).start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Log.d("CreateBlog", requestCode + " request code");
                if (resultCode == RESULT_OK) {

                    Uri image_uri = result.getUri();
                    try {
                        image_bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                        create_blog_image.setImageBitmap(image_bitmap);
                        Log.d("CreateBlog", requestCode + " request code");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("CreateBlog", "Not Got image from gallery " + e.getMessage());
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Log.d("ERROR IN CROP IMAGE",error.getMessage());
                    error.printStackTrace();

                }

        } else if ( requestCode == title_code && resultCode == RESULT_OK) {
            title = data.getStringExtra("title");

            if (!title.isEmpty()){
                if (!("ADD" == add_title.getText().toString())) {
                    add_title.setText("EDIT");
                }
            }
        } else if (requestCode == content_code && resultCode == RESULT_OK) {
            content = data.getStringExtra("content");

            if (!content.isEmpty()){
                if (!("ADD" == add_content.getText().toString())) {
                    add_content.setText("EDIT");
                }
            }

        } else if ( requestCode == tags_code && resultCode == RESULT_OK) {
            String tags = data.getStringExtra("tags");
            if (!tags.isEmpty()) {
                add_tags.setText("EDIT");
            }
        } else if (resultCode == RESULT_CANCELED ){
            Log.d("CreateBlog","RESULT_CANCELED");
        }
//

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.create_blog_image:
                startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),gallery_code);
                break;

            case R.id.cancel_button:
                finish();
                break;

            case R.id.title_add_button:
                startActivityForResult(new Intent(CreateBlog.this,AddTitle.class).putExtra("title",title),title_code);
                break;

            case R.id.content_add_button:
                startActivityForResult(new Intent(CreateBlog.this,AddContent.class).putExtra("content",content),content_code);
                break;

            case R.id.add_tags_B_create_blog:
               tags_ntv.setVisibility(View.VISIBLE);
                break;

            case R.id.next_button:

                // everthing is checked here like title, content is written or not..

                tags = tags_ntv.getChipValues();

                if (image_bitmap == null) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateBlog.this);
                    builder.setMessage(R.string.upload_blog_image);
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();

                } else if (title.isEmpty()) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateBlog.this);
                    builder.setMessage(R.string.add_title);
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (content.isEmpty()) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateBlog.this);
                    builder.setMessage(R.string.Add_Content);
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (tags.size() > 3) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateBlog.this);
                    builder.setMessage(R.string.tags_limit);
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    // preview window
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateBlog.this)
                            .setTitle(R.string.publish_string).setCancelable(false);

                    LayoutInflater layoutInflater = this.getLayoutInflater();
                    View dialogview = layoutInflater.inflate(R.layout.alert_dialog,null);
                    builder.setView(dialogview);

                    TextView title_preview = (TextView) dialogview.findViewById(R.id.blog_preview_title_TV);
                    TextView content_preview = (TextView) dialogview.findViewById(R.id.blog_preview_content_TV);
                    ImageView image_preview = (ImageView) dialogview.findViewById(R.id.blog_preview_image_IV);
                    TextView datetime_tv = (TextView) dialogview.findViewById(R.id.blog_preview_datetime_TV);

                    String datetime_string = find_datetime(content);
                    datetime_tv.setText(datetime_string);

                    Chip chip1 = (Chip) dialogview.findViewById(R.id.blog_preview_chip_1_CC);
                    Chip chip2 = (Chip) dialogview.findViewById(R.id.blog_preview_chip_2_CC);
                    Chip chip3 = (Chip) dialogview.findViewById(R.id.blog_preview_chip_3_CC);
                    Log.d("CreateBlog","Chip Ending");

                    String tags_string ;

                    if (tags.size()==1) {
                        chip2.setText(tags.get(0));
                        chip1.setVisibility(View.INVISIBLE);
                        chip3.setVisibility(View.INVISIBLE);
                        tags_string = tags.get(0);
                    } else if (tags.size()==2) {
                        chip1.setText(tags.get(0));
                        chip2.setText(tags.get(1));
                        chip3.setVisibility(View.INVISIBLE);
                        tags_string = tags.get(0) + "-" + tags.get(1);
                    } else if (tags.size()==3){
                        chip1.setText(tags.get(0));
                        chip2.setText(tags.get(1));
                        chip3.setText(tags.get(2));
                        tags_string = tags.get(0) + "-" + tags.get(1) + "-" + tags.get(2);

                    } else {
                        chip2.setText("No tags");
                        chip1.setVisibility(View.INVISIBLE);
                        chip3.setVisibility(View.INVISIBLE);
                        tags_string = "No tags";
                    }

                    Log.d("CreateBlog",tags_string);

                    image_preview.setImageBitmap(image_bitmap);
                    title_preview.setText(title);
                    content_preview.setText(content);

                    blog_details blog = new blog_details();
                    blog.setTitle(title);
                    blog.setDatetime(datetime_string);
                    blog.setContent(content);
                    blog.setTags(tags_string);

                    uploadBlog(image_bitmap,blog);

                    builder.setPositiveButton(R.string.publish_option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(CreateBlog.this,"Publish is clicked",Toast.LENGTH_SHORT).show();
                            //todo start a activity for result to take tags from user
                        }
                    })
                            .setNegativeButton(R.string.not_yet_string, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Toast.makeText(CreateBlog.this,"Not yet",Toast.LENGTH_SHORT).show();
                                    // todo write code for uploading data to the server

                                }
                            });
                    AlertDialog alert11 = builder.create();
                    alert11.show();

                }


                break;
        }

    }

    private String find_datetime(String content) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY");
        String date = dateFormat.format(calendar.getTime());
        String[] words = content.split(" ");

        // taking reading speed as 200 words/min
        String read_time = String.valueOf(Math.round(words.length/200.0));

        return date + "-" + read_time + "min read";
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    //upload bitmap and some other info with this
    private void uploadBlog(final Bitmap bitmap, final blog_details blog) {

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
                                Toast.makeText(CreateBlog.this, "Blog Published", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CreateBlog.this, BlogFeed.class));

                            } else {
                                Toast.makeText(CreateBlog.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(CreateBlog.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("TASK","upload_blog");
                params.put("title",blog.getTitle());
                params.put("datetime",blog.getDatetime());
                params.put("content",blog.getContent());
                params.put("tags",blog.getTags());
                return params;
            }

            //passing image by rename it uniquely

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws AuthFailureError {
                Map<String,DataPart> params = new HashMap<>();
                String imagename = SharedPrefmanager.getInstance(CreateBlog.this).getUser().getUsername();
                Log.d("Bitmap",bitmap.toString());
                params.put("pic", new DataPart(imagename+".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        volleySingleton.getInstance(CreateBlog.this).getRequestQueue().add(volleyMultipartRequest);

    }
}