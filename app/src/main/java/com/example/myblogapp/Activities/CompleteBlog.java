package com.example.myblogapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myblogapp.R;

public class CompleteBlog extends AppCompatActivity {
    private TextView blog_content_TV;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_blog);

        blog_content_TV = (TextView) findViewById(R.id.blog_content_TV);

        // create a bundle object to retrieve the data from extras
        bundle = getIntent().getExtras();
        blog_content_TV.setText((String) bundle.get("content"));

    }
}