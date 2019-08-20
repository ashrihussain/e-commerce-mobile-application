package com.example.eea.eea.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eea.eea.Adapters.CheckoutCartAdapter;
import com.example.eea.eea.Model.OrderItem;
import com.example.eea.eea.Model.Product;
import com.example.eea.eea.R;
import com.example.eea.eea.Services.OrderItemService;
import com.example.eea.eea.Services.OrderItemWrapper;
import com.example.eea.eea.Services.ProductsService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class CheckoutDialogFragment extends AppCompatDialogFragment {

    Double price;
    int quantity;


    List<OrderItem> userCart;
      String username;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

            price= bundle.getDouble("price");
            quantity= bundle.getInt("quantity");

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.checkout_card_details, null);

        TextView quantityText= view.findViewById(R.id.checkoutQuantityText);
        final GridView checkoutItems= view.findViewById(R.id.checkoutGrid);
        TextView priceText= view.findViewById(R.id.checkoutTotalText);

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

                CheckoutCartAdapter cca=new CheckoutCartAdapter(getContext(), userCart);
                checkoutItems.setAdapter(cca);



            }
            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable throwable) {
                Toast.makeText(getContext(),"Error! Please try again!", Toast.LENGTH_SHORT).show();

            }
        });


            quantityText.setText(String.valueOf(quantity));
            priceText.setText("$ "+ String.valueOf(price));

        builder.setView( view)
                .setTitle("Confirm Purchase")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date d = Calendar.getInstance().getTime();
                        Toast.makeText(getActivity(), "Order placed successfully!! Please refresh the page", Toast.LENGTH_SHORT).show();
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(OrderItemService.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        OrderItemService orderItemService = retrofit.create(OrderItemService.class);
                        Call<Void> checkout = orderItemService.checkout(username);

                        checkout.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(ProductsService.BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                ProductsService productsService = retrofit.create(ProductsService.class);
                                OrderItemWrapper orderItemWrapper = new OrderItemWrapper();
                                orderItemWrapper.setOrderItemList(userCart);
                                Call<Product> updateProducts = productsService.updateProducts(orderItemWrapper);

                                updateProducts.enqueue(new Callback<Product>() {
                                    @Override
                                    public void onResponse(Call<Product> call, Response<Product> response) {
                                        Toast.makeText(getActivity(), quantity+" Products Purchased!", Toast.LENGTH_LONG).show();
                                        Fragment fragment= new ViewCart();
                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.fragment_container, fragment);
                                        ft.commit();
                                        Toast.makeText(getActivity(), "Order placed successfully!!", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Product> call, Throwable throwable) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable throwable) {
                            }
                        });


                    }

                });




        return builder.create();

    }


}
