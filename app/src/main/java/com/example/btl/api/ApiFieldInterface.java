package com.example.btl.api;

import com.example.btl.models.Field;
import com.example.btl.models.FieldResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiFieldInterface {

    // Lấy thông tin của Field theo ID
    @GET("field/{id}")
    Call<Field> getById(@Path("id") int id);

    // Lấy danh sách tất cả các Field
    @GET("field/list")
    Call<FieldResponse> getAllFields();

    // Tạo mới một Field
    @POST("field")
    Call<Field> createField(@Body Field field);

    // Cập nhật thông tin Field theo ID
    @PUT("field/{id}")
    Call<Field> updateField(@Path("id") int id, @Body Field field);

    // Xóa Field theo ID
    @DELETE("field/{id}")
    Call<Void> deleteField(@Path("id") int id);

    // Lọc Field theo loại và khu vực
    @GET("fields/filter")
    Call<List<Field>> filterFields(
            @Query("type") String type,
            @Query("address") String address
    );
}
