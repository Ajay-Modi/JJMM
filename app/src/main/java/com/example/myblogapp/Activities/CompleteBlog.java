package com.example.myblogapp.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.myblogapp.Model.blog_details;
import com.example.myblogapp.R;

public class CompleteBlog extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView go_back;
//    private ImageView vertical_more;

    private TextView blog_title;

    private ImageView writer_image;
    private TextView writer_name;
    private TextView datetime_string;

    private ImageView blog_image;

    private TextView blog_content;

    private TextView blog_tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_blog);

        toolbar = (Toolbar) findViewById(R.id.toolbar_TB_complete_blog);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        go_back = (ImageView) toolbar.findViewById(R.id.back_IV_complete_blog);
        go_back.setOnClickListener(this);
//        vertical_more = (ImageView) toolbar.findViewById(R.id.more_vertical_IV_complete_blog);

        blog_title = (TextView) findViewById(R.id.title_string_TV_complete_blog);
        blog_image = (ImageView) findViewById(R.id.blog_main_image_IV_complete_blog);
        writer_image = (ImageView) findViewById(R.id.writter_image_IV_complete_blog);
        writer_name = (TextView) findViewById(R.id.writer_name_TV_complete_blog);
        datetime_string = (TextView) findViewById(R.id.datetime_TV_complete_blog);

         blog_content = (TextView) findViewById(R.id.content_TV_complete_blog);
         blog_tags = (TextView) findViewById(R.id.tags_string_TV_complete_blog);

         blog_details blog = getIntent().getExtras().getParcelable("blog");
         retrieveBase64(blog.getImage_address(), blog_image);
         retrieveBase64(blog.getWriter_profile_image_address(), writer_image);
         writer_name.setText(blog.getWriter_username());
         blog_title.setText(blog.getTitle());
         datetime_string.setText(blog.getDatetime());
         blog_content.setText(blog.getContent());

    }

    private void retrieveBase64(String image_address, final ImageView view) {
        ImageRequest imageRequest = new ImageRequest(image_address, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                view.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
            }
        });

        volleySingleton.getInstance(CompleteBlog.this).getRequestQueue().add(imageRequest);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_IV_complete_blog:
                finish();
                break;
        }

    }
}