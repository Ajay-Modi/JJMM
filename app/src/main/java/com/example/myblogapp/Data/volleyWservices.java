package com.example.myblogapp.Data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myblogapp.Model.users_details;

import org.json.JSONException;
import org.json.JSONObject;

public class volleyWservices {

    private static final String URL = "http://192.168.3.156/phptesting/wservices/";
    private Context context;
    private RequestQueue requestQueue;
    Boolean createUser_pass = null;
    private users_details userFromDB=null;

    public volleyWservices(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(this.context);

    }

    public Boolean createUser(users_details user) {

        final String postURL = URL + "registerUser.php";

        JSONObject postValues = new JSONObject();

        try {

            postValues.put("TASK", "PARTIAL_INFO");
            postValues.put("email",user.getEmail());
            postValues.put("username",user.getUsername());
            postValues.put("password",user.getPassword());

        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postURL, postValues, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Integer res = response.getInt("success");

                    Log.d("volleyWservices",response.getString("message"));
                    System.out.print(response.getString("message"));
//                        userFromDB.setFull_name(response.getString("message"));

                    if (res==1) {
                        createUser_pass = true;
                    } else {
                        createUser_pass = false;
                    }
                } catch (JSONException e) {

                    Log.d("volleyWservices", e.getMessage());

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volleyWservices",error.getMessage());
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);


        return createUser_pass;
    }
}
