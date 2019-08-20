package com.example.eea.eea.Services;

import com.example.eea.eea.Model.Order;
import com.example.eea.eea.Model.OrderItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService {

    String BASE_URL = "http:/10.3.4.149:8080/";


    @POST("oitems")
    Call<OrderItem> addToCart(@Body OrderItem orderItem);

    @GET("orders/{username}")
    Call<List<Order>> getPurcahsedItems(@Path("username") String username);


}
