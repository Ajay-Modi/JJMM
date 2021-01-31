package com.example.myblogapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class blog_details implements Parcelable {
    private int blog_id;
    private String image_address;
    private String title;
    private String datetime;
    private String content;
    private String tags;
    private String written_by;
    private Boolean is_liked;
    private Boolean is_bookmarked;
    private String writer_username;
    private String writer_profile_image_address;
    private int like_count;
    private int comment_count;

    public blog_details() {
    }

    protected blog_details(Parcel in) {
        blog_id = in.readInt();
        image_address = in.readString();
        title = in.readString();
        datetime = in.readString();
        content = in.readString();
        tags = in.readString();
        written_by = in.readString();
        byte tmpIs_liked = in.readByte();
        is_liked = tmpIs_liked == 0 ? null : tmpIs_liked == 1;
        byte tmpIs_bookmarked = in.readByte();
        is_bookmarked = tmpIs_bookmarked == 0 ? null : tmpIs_bookmarked == 1;
        like_count = in.readInt();
        comment_count = in.readInt();
        writer_username = in.readString();
        writer_profile_image_address = in.readString();
    }

    public static final Creator<blog_details> CREATOR = new Creator<blog_details>() {
        @Override
        public blog_details createFromParcel(Parcel in) {
            return new blog_details(in);
        }

        @Override
        public blog_details[] newArray(int size) {
            return new blog_details[size];
        }
    };

    public String getWritten_by() {
        return written_by;
    }

    public void setWritten_by(String written_by) {
        this.written_by = written_by;
    }

    public Boolean getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(Boolean is_liked) {
        this.is_liked = is_liked;
    }

    public Boolean getIs_bookmarked() {
        return is_bookmarked;
    }

    public void setIs_bookmarked(Boolean is_bookmarked) {
        this.is_bookmarked = is_bookmarked;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getWriter_username() {
        return writer_username;
    }

    public void setWriter_username(String writer_username) {
        this.writer_username = writer_username;
    }

    public String getWriter_profile_image_address() {
        return writer_profile_image_address;
    }

    public void setWriter_profile_image_address(String writer_profile_image_address) {
        this.writer_profile_image_address = writer_profile_image_address;
    }



    public int getBlog_id() { return blog_id; }

    public void setBlog_id(int blog_id) { this.blog_id = blog_id; }

    public String getImage_address() {
        return image_address;
    }

    public void setImage_address(String image_address) {
        this.image_address = image_address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(blog_id);
        dest.writeString(image_address);
        dest.writeString(title);
        dest.writeString(datetime);
        dest.writeString(content);
        dest.writeString(tags);
        dest.writeString(written_by);
        dest.writeByte((byte) (is_liked == null ? 0 : is_liked ? 1 : 2));
        dest.writeByte((byte) (is_bookmarked == null ? 0 : is_bookmarked ? 1 : 2));
        dest.writeInt(like_count);
        dest.writeInt(comment_count);
        dest.writeString(writer_username);
        dest.writeString(writer_profile_image_address);
    }
}
