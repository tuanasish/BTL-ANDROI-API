package com.example.btl.api;

import com.example.btl.models.Booking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiBookingInterface {
    // Endpoint để lấy danh sách sân
    //v1/booking/create
    @POST("/booking/create")
    Call<Booking> createBooking(@Body Booking booking);

    //get detail
    @GET("/booking/getbyid/{id}")
    Call<Booking> getById(@Path("id") int id);
    //detail by userId
    @GET("/booking/getbyuser/{userId}")
    Call<List<Booking>> getBookingsByUser(@Path("userId") int userId);

    //update
    @PUT("/booking/update/{id}")
    Call<Booking> updateBooking(@Path("id") int id, @Body Booking booking);
    //delete
    @DELETE("/booking/delete/{id}")
    Call<Void> deleteBooking(@Path("id") int id);
    //loc theo ngày
    @GET("/booking/search")
    Call<List<Booking>> searchBookings(@Query("date") String date, @Query("status") String status);

}
