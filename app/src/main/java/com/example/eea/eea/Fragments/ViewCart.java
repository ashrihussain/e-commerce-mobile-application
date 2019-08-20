package com.example.eea.eea.Fragments;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eea.eea.Adapters.CartAdapter;
import com.example.eea.eea.Model.OrderItem;
import com.example.eea.eea.R;
import com.example.eea.eea.Services.OrderItemService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ViewCart extends Fragment   {

    int finalQuantity=0;
    double finalPrice=0.0;
    TextView totalPrice;
    TextView totalQuantity;
    List<OrderItem> userCart;
    View view;
    private String username;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        inflater = LayoutInflater.from(getContext());
         view = inflater.inflate(R.layout.view_cart, container, false);
        final ListView listView=view.findViewById(R.id.cartList);
        getActivity().setTitle("Shopping Cart");


        Button checkout=view.findViewById(R.id.checkout);
        totalPrice=view.findViewById(R.id.totalPrice);
        totalQuantity=view.findViewById(R.id.totalQuantity);
        final FragmentManager fm = getActivity().getSupportFragmentManager();

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OrderItemService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderItemService orderItemService = retrofit.create(OrderItemService.class);
        Call<List<OrderItem>> cart = orderItemService.getCartItems(username);

        cart.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                userCart = response.body();

                CartAdapter cartAdapter = new CartAdapter(getContext(), userCart );
                listView.setAdapter(cartAdapter);
                for(OrderItem c: userCart)
                {
                    finalQuantity=finalQuantity+ c.getQuantity();
                    double price =  Double.parseDouble(c.getProduct().getPrice());
                    double total = c.getQuantity() * price;
                    finalPrice=finalPrice + total;
                }

                totalPrice.setText(String.valueOf(finalPrice));
                totalQuantity.setText(String.valueOf(finalQuantity));

            }
            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable throwable) {


            }
        });


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle=new Bundle();
                bundle.putInt("quantity", finalQuantity);
                bundle.putDouble("price", finalPrice);
                CheckoutDialogFragment checkout=new CheckoutDialogFragment();
                checkout.setArguments(bundle);
                checkout.show(getFragmentManager(), "CheckoutDialogFragment");
            }
        });

         return view;

    }



}
