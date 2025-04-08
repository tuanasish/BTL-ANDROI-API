package com.example.btl.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeSlot implements Parcelable {
    public static final int AVAILABLE = 0;
    public static final int BOOKED = 1;
    public static final int LOCKED = 2;

    private int slotID;
    private int fieldID;
    private Date bookingDate;
    private String startTime;
    private String endTime;
    private int status;
    private boolean isSelected;

    public TimeSlot(int slotID, int fieldID, Date bookingDate,
                    String startTime, String endTime, int status) {
        this.slotID = slotID;
        this.fieldID = fieldID;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.isSelected = false;
    }

    protected TimeSlot(Parcel in) {
        slotID = in.readInt();
        fieldID = in.readInt();
        bookingDate = new Date(in.readLong());
        startTime = in.readString();
        endTime = in.readString();
        status = in.readInt();
        isSelected = in.readByte() != 0;
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

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public int getFieldID() {
        return fieldID;
    }

    public void setFieldID(int fieldID) {
        this.fieldID = fieldID;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(slotID);
        dest.writeInt(fieldID);
        dest.writeLong(bookingDate != null ? bookingDate.getTime() : -1);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeInt(status);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "slotID=" + slotID +
                ", fieldID=" + fieldID +
                ", bookingDate=" + bookingDate +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    public String getTimeRange() {
        try {
            // Format lại chỉ hiển thị giờ:phút
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat originalFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            Date start = originalFormat.parse(startTime);
            Date end = originalFormat.parse(endTime);

            return displayFormat.format(start) + " - " + displayFormat.format(end);
        } catch (Exception e) {
            return startTime + " - " + endTime;
        }
    }
    // Thêm các phương thức quản lý trạng thái chọn
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void toggleSelected() {
        isSelected = !isSelected;
    }
}