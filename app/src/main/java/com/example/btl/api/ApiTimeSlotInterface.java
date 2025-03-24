package com.example.btl.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.btl.models.CourtResponse;
import com.example.btl.models.TimeSlot;

import java.lang.reflect.Array;
import java.util.List;

public interface ApiTimeSlotInterface {
    @GET("field/list/{fieldId}/booked")
    Call<List<TimeSlot>> getTimeSlots(@Path("fieldId") int fieldId, @Query("date") String date);
    @GET("field/{field_id}/courts")
    Call<CourtResponse> getCountCourt(@Path("field_id") int field_id);
}
