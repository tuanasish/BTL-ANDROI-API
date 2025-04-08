package com.example.btl.models;

import com.google.gson.annotations.SerializedName;

public class BookingRequest {
    @SerializedName("field_id")
    private int fieldId;

    @SerializedName("date")
    private String date;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("court_id")
    private int courtId;

    @SerializedName("total_price")
    private double totalPrice;

    public BookingRequest(int fieldId, String date, String startTime, String endTime, int userId, int courtId, double totalPrice) {
        this.fieldId = fieldId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.courtId = courtId;
        this.totalPrice = totalPrice;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
