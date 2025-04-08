package com.example.btl.api;

import com.example.btl.models.BookingRequest;
import com.example.btl.models.CourtResponse;
import com.example.btl.models.TimeSlot;
import com.example.btl.models.TimeSlotResponse;
import com.example.btl.models.TimeslotResponseBooking;
import com.google.gson.JsonObject;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiTimeSlotService {
    private ApiTimeSlotInterface api;

    public ApiTimeSlotService(ApiTimeSlotInterface api) {
        this.api = api;
    }

    public void createBooking(BookingRequest request, ApiCallback<JsonObject> callback) {
        api.createBooking(request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

//    //lay count court va id
//    public void getCountCourt(int fieldId, ApiCallback<CourtResponse> callback) {
//        api.getCountCourt(fieldId).enqueue(new Callback<CourtResponse>() {
//            @Override
//            public void onResponse(Call<CourtResponse> call, Response<CourtResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    callback.onSuccess(response.body());
//                } else {
//                    callback.onError(new Exception("Response error: " + response.code()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CourtResponse> call, Throwable t) {
//                callback.onError(t);
//            }
//        });
//    }


    // Lấy danh sách TimeSlot đã được đặt theo FieldId và Date
    public void getTimeSlots(int fieldId, String date, ApiCallback<TimeslotResponseBooking> callback) {
        api.getTimeSlots(fieldId, date).enqueue(new Callback<TimeslotResponseBooking>() {
            @Override
            public void onResponse(Call<TimeslotResponseBooking> call, Response<TimeslotResponseBooking> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<TimeslotResponseBooking> call, Throwable t) {
                callback.onError(t);
            }
        });

    }


    // Interface callback để xử lý kết quả bất đồng bộ
    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(Throwable t);
    }
}
