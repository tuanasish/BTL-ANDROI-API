package com.example.btl.api;

import com.example.btl.models.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiUserInterface {

    @GET("user/{id}")
    Call<User> getById(@Path("id") int id);

    @GET("user")
    Call<List<User>> getAll();

    // Đăng ký User
    @POST("user")
    Call<User> create(@Body User user);

    // Sửa User
    @PUT("user/{id}")
    Call<User> update(@Path("id") int id, @Body User user);
}
