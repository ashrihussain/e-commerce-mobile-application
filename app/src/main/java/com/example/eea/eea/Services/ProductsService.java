package com.example.eea.eea.Services;

import com.example.eea.eea.Model.OrderItem;
import com.example.eea.eea.Model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductsService {

    String BASE_URL = "http://10.3.4.149:8080/";

    @GET("products")
    Call<List<Product>> getProducts();


    @GET("products/{productId}")
    Call<Product> getProduct(@Path("productId") int productId);

    @POST("oitems")
    Call<OrderItem> addToCart(@Body OrderItem orderItem);

    @PUT("products")
    Call<Product> updateProducts(@Body OrderItemWrapper orderItemWrapper);
}
