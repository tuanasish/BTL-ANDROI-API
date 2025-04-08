package com.example.btl.api;

import android.util.Log;

import com.example.btl.models.ChangePasswordRequest;
import com.example.btl.models.LoginResponse;
import com.example.btl.models.RegisterResponse;
import com.example.btl.models.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUserService {
    private ApiUserInterface api;

    public ApiUserService(ApiUserInterface api) {
        this.api = api;
    }

    //dang nhap
    public void loginUser(String email, String password, final ApiCallback<User> callback) {
        api.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    User user = loginResponse.getUser();

                    if (user != null) {
                        Log.d("API_RESPONSE", "Login success: " + loginResponse.getMessage());
                        Log.d("API_RESPONSE", "User data: " + user.toString());
                        callback.onSuccess(user);
                    } else {
                        callback.onError(new Exception("User data is null"));
                    }
                } else {
                    String errorMsg = "Login failed: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("API_ERROR", "Error reading error body", e);
                    }
                    callback.onError(new Exception(errorMsg));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("API_FAILURE", "Network error: " + t.getMessage());
                callback.onError(t);
            }
        });
    }

    // dang ky
    public void registerUser(User user, final ApiCallback<User> callback) {
        api.register(user).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    User registeredUser = registerResponse.getUser();

                    if (registeredUser != null) {
                        callback.onSuccess(registeredUser);
                    } else {
                        callback.onError(new Exception("User data is null in response"));
                    }
                } else {
                    String errorMsg = "Registration failed: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("API_ERROR", "Error reading error body", e);
                    }
                    callback.onError(new Exception(errorMsg));
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("API_FAILURE", "Registration error: " + t.getMessage());
                callback.onError(t);
            }
        });
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
    // Thêm phương thức changePassword vào ApiUserService




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

    public void changePassword(int userId, String oldPassword, String newPassword, ApiCallback<Void> callback) {
        ChangePasswordRequest request = new ChangePasswordRequest(oldPassword, newPassword);
        api.changePassword(userId, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError(new Exception("Lỗi: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
