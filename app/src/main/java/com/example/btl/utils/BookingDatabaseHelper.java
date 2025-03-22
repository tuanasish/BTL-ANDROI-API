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
                + COLUMN_TIME + " TEXT, "
                + COLUMN_TOTAL_PRICE + " INTEGER, "
                + COLUMN_BOOKED_DATE + " TEXT);");
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public long addBooking(TimeSlot slot, String bookedDate, int totalPrice) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIELD_NAME, slot.getFieldName());
        values.put(COLUMN_TIME, slot.getTime());
        values.put(COLUMN_TOTAL_PRICE, totalPrice);
        values.put(COLUMN_BOOKED_DATE, bookedDate);
        return db.insert(TABLE_BOOKINGS, null, values);
    }

    public List<TimeSlot> getBookings() {
        List<TimeSlot> bookings = new ArrayList<>();
        Cursor cursor = db.query(TABLE_BOOKINGS, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String fieldName = cursor.getString(cursor.getColumnIndex(COLUMN_FIELD_NAME));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                @SuppressLint("Range") int totalPrice = cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_PRICE));
                @SuppressLint("Range") String bookedDate = cursor.getString(cursor.getColumnIndex(COLUMN_BOOKED_DATE));
                bookings.add(new TimeSlot(fieldName, time, TimeSlot.BOOKED, totalPrice, bookedDate));
            }
            cursor.close();
        }
        return bookings;
    }
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

}
