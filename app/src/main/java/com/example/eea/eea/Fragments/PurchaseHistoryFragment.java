package com.example.eea.eea.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eea.eea.Adapters.PurchaseHistoryAdapter;
import com.example.eea.eea.Model.Order;
import com.example.eea.eea.Model.OrderItem;
import com.example.eea.eea.R;
import com.example.eea.eea.Services.OrderItemService;
import com.example.eea.eea.Services.OrderService;
import com.example.eea.eea.Services.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class PurchaseHistoryFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        inflater = LayoutInflater.from(getContext());
        inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.purchase_history, container, false);
        getActivity().setTitle("Purchase History");


        final ListView listView = view.findViewById(R.id.purchaseList);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString("username", "");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final OrderItemService orderItem = retrofit.create(OrderItemService.class);

        Call<List<OrderItem>> call = orderItem.getPurchasedItems(username);

        call.enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                final List<OrderItem> orderItems = response.body();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UserService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                OrderService orderService = retrofit.create(OrderService.class);
                Call<List<Order>> call1 = orderService.getPurcahsedItems(username);

                call1.enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        List<Order> orders = response.body();
                        List<String> dates = new ArrayList<String>();

                        for (Order o: orders) {
                            dates.add(o.getOrder_date());
                        }

                        PurchaseHistoryAdapter piAdapter = new PurchaseHistoryAdapter(getContext(), orderItems, dates);
                        listView.setAdapter(piAdapter);


                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable throwable) {
                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();

            }
        });





        return view;

    }
}