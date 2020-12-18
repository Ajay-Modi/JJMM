package com.example.myblogapp.createblog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myblogapp.R;
import com.hootsuite.nachos.NachoTextView;

import java.util.List;

public class AddTags extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView cancel;
    private ImageView next;
    private NachoTextView tags_ntv;
    private List<String> tags;
    private final static int tags_code = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tags);

        toolbar = (Toolbar) findViewById(R.id.toolbar_TB_addtags);
        tags_ntv = (NachoTextView) findViewById(R.id.tags_NTV_addtags);

        setSupportActionBar(toolbar);

        cancel = (ImageView) findViewById(R.id.cancel_B_addtags);
        next = (ImageView) findViewById(R.id.next_B_addtags);

        String suggestion[] = {"Technology", "Mathematics", "Android", "AndroidDev", "Mind Peace", "Machines"};
        ArrayAdapter adapter = new ArrayAdapter(AddTags.this,R.layout.support_simple_spinner_dropdown_item,suggestion);
        tags_ntv.setAdapter(adapter);

        cancel.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.cancel_B_addtags:
                finish();
                Toast.makeText(AddTags.this,"Cross is clicked",Toast.LENGTH_SHORT).show();
                break;

            case R.id.next_B_addtags:
                tags = tags_ntv.getChipAndTokenValues();
                String notags ;

                if (tags.isEmpty()) {
                    notags = "NO_TAGS";
                    setResult(tags_code,new Intent().putExtra("tags",notags));
                    // TODO: 15-12-2020 number of tags selected is zero
                } else {
                    setResult(Activity.RESULT_OK,new Intent().putExtra("tags",tags.toString()));
                    finish();
                }
                break;
        }
    }
}