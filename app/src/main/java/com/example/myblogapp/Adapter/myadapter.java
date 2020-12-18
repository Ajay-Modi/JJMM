package com.example.myblogapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myblogapp.Activities.CompleteBlog;
import com.example.myblogapp.Model.feed_Items;
import com.example.myblogapp.R;

import java.util.List;

public class myadapter extends RecyclerView.Adapter<myadapter.ViewHolder>{
    // data to set on each recycler item
    private List<feed_Items> mydataset;
    private Context context;

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


        feed_Items data = mydataset.get(position);
        holder.title_string.setText(data.getBlog_title());
        //holder.datetime.setText(data.getDatetime());
        holder.main_image.setImageResource(data.getBlog_main_image());
        holder.blog_content_string.setText(data.getBlog_content());

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
        public TextView writter_name;
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
            writter_name = (TextView) itemView.findViewById(R.id.writter_name);
            blog_content_string = (TextView) itemView.findViewById(R.id.blog_content_string);
            like_IV = (ImageView) itemView.findViewById(R.id.like_IV);
            comment_IV = (ImageView) itemView.findViewById(R.id.comment_IV);
            share_IV = (ImageView) itemView.findViewById(R.id.share_IV);
            bookmark_IV = (ImageView) itemView.findViewById(R.id.bookmark_IV);
        }

        @Override
        public void onClick(View v) {
            //find out the position in adapter where view is clicked
            int position = getAdapterPosition();
            feed_Items temp = mydataset.get(position);

            // write code when view is clicked then it will open a new activity which full blog
            Intent complete_blog = new Intent(context, CompleteBlog.class);

            //add info with intent whatever we want to send or receive
            complete_blog.putExtra("content",temp.getBlog_content());

            // startActivity
            context.startActivity(complete_blog);


        }
    }
}