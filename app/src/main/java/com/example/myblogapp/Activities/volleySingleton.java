package com.example.myblogapp.Activities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class volleySingleton {
    private static volleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context ctx;

    public volleySingleton(Context ctx) {
        this.ctx = ctx;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized volleySingleton getInstance(Context context){
        if (mInstance==null){
            return new volleySingleton(context.getApplicationContext());
        }

        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue==null)
            mRequestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        return mRequestQueue;

    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
