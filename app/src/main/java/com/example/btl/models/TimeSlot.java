package com.example.btl.models;

import java.sql.Date;
import java.sql.Time;

public class TimeSlot {

    private int slot_id;            // slot_id (PK, auto increment)
    private int field_id;           // field_id
    private Date booking_date;      // booking_date
    private Time start_time;        // start_time
    private Time end_time;          // end_time
    private String status;          // status (enum: 'available', 'booked')

    public TimeSlot(String startTime, String endTime, String status) {
    }

    // Getter and Setter methods
    public int getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(int slot_id) {
        this.slot_id = slot_id;
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public Date getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(Date booking_date) {
        this.booking_date = booking_date;
    }

    public Time getStart_time() {
        return start_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public Time getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "slot_id=" + slot_id +
                ", field_id=" + field_id +
                ", booking_date=" + booking_date +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", status='" + status + '\'' +
                '}';
    }
}
