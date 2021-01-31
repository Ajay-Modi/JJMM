package com.example.myblogapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.myblogapp.Activities.CompleteBlog;
import com.example.myblogapp.Activities.volleySingleton;
import com.example.myblogapp.Model.blog_details;
import com.example.myblogapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myadapter extends RecyclerView.Adapter<myadapter.ViewHolder>{
    // data to set on each recycler item
    private List<blog_details> mydataset;
    private Context context;
    private Map<Integer, Bitmap> images = new HashMap<>();

    public myadapter(Context context, List mydataset) {
        this.context = context;
        this.mydataset = mydataset;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        //  Creating view from the xml file
        View view = LayoutInflater.from(context).inflate(R.layout.blog_cardview_2,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        // here we can make it generalise by making adapter class variables and List of objects containing blogfeed class]

        blog_details data = mydataset.get(position);

        holder.main_image.setImageDrawable(null);

        if (images.containsKey(data.getBlog_id())){
            holder.main_image.setImageBitmap(images.get(data.getBlog_id()));
        } else  {
             retrieveBase64(data.getImage_address(), holder.main_image, data.getBlog_id());
        }

        holder.title_string.setText(data.getTitle());
        holder.blog_content_string.setText(data.getContent());
        holder.datetime_string.setText(data.getDatetime());
        holder.writer_name.setText(data.getWriter_username());

        Log.d("IsLIKED", data.getBlog_id() + data.getIs_liked().toString());

        if (data.getIs_liked() == true) {
            holder.like_IV.setImageResource(R.drawable.red_heart);
            Log.d("IsLIKED", data.getBlog_id() + " " + data.getTitle() + data.getIs_liked().toString());
        } else {
            holder.like_IV.setImageResource(R.drawable.outline_favorite_border_black_18dp);
        }

        if (data.getIs_bookmarked() == true) {
            holder.bookmark_IV.setImageResource(R.drawable.bookmarked_image);
        } else {
            holder.bookmark_IV.setImageResource(R.drawable.bookmarks_outline);
        }

    }


    @Override
    public int getItemCount() {
        return mydataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // this class is mainly holder state of the widgets

        // Declare variable for the items of the recyclerView
        public ImageView main_image;
        public TextView title_string;
        public TextView datetime_string;
        public TextView writer_name;
        public TextView blog_content_string;
        public ImageView like_IV;
        public ImageView comment_IV;
        public ImageView share_IV;
        public ImageView bookmark_IV;



        public ViewHolder( View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            // find the widgets from the itemView and create reference of those which we can use in future
            main_image = (ImageView)  itemView.findViewById(R.id.main_image);
            title_string = (TextView) itemView.findViewById(R.id.title_string_TextView);
            datetime_string = (TextView) itemView.findViewById(R.id.datetime_string);
            writer_name = (TextView) itemView.findViewById(R.id.writter_name);
            blog_content_string = (TextView) itemView.findViewById(R.id.blog_content_string);
            like_IV = (ImageView) itemView.findViewById(R.id.like_IV);
            comment_IV = (ImageView) itemView.findViewById(R.id.comment_IV);
            share_IV = (ImageView) itemView.findViewById(R.id.share_IV);
            bookmark_IV = (ImageView) itemView.findViewById(R.id.bookmark_IV);


            writer_name.setOnClickListener(this);
            like_IV.setOnClickListener(this);
            comment_IV.setOnClickListener(this);
            share_IV.setOnClickListener(this);
            bookmark_IV.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //find out the position in adapter where view is clicked
            int id = v.getId();
            int position = getAdapterPosition();
            blog_details blog = mydataset.get(position);

            switch (id) {
                case R.id.writter_name:
                    Toast.makeText(context,"writer name is clicked",Toast.LENGTH_SHORT).show();

                case R.id.comment_IV:
                    Toast.makeText(context,"comment is clicked",Toast.LENGTH_SHORT).show();
                    break;

                case R.id.like_IV:
                    if (blog.getIs_liked()) {
                        like_IV.setImageResource(R.drawable.outline_favorite_border_black_18dp);
                        blog.setIs_liked(false);
                    } else {
                        like_IV.setImageResource(R.drawable.red_heart);
                        blog.setIs_liked(true);
                    }
                    break;

                case R.id.share_IV:
                    Toast.makeText(context,"share is clicked",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "Adapter");
                    intent.putExtra(Intent.EXTRA_TEXT, "Hello from blogger...");
                    intent.setType("text/plain");
                    context.startActivity(intent.createChooser(intent, "Share"));
                    break;

                case R.id.bookmark_IV:
                    if (blog.getIs_bookmarked()) {
                        bookmark_IV.setImageResource(R.drawable.bookmarks_outline);
                        blog.setIs_bookmarked(false);
                    } else {
                        bookmark_IV.setImageResource(R.drawable.bookmarked_image);
                        blog.setIs_bookmarked(true);
                    }
                    break;

                default:
                    blog_details temp = mydataset.get(position);

                    Intent complete_blog = new Intent(context, CompleteBlog.class);
                    complete_blog.putExtra("blog", temp);

                    context.startActivity(complete_blog);


            }


        }
    }

    //fetch blog main image
    private void retrieveBase64(String image_address, final ImageView view, final Integer blog_id) {
        Log.d("URL :",image_address);
        ImageRequest imageRequest = new ImageRequest(image_address, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                view.setImageBitmap(response);
                images.put(blog_id, response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        volleySingleton.getInstance(context).getRequestQueue().add(imageRequest);
    }

    //clear all elements of the recyclerview
    public void clear() {
        mydataset.clear();

    }
}