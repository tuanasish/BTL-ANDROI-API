package com.example.btl.api;

import com.example.btl.models.Field;
import com.example.btl.models.FieldResponse;
import com.example.btl.models.FieldSingleResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiFieldInterface {

    // Lấy thông tin của Field theo ID
    @GET("field/{id}")
    Call<FieldSingleResponse> getById(@Path("id") int id);
    // Lấy danh sách tất cả các Field
    @GET("field/list")
    Call<FieldResponse> getAllFields();

    // them 1 field vao csdl
    @Multipart
    @POST("field/create")
    Call<ResponseBody> addField(
            @Part("name") RequestBody name,
            @Part("location") RequestBody location,
            @Part("type") RequestBody type,
            @Part("price") RequestBody price,
            @Part("capacity") RequestBody capacity,
            @Part MultipartBody.Part image
    );

    @Multipart
    @PUT("field/update/{id}")
    Call<ResponseBody> updateFieldToServer(
            @Path("id") int id,
            @Part("name") RequestBody name,
            @Part("location") RequestBody location,
            @Part("type") RequestBody type,
            @Part("price") RequestBody price,
            @Part("capacity") RequestBody capacity,
            @Part MultipartBody.Part image
    );




    // Xóa Field theo ID
    @DELETE("field/delete/{id}")
    Call<Void> deleteField(@Path("id") int id);

    // Lọc Field theo loại và khu vực
    @GET("fields/filter")
    Call<List<Field>> filterFields(
            @Query("type") String type,
            @Query("address") String address
    );
}
