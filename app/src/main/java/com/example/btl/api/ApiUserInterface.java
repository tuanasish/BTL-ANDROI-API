package com.example.btl.api;

import com.example.btl.models.ChangePasswordRequest;
import com.example.btl.models.LoginResponse;
import com.example.btl.models.RegisterResponse;
import com.example.btl.models.User;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiUserInterface {

    // dang nhap
    @GET("api/auth/login")
    Call<LoginResponse> login(@Query("email") String email,
                              @Query("password") String password);
    // dang ki
    @POST("api/auth/register")
    Call<RegisterResponse> register(@Body User user);

    @GET("user/{id}")
    Call<User> getById(@Path("id") int id);

    @GET("user")
    Call<List<User>> getAll();

    // Đăng ký User
    @POST("user")
    Call<User> create(@Body User user);

    // Sửa User
    @PUT("api/auth/update/{id}")
    Call<User> update(@Path("id") int id, @Body User user);

    @PUT("api/auth/{id}/pwd")
    Call<Void> changePassword(@Path("id") int userId, @Body ChangePasswordRequest request);

}