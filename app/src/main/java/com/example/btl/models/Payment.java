package com.example.btl.models;

import java.sql.Timestamp;

public class Payment {
    private int payment_id;
    private int booking_id;
    private int user_id;
    private double amount;
    private String payment_method;
    private String payment_status;
    private Timestamp payment_date;
}
