package com.example.eea.eea.Services;

import com.example.eea.eea.Model.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewService {


    String BASE_URL = "http://10.3.4.149:8080/";

    @POST("reviews")
    Call<Review> addReview(@Body Review review);

    @GET("reviews")
    Call<List<Review>> getAllInquiries();

    @GET("/reviews/{pid}")
    Call<List<Review>> getReview(@Path("pid") int pid);
}
