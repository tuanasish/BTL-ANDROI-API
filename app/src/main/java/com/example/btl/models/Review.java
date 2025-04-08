package com.example.btl.models;

import java.sql.Timestamp;

public class Review {
    private int review_id;
    private int user_id;
    private String user_name;
    private int field_id;
    private int rating;
    private String comment;
    private String review_date;

    public Review(int user_id, int field_id, int rating, String comment, String review_date) {
        this.user_id = user_id;
        this.field_id = field_id;
        this.rating = rating;
        this.comment = comment;
        this.review_date = review_date;
    }
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview_date() {
        return review_date;
    }

    public void setReview_date(String review_date) {
        this.review_date = review_date;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
