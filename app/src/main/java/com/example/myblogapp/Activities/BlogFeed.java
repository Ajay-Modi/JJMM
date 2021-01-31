package com.example.myblogapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.myblogapp.Adapter.myadapter;
import com.example.myblogapp.Model.blog_details;
import com.example.myblogapp.Model.users_details;
import com.example.myblogapp.R;
import com.example.myblogapp.createblog.CreateBlog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.myblogapp.Util.Constants.BaseURL;

public class BlogFeed extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //make a private variable to hold reference of hamburger
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoggle;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeButton;
    private SpinKitView progressBar;
    private Boolean fetch_data_or_not = true;

    //load more functionality
    Boolean isScrolling = false;
    int currentItems, scrolledItems, totalItems;

    //starting blog_id
    private int starting_blog_id = 0;


    //action bar header
    private View header;
    private ImageView profile_image_header; // TODO: 07-12-2020 upload image and retrieve
    private TextView fullname_header;

    //notification bell

    private List<blog_details> blog_list = new ArrayList<>();

    private RecyclerView myrecyclerView;

    private RecyclerView.LayoutManager layoutManager;

    private myadapter adapter;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_feed);

        getSupportActionBar().setTitle(R.string.blog_feed_actionbar_name);

        mdrawerlayout = (DrawerLayout) findViewById(R.id.activity_blog_feed);
        swipeButton = (SwipeRefreshLayout) findViewById(R.id.swipe_button_blog_feed);
        progressBar = (SpinKitView) findViewById(R.id.indeterminate_PB_Blog_Feed);

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

        // initialize the recyclerView
        myrecyclerView = (RecyclerView) findViewById(R.id.myrecyclerView);


        // create a Linear Layout Manager for managing all the things like overriding method in adpater
        myrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(BlogFeed.this);
        myrecyclerView.setLayoutManager(layoutManager);

        // create object of Adapter class for everything
        // TODO: 16-01-2021 Completed task --- set dataset to the adapter!

        // set this adapter on RecyclerView
        adapter = new myadapter(BlogFeed.this,blog_list );

        myrecyclerView.setAdapter(adapter);

        new_blogs();

        // load more functionality
        myrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItems = layoutManager.getItemCount();
                currentItems = layoutManager.getChildCount();
                scrolledItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                if (isScrolling && totalItems == currentItems + scrolledItems) {
                    Log.d("RES -- SCROLLING", scrolledItems + " " + currentItems + " = " + totalItems);
                    isScrolling = false;
                    new_blogs();

                }
            }
        });

        //implement functionality of swipe to refresh button
        swipeButton.setColorSchemeColors(getColor(R.color.colorAccent));

        swipeButton.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO: 16-01-2021 in this, we want to fetch recently uploaded blogs!
                swipeButton.setRefreshing(false);
            }
        });
    }

    private void new_blogs() {

        Log.d("RESPONSE", fetch_data_or_not.toString());

        if (fetch_data_or_not == false) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        users_details user = SharedPrefmanager.getInstance(BlogFeed.this).getUser();

        Log.d("RESPONSE BLOGFEED", user.getUsername()+ "  " + user.getPassword() + " " + starting_blog_id);
        Log.d("RESPONSE", blog_list.toString());
        final String URL = BaseURL + "?TASK=get_blogs&username="+user.getUsername()+"&password="+user.getPassword()+"&starting_blog_id="+starting_blog_id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // if no error in the response

                    if (jsonObject.getInt("success") == 1) {

                        JSONArray jsonArray = jsonObject.getJSONArray("blogs");

                        if (jsonArray.length() == 0) {
                            progressBar.setVisibility(View.GONE);
                            fetch_data_or_not = false;
                            return ;
                        }


                        //getting user from response
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject blogObject = jsonArray.getJSONObject(i);
                            blog_details blog = new blog_details();
                            blog.setBlog_id(blogObject.getInt("id"));
                            blog.setTitle(blogObject.getString("title"));
                            blog.setDatetime(blogObject.getString("date_readtime"));
                            blog.setContent(blogObject.getString("content"));
                            blog.setWritten_by(blogObject.getString("written_by"));
                            blog.setImage_address(blogObject.getString("image_address"));
                            blog.setLike_count(blogObject.getInt("like_count"));
                            blog.setComment_count(blogObject.getInt("comment_count"));
                            blog.setWriter_username(blogObject.getString("writer_name"));
                            blog.setWriter_profile_image_address(blogObject.getString("writer_profile_image_address"));

                            if (blogObject.getInt("is_liked") == 0) {
                                blog.setIs_liked(false);
                            } else {
                                blog.setIs_liked(true);
                            }

                            if (blogObject.getInt("is_bookmarked") == 0) {
                                blog.setIs_bookmarked(false);
                            } else {
                                blog.setIs_bookmarked(true);
                            }

                            blog_list.add(blog);
                            adapter.notifyDataSetChanged();

                            Log.d("BLOG", blog.getTitle() + "  " + i+" "+jsonArray.length());

                            if (i == jsonArray.length()-1) {
                                Log.d("RESPONSE - JSONARRAY", jsonArray.toString());
                                starting_blog_id = blogObject.getInt("id");
                                Log.d("BLOG", blog.getTitle() + "  " + blog_list);
                            }
                            progressBar.setVisibility(View.GONE);
                        }

                        //just show these blogs
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(BlogFeed.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Toast.makeText(BlogFeed.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        
        } , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                error.getStackTrace();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        volleySingleton.getInstance(this).getRequestQueue().add(stringRequest);
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