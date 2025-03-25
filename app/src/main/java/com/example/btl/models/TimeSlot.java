package com.example.btl.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeSlot implements Parcelable {
    public static final int AVAILABLE = 0;
    public static final int BOOKED = 1;
    public static final int LOCKED = 2;

    private String fieldName;     // Tên sân
    private String time;          // Khung giờ
    private int status;           // Trạng thái đặt sân
    private boolean selected;     // Đánh dấu chọn sân
    private int totalPrice;       // Tổng tiền
    private String bookedDate;    // Ngày đặt
    private String fieldAddress;  // Địa chỉ sân
    private String fieldNumber;   // SĐT sân

    // Constructor chính
    public TimeSlot(String fieldName, String time, int status, int totalPrice, String bookedDate) {
        this.fieldName = fieldName;
        fixFieldName();
        this.time = time;
        this.status = status;
        this.totalPrice = totalPrice;
        this.bookedDate = bookedDate;
        this.selected = false;
    }

    // Constructor từ Parcel
    protected TimeSlot(Parcel in) {
        fieldName = in.readString();
        time = in.readString();
        status = in.readInt();
        totalPrice = in.readInt();
        bookedDate = in.readString();
        selected = in.readByte() != 0;
        fieldAddress = in.readString();
        fieldNumber = in.readString();
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

    // Getter & Setter
    public String getFieldName() {
        return fieldName;
    }

    public String getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public String getFieldAddress() {
        return fieldAddress;
    }

    public String getFieldNumber() {
        return fieldNumber;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggleSelected() {
        this.selected = !this.selected;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setFieldAddress(String fieldAddress) {
        this.fieldAddress = fieldAddress;
    }

    public void setFieldNumber(String fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    public void fixFieldName() {
        if (fieldName.contains("Sân nhỏ")) {
            fieldName = fieldName.replace("Sân nhỏ", "PickelBall");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fieldName);
        dest.writeString(time);
        dest.writeInt(status);
        dest.writeInt(totalPrice);
        dest.writeString(bookedDate);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(fieldAddress);
        dest.writeString(fieldNumber);
    }
}
