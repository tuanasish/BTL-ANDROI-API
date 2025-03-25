package com.example.btl.models;

import java.sql.Timestamp;

public class Booking {
    private int booking_id;
    private int user_id;
    private int field_id;
    private int count_id;
    private java.sql.Date date;
    private java.sql.Time start_time;
    private java.sql.Time end_time;
    private double total_price;
    private String booking_status;
    private Timestamp created_at;
    private Timestamp updated_at;

}
