package com.example.myblogapp.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.myblogapp.Activities.volleySingleton;

import java.io.ByteArrayOutputStream;

public class ImageHandler {
    private Context ctx;
    public Bitmap bitmap=null;

    public String getBas64_string() {
        return bas64_string;
    }

    public String bas64_string=null;

    public ImageHandler(Context ctx) {
        this.ctx = ctx;
    }

    public void getImage(final String image_address) {
        ImageRequest imageRequest = new ImageRequest(image_address, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                encodeTobase64(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                Toast.makeText(ctx,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        volleySingleton.getInstance(ctx).getRequestQueue().add(imageRequest);
    }

    public void encodeTobase64(Bitmap bitmap) {
        Bitmap bitmap_temp = bitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap_temp.compress(Bitmap.CompressFormat.PNG,80,baos);
        byte[] stream = baos.toByteArray();

        this.bas64_string = Base64.encodeToString(stream,Base64.DEFAULT);
    }

}
