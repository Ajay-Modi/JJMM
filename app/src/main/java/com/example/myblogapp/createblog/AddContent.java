
package com.example.myblogapp.createblog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myblogapp.R;

public class AddContent extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView cancel;
    private ImageView next;
    private EditText content_et;
    private String content;
    private final static int content_code=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);

        toolbar = (Toolbar) findViewById(R.id.toolbar_TB_addcontent);

        setSupportActionBar(toolbar);

        content_et = (EditText) findViewById(R.id.content_ET_addcontent);
        cancel = (ImageView) findViewById(R.id.cancel_B_addcontent);
        next = (ImageView) findViewById(R.id.next_B_addcontent);

        String temp_content = getIntent().getStringExtra("content");

        content_et.setText(temp_content);

        cancel.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.cancel_B_addcontent:
                finish();
                break;

            case R.id.next_B_addcontent:

                content = content_et.getText().toString().trim();
                if (content.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddContent.this);
                    builder.setMessage(R.string.Add_Content);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(AddContent.this,R.string.content_added,Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK,new Intent().putExtra("content",content));
                    finish();
                }
                break;

        }

    }
}