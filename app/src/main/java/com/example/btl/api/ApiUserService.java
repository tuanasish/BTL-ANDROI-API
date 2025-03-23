package com.example.btl.api;

import com.example.btl.models.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUserService {
    private ApiUserInterface api;

    public ApiUserService(ApiUserInterface api) {
        this.api = api;
    }

    // Lấy thông tin User theo ID
    public void getUserById(String email, int id, ApiCallback<User> callback) {
        api.getById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Lấy danh sách tất cả Users
    public void getAllUsers(ApiCallback<List<User>> callback) {
        api.getAll().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Tạo mới một User (Đăng ký)
    public void createUser(User user, ApiCallback<User> callback) {
        api.create(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Cập nhật User theo ID
    public void updateUser(int id, User user, ApiCallback<User> callback) {
        api.update(id, user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Response error: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
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
