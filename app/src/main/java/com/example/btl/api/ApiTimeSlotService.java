package com.example.btl.api;

import com.example.btl.models.CourtResponse;
import com.example.btl.models.TimeSlot;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiTimeSlotService {
    private ApiTimeSlotInterface api;

    public ApiTimeSlotService(ApiTimeSlotInterface api) {
        this.api = api;
    }

    //lay count court va id
    public void getCountCourt(int fieldId, ApiCallback<CourtResponse> callback) {
        api.getCountCourt(fieldId).enqueue(new Callback<CourtResponse>() {
            @Override
            public void onResponse(Call<CourtResponse> call, Response<CourtResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<CourtResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


    // Lấy danh sách TimeSlot đã được đặt theo FieldId và Date
    public void getTimeSlots(int fieldId, String date, ApiCallback<List<TimeSlot>> callback) {
        api.getTimeSlots(fieldId, date).enqueue(new Callback<List<TimeSlot>>() {
            @Override
            public void onResponse(Call<List<TimeSlot>> call, Response<List<TimeSlot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<TimeSlot>> call, Throwable t) {
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
