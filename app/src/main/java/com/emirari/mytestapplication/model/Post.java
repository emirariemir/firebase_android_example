package com.emirari.mytestapplication.model;

public class Post {
    public String userMail;
    public String caption;
    public String imageUrl;

    public Post(String userMail, String caption, String imageUrl) {
        this.userMail = userMail;
        this.caption = caption;
        this.imageUrl = imageUrl;
    }
}
