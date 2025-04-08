package com.example.btl.api;

import com.example.btl.models.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiReviewInterface {
        @GET("reviews/field/{field_id}")
        Call<List<Review>> getReviewsByFieldId(@Path("field_id") int fieldId);

        @POST("reviews/create")
        Call<Review> createReview(@Body Review review);


}
