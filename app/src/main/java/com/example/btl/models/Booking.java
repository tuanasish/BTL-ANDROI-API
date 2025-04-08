package com.example.btl.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.sql.Time;
import java.util.Date;

public class Booking implements Parcelable {
    private int ID;
    private int userID;
    private int fieldID;
    private int courtID;
    private String date;
    private String startTime;
    private String endTime;
    private String status;
    private double totalPrice;
    private String createdAt;
    private String updatedAt;

    public Booking(int ID, int userID, int fieldID, int courtID, String date,
                   String startTime, String endTime, String status,
                   double totalPrice, String createdAt, String updatedAt) {
        this.ID = ID;
        this.userID = userID;
        this.fieldID = fieldID;
        this.courtID = courtID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    protected Booking(Parcel in) {
        ID = in.readInt();
        userID = in.readInt();
        fieldID = in.readInt();
        courtID = in.readInt();
        date = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        status = in.readString();
        totalPrice = in.readDouble();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(userID);
        dest.writeInt(fieldID);
        dest.writeInt(courtID);
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(status);
        dest.writeDouble(totalPrice);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    // Getters and Setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFieldID() {
        return fieldID;
    }

    public void setFieldID(int fieldID) {
        this.fieldID = fieldID;
    }

    public int getCourtID() {
        return courtID;
    }

    public void setCourtID(int courtID) {
        this.courtID = courtID;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "ID=" + ID +
                ", userID=" + userID +
                ", fieldID=" + fieldID +
                ", courtID=" + courtID +
                ", date='" + date + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", status='" + status + '\'' +
                ", totalPrice=" + totalPrice +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}