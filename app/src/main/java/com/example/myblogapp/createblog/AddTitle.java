package com.example.myblogapp.createblog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myblogapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddTitle extends AppCompatActivity implements View.OnClickListener {
    private ImageView cancel;
    private ImageView next;
    private Toolbar toolbar;
    private TextInputLayout title_et;
    private TextInputEditText title_eti;
    private final static int title_code=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_title);

        toolbar = (Toolbar) findViewById(R.id.toolbar_TB_addtitle);
        setSupportActionBar(toolbar);

        cancel = (ImageView) findViewById(R.id.cancel_B_addtitle);
        next = (ImageView) findViewById(R.id.next_B_addtitle);
        title_et = (TextInputLayout) findViewById(R.id.title_TIL_addtitle);
        title_eti = (TextInputEditText) findViewById(R.id.title_TIL_ET);

        String temp_content = getIntent().getStringExtra("title");

        title_eti.setText(temp_content);


        cancel.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.cancel_B_addtitle:
                finish();
                break;


            case R.id.next_B_addtitle:
                String title = title_eti.getText().toString().trim();
                Log.d("AddTitle",title);

                if (title.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddTitle.this);
                    builder.setMessage(R.string.Add_title);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (title.length() > 60) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddTitle.this);
                    builder.setMessage(R.string.word_limit);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    setResult(Activity.RESULT_OK,new Intent().putExtra("title",title));
                    finish();
                }

                break;
        }
    }
}