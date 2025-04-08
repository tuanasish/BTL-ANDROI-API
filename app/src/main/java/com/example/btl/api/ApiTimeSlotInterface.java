package com.example.btl.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.btl.models.BookingRequest;
import com.example.btl.models.CourtResponse;
import com.example.btl.models.TimeSlot;
import com.example.btl.models.TimeslotResponseBooking;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.List;

public interface ApiTimeSlotInterface {



    @GET("field/list/{fieldId}/booked")
    Call<TimeslotResponseBooking> getTimeSlots(@Path("fieldId") int fieldId, @Query("date") String date);
    @GET("field/{field_id}/courts")
    Call<CourtResponse> getCountCourt(@Path("field_id") int field_id);
    @POST("bookings/create")
    Call<JsonObject> createBooking(@Body BookingRequest request);
}
