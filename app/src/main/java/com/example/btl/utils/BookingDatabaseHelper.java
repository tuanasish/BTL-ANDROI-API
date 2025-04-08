package com.example.btl.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl.models.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class BookingDatabaseHelper {
    private static final String DATABASE_NAME = "bookings.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIELD_NAME = "field_name";
    private static final String COLUMN_FIELD_ADDRESS = "field_address";
    private static final String COLUMN_FIELD_NUMBER = "field_number";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_TOTAL_PRICE = "total_price";
    private static final String COLUMN_BOOKED_DATE = "booked_date";

    private SQLiteDatabase db;
    private final Context context;

    public BookingDatabaseHelper(Context context) {
        this.context = context;
    }

    public void open() throws SQLException {
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_BOOKINGS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIELD_NAME + " TEXT, "
                + COLUMN_FIELD_ADDRESS + " TEXT, "
                + COLUMN_FIELD_NUMBER + " TEXT, "
                + COLUMN_TIME + " TEXT, "
                + COLUMN_TOTAL_PRICE + " INTEGER, "
                + COLUMN_BOOKED_DATE + " TEXT);");
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    // ✅ Lưu đầy đủ thông tin booking
    public long addBooking(TimeSlot slot, String bookedDate, int totalPrice) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIELD_NAME, slot.getFieldID());
//        values.put(COLUMN_FIELD_ADDRESS, slot.getFieldAddress());
        values.put(COLUMN_FIELD_NUMBER, slot.getSlotID());
        values.put(COLUMN_TIME, slot.getTimeRange());
        values.put(COLUMN_TOTAL_PRICE, totalPrice);
        values.put(COLUMN_BOOKED_DATE, bookedDate);
        return db.insert(TABLE_BOOKINGS, null, values);
    }

    // ✅ Lấy toàn bộ danh sách đặt sân
    public List<TimeSlot> getBookings() {
        List<TimeSlot> bookings = new ArrayList<>();
        Cursor cursor = db.query(TABLE_BOOKINGS, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String fieldName = cursor.getString(cursor.getColumnIndex(COLUMN_FIELD_NAME));
                @SuppressLint("Range") String fieldAddress = cursor.getString(cursor.getColumnIndex(COLUMN_FIELD_ADDRESS));
                @SuppressLint("Range") String fieldNumber = cursor.getString(cursor.getColumnIndex(COLUMN_FIELD_NUMBER));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                @SuppressLint("Range") int totalPrice = cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_PRICE));
                @SuppressLint("Range") String bookedDate = cursor.getString(cursor.getColumnIndex(COLUMN_BOOKED_DATE));

//                TimeSlot slot = new TimeSlot(fieldName, time, TimeSlot.BOOKED, totalPrice, bookedDate);
//                slot.setFieldAddress(fieldAddress);
//                slot.setFieldNumber(fieldNumber);

//                bookings.add(slot);
            }
            cursor.close();
        }
        return bookings;
    }

    // ✅ Lấy tổng số tiền đã đặt
    public int getTotalPrice() {
        int total = 0;
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_TOTAL_PRICE + ") FROM " + TABLE_BOOKINGS, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                total = cursor.getInt(0);
            }
            cursor.close();
        }
        return total;
    }

    // ✅ Kiểm tra xem khung giờ đã được đặt trong ngày chưa
    public boolean isTimeSlotBooked(String date, String time) {
        String query = "SELECT 1 FROM " + TABLE_BOOKINGS +
                " WHERE " + COLUMN_BOOKED_DATE + "=? AND " + COLUMN_TIME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{date, time});
        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) cursor.close();
        return exists;
    }
}
