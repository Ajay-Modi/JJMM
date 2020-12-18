package com.example.myblogapp.Util;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myblogapp.R;

public class LoaderDialog {

    private ProgressBar progressBar;
    private TextView textView;
    private Activity activity;
    private RelativeLayout relativeLayout;


    Animation anim;

    public LoaderDialog (Activity activity) {
        this.activity=activity;

    }

    public void startLoader(){
        relativeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.progress_dialog,null);
        progressBar = (ProgressBar) relativeLayout.findViewById(R.id.progressbar);
        textView = (TextView) relativeLayout.findViewById(R.id.textview);
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Please wait...");


    }

    public void stopLoader(){
        progressBar.setVisibility(View.GONE);
        textView.setText("Done");
    }
}
