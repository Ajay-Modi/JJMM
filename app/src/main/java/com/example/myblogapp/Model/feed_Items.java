package com.example.myblogapp.Model;


// class for data interface

public class feed_Items {
    private String blog_title;
    private String blog_writer;
    private String blog_content;
    private int blog_main_image;
    private String datetime;

    public feed_Items( String blog_title, String blog_content, String blog_writer, int blog_main_image, String reading_time) {
        this.blog_title = blog_title;
        this.blog_content = blog_content;
        this.blog_writer = blog_writer;
        this.blog_main_image = blog_main_image;
    }

    // all getter and setter

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String reading_time) {
        this.datetime = reading_time;
    }


    public String getBlog_title() {
        return blog_title;
    }

    public void setBlog_title(String blog_title) {
        this.blog_title = blog_title;
    }

    public String getBlog_writer() {
        return blog_writer;
    }

    public void setBlog_writer(String blog_writer) {
        this.blog_writer = blog_writer;
    }

    public String getBlog_content() {
        return blog_content;
    }

    public void setBlog_content(String blog_content) {
        this.blog_content = blog_content;
    }

    public int getBlog_main_image() {
        return blog_main_image;
    }

    public void setBlog_main_image(int blog_main_image) {
        this.blog_main_image = blog_main_image;
    }
}
