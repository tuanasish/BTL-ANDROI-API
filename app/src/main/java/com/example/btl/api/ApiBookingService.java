package com.example.btl.api;

import com.example.btl.models.Booking;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiBookingService {
    private ApiBookingInterface api;

    public ApiBookingService(ApiBookingInterface api) {
        this.api = api;
    }

    // Tạo mới một Booking
    public void createBooking(Booking booking, ApiCallback<Booking> callback) {
        api.createBooking(booking).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Lấy thông tin Booking theo ID
    public void getBookingById(int id, ApiCallback<Booking> callback) {
        api.getById(id).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Cập nhật thông tin Booking theo ID
    public void updateBooking(int id, Booking booking, ApiCallback<Booking> callback) {
        api.updateBooking(id, booking).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Xóa Booking theo ID
    public void deleteBooking(int id, ApiCallback<Void> callback) {
        api.deleteBooking(id).enqueue(new Callback<Void>() {
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

    // Tìm kiếm Booking theo ngày và trạng thái
    public void searchBookings(String date, String status, ApiCallback<List<Booking>> callback) {
        api.searchBookings(date, status).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Nếu API có endpoint lấy danh sách Booking theo userId, bạn có thể thêm như sau:
    public void getBookingsByUserId(int userId, ApiCallback<List<Booking>> callback) {

        api.getBookingsByUser(userId).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
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
