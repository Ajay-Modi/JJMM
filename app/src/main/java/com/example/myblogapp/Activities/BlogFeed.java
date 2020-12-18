package com.example.myblogapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.myblogapp.Adapter.myadapter;
import com.example.myblogapp.Model.feed_Items;
import com.example.myblogapp.Model.users_details;
import com.example.myblogapp.R;
import com.example.myblogapp.createblog.CreateBlog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class BlogFeed extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //make a private variable to hold reference of hamburger
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoggle;
    private NavigationView navigationView;

    //action bar header
    private View header;
    private ImageView profile_image_header; // TODO: 07-12-2020 upload image and retrieve
    private TextView fullname_header;

    //notification bell


    // primary data for feed Items like title, name of artist, reading time, main photo of blog
    private String[] titile = {"your ad creative has a shelf life too.", "Technology is Building our Future", "How to make a Android Application for Blog"};
    private String[] blog_content = {"The view holder objects are managed by an adapter, which you create by extending RecyclerView.Adapter. The adapter creates view holders as needed. The adapter also binds the view holders to their data",
            "The views in the list are represented by view holder objects. These objects are instances of a class you define by extending RecyclerView.ViewHolder",
            " Each view holder is in charge of displaying a single item with a view. For example, if your list shows music collection, each view holder might represent a single album. The RecyclerView creates only as many view holders as are needed to display the on-screen portion of the dynamic content"};
    private String[] name_artist = {"Jon Snow", "Edward", "Bear Gyrlls"};
    private String[] reading_time = {"10 min read", "3 min read", "1 min read"};
    int[] main_images = {R.drawable.blog_sample_1, R.drawable.blog_sample_2, R.drawable.sample_6};

    private List<feed_Items> myDataSet;

    private RecyclerView myrecyclerView;

    private myadapter adapter;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_feed);

        getSupportActionBar().setTitle(R.string.blog_feed_actionbar_name);

        mdrawerlayout = (DrawerLayout) findViewById(R.id.activity_blog_feed);
        mtoggle = new ActionBarDrawerToggle(BlogFeed.this,mdrawerlayout,R.string.open,R.string.close);
        mdrawerlayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigationBar);
        navigationView.setNavigationItemSelectedListener(this);

        //fetch header ID
        header = navigationView.getHeaderView(0);
        profile_image_header = (ImageView) header.findViewById(R.id.profile_image_header);
        fullname_header = (TextView) header.findViewById(R.id.fullname_header);

        users_details user = SharedPrefmanager.getInstance(BlogFeed.this).getUser();

        fullname_header.setText("@"+user.getUsername());
        retrieveBase64(user.getProfile_image_address(),profile_image_header);

//        Log.d("BLOGFEED",user.getEmail());

        Log.d("BLOGFEED","Image and Username setted");

        // implement fab
        fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BlogFeed.this,"Write your Blog",Toast.LENGTH_LONG).show();
                startActivity(new Intent(BlogFeed.this, CreateBlog.class));
            }
        });
        // first create dataset which I have to display on the BlogFeed activity
        myDataSet = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            feed_Items temp = new feed_Items(titile[i%3], blog_content[i%3], name_artist[i%3], main_images[i%3], reading_time[i%3]);
            myDataSet.add(temp);
        }

        // initialize the recyclerView
        myrecyclerView = (RecyclerView) findViewById(R.id.myrecyclerView);


        // create a Linear Layout Manager for managing all the things like overriding method in adpater
        myrecyclerView.setHasFixedSize(true);
        myrecyclerView.setLayoutManager(new LinearLayoutManager(getParent()));

        // create object of Adapter class for everything
        adapter = new myadapter(this,myDataSet);

        // set this adapter on RecyclerView
        myrecyclerView.setAdapter(adapter);

    }

    private void retrieveBase64(String image_address, final ImageView view) {
        Log.d("URL :",image_address);
        ImageRequest imageRequest = new ImageRequest(image_address, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                view.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                Toast.makeText(BlogFeed.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        volleySingleton.getInstance(BlogFeed.this).getRequestQueue().add(imageRequest);

//        //todo we should add check for string not to be null
//        String image_base64 = image_base;
//        byte[] b = Base64.decode(image_base64,Base64.DEFAULT);
//        InputStream inputStream = new ByteArrayInputStream(b);
//        return BitmapFactory.decodeStream(inputStream);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){

            case R.id.home :
                mdrawerlayout.closeDrawer(Gravity.LEFT);
                Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
                break;

            case R.id.account:
                Toast.makeText(this,"Account",Toast.LENGTH_SHORT).show();
                break;

            case R.id.search:
                Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
                break;

            case R.id.yourblogs:
                Toast.makeText(this,"Blogs",Toast.LENGTH_SHORT).show();
                break;

            case R.id.bookmarks:
                Toast.makeText(this,"bookmarks",Toast.LENGTH_SHORT).show();
                break;

            case R.id.setting:
                Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:
                Toast.makeText(this,"Bye!",Toast.LENGTH_SHORT).show();
                SharedPrefmanager.getInstance(BlogFeed.this).logout();
                break;

        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification,menu);
        //notification bell
        // menu is a view group
        final MenuItem bell = menu.findItem(R.id.notificationButton);

        onOptionsItemSelected(bell);

        // TODO: 07-12-2020 Notification Task is pending

        return true;
    }
}