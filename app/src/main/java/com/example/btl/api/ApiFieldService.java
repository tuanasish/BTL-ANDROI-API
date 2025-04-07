package com.example.btl.api;

import android.util.Log;

import com.example.btl.models.Field;
import com.example.btl.models.FieldResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiFieldService {
    private ApiFieldInterface api;

    public ApiFieldService(ApiFieldInterface api) {
        this.api = api;
    }

    // Lấy thông tin của Field theo ID
    public void getFieldById(int id, final ApiCallback<Field> callback) {
        api.getById(id).enqueue(new Callback<Field>() {
            @Override
            public void onResponse(Call<Field> call, Response<Field> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());

                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<Field> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Lấy danh sách tất cả các Field
    public void getAllFields(final ApiCallback<List<Field>> callback) {
        api.getAllFields().enqueue(new Callback<FieldResponse>() {
            @Override
            public void onResponse(Call<FieldResponse> call, Response<FieldResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Field> fields = response.body().getData(); // Lấy danh sách Field từ FieldResponse
                    Log.d("API_RESPONSE", "Data received: " + fields.toString());
                    callback.onSuccess(fields);
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<FieldResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Tạo mới một Field
    public void createField(Field field, final ApiCallback<Field> callback) {
        api.createField(field).enqueue(new Callback<Field>() {
            @Override
            public void onResponse(Call<Field> call, Response<Field> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<Field> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Xóa Field theo ID
    public void deleteField(int id, final ApiCallback<Void> callback) {
        api.deleteField(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Lọc Field theo loại và địa chỉ
    public void filterFields(String type, String address, final ApiCallback<List<Field>> callback) {
        api.filterFields(type, address).enqueue(new Callback<List<Field>>() {
            @Override
            public void onResponse(Call<List<Field>> call, Response<List<Field>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<List<Field>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(Throwable t);
    }
}
