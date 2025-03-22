package com.example.btl.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.sql.Date;
import java.sql.Time;

public class TimeSlot implements Parcelable {
    public static final int AVAILABLE = 0;
    public static final int BOOKED = 1;
    public static final int LOCKED = 2;

    private int slot_id;
    private int field_id;
    private Date booking_date;
    private Time start_time;
    private Time end_time;
    private String status; // Ngày đặt


    protected TimeSlot(Parcel in) {
        slot_id = in.readInt();
        field_id = in.readInt();
        status = in.readString();
    }

    public static final Creator<TimeSlot> CREATOR = new Creator<TimeSlot>() {
        @Override
        public TimeSlot createFromParcel(Parcel in) {
            return new TimeSlot(in);
        }

        @Override
        public TimeSlot[] newArray(int size) {
            return new TimeSlot[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(slot_id);
        dest.writeInt(field_id);
        dest.writeString(status);
    }
}
