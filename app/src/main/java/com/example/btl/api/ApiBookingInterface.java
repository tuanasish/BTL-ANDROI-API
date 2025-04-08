package com.example.btl.api;

import com.example.btl.models.Booking;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiBookingInterface {
    @GET("bookings/user/{user_id}/with-field")
    Call<JsonObject> getBookingsWithField(@Path("user_id") int userId);

    Call<Booking> createBooking(Booking booking);

    Call<Booking> getById(int id);

    Call<Booking> updateBooking(int id, Booking booking);

    Call<Void> deleteBooking(int id);

    Call<List<Booking>> searchBookings(String date, String status);

    Call<List<Booking>> getBookingsByUser(int userId);
    // Endpoint để lấy danh sách sân
    //
}
