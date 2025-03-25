package com.example.btl.api;

import com.example.btl.models.Booking;

import java.util.List;

import retrofit2.Call;

public interface ApiBookingInterface {
    Call<Booking> createBooking(Booking booking);

    Call<Booking> getById(int id);

    Call<Booking> updateBooking(int id, Booking booking);

    Call<Void> deleteBooking(int id);

    Call<List<Booking>> searchBookings(String date, String status);

    Call<List<Booking>> getBookingsByUser(int userId);
    // Endpoint để lấy danh sách sân
    //
}
