package com.example.btl.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeSlotResponse {
    @SerializedName("data")
    private List<ServerTimeSlot> data;

    public List<ServerTimeSlot> getData() {
        return data != null ? data : new ArrayList<>();
    }

    public static class ServerTimeSlot {
        @SerializedName("slot_id")
        private int slotId;

        @SerializedName("field_id")
        private int fieldId;

        @SerializedName("booking_date")
        private String bookingDate;

        @SerializedName("start_time")
        private String startTime;

        @SerializedName("end_time")
        private String endTime;

        @SerializedName("status")
        private String status;

        // Getters
        public int getSlotId() {
            return slotId;
        }

        public int getFieldId() {
            return fieldId;
        }

        public String getBookingDate() {
            return bookingDate != null ? bookingDate : "";
        }

        public String getStartTime() {
            return startTime != null ? startTime : "";
        }

        public String getEndTime() {
            return endTime != null ? endTime : "";
        }

        public String getStatus() {
            return status != null ? status : "available";
        }
    }
}

class TimeSlotConverter {
    private static final SimpleDateFormat SERVER_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault());
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public static List<TimeSlot> convert(List<TimeSlotResponse.ServerTimeSlot> serverSlots) {
        List<TimeSlot> result = new ArrayList<>();

        if (serverSlots == null) return result;

        for (TimeSlotResponse.ServerTimeSlot serverSlot : serverSlots) {
            try {
                Date bookingDate = parseBookingDate(serverSlot.getBookingDate());
                validateTimeFormat(serverSlot.getStartTime());
                validateTimeFormat(serverSlot.getEndTime());

                result.add(new TimeSlot(
                        serverSlot.getSlotId(),
                        serverSlot.getFieldId(),
                        bookingDate,
                        serverSlot.getStartTime(),
                        serverSlot.getEndTime(),
                        mapStatus(serverSlot.getStatus())
                ));
            } catch (ParseException | IllegalArgumentException e) {
                // Log error và bỏ qua slot không hợp lệ
                System.err.println("Error parsing timeslot: " + e.getMessage());
            }
        }
        return result;
    }

    private static Date parseBookingDate(String dateString) throws ParseException {
        if (dateString == null || dateString.isEmpty()) {
            throw new ParseException("Booking date is empty", 0);
        }
        return SERVER_DATE_FORMAT.parse(dateString);
    }

    private static void validateTimeFormat(String time) throws IllegalArgumentException {
        if (!time.matches("^([0-1]?\\d|2[0-3]):[0-5]\\d:[0-5]\\d$")) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }
    }

    private static int mapStatus(String serverStatus) {
        switch (serverStatus.toLowerCase(Locale.ROOT)) {
            case "booked":
                return TimeSlot.BOOKED;
            case "locked":
                return TimeSlot.LOCKED;
            case "available":
            default:
                return TimeSlot.AVAILABLE;
        }
    }
}