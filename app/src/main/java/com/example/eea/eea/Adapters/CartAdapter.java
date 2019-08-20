package com.example.eea.eea.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eea.eea.Fragments.ViewCart;
import com.example.eea.eea.Model.Order;
import com.example.eea.eea.Model.OrderItem;
import com.example.eea.eea.R;
import com.example.eea.eea.Services.OrderItemService;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class CartAdapter extends ArrayAdapter<OrderItem> {

    String id;


    public CartAdapter(@NonNull Context context, @NonNull List<OrderItem> objects) {
        super(context, R.layout.cart_item, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View view = layoutInflater.inflate(R.layout.cart_item, parent, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("uid", "");

        final Date date = Calendar.getInstance().getTime();

        final OrderItem retrieveOitem = getItem(position);

        TextView productName = view.findViewById(R.id.cartText);
        TextView productPrice = view.findViewById(R.id.cartPriceText);
        TextView productQuantity = view.findViewById(R.id.cartQuantity);
        ImageView productPhoto = view.findViewById(R.id.cartImage);
        Button removeButton = view.findViewById(R.id.removeButton);

        productName.setText("Item: " + retrieveOitem.getProduct().getProductName());
        productPrice.setText("$"  + String.valueOf(retrieveOitem.getQuantity()* Double.parseDouble(retrieveOitem.getProduct().getPrice())));
        productQuantity.setText("Quantity: " + String.valueOf(retrieveOitem.getQuantity()));

        Picasso.get()
                .load(retrieveOitem.getProduct().getProdImage())
                .placeholder(R.drawable.mensicons)
                .resize(200, 300)
                .into(productPhoto);



        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveOitem.setStatus("");
                remove(retrieveOitem);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(OrderItemService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                OrderItemService orderItemService = retrofit.create(OrderItemService.class);

                retrieveOitem.setStatus("");

                Call<OrderItem> call = orderItemService.deleteFromCart(retrieveOitem.getId());

                call.enqueue(new Callback<OrderItem>() {
                    @Override
                    public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                        Toast.makeText(getContext(), retrieveOitem.getProduct().getProductName() + " removed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<OrderItem> call, Throwable throwable) {
//

                    }

                });
                Toast.makeText(getContext(), retrieveOitem.getProduct().getProductName() + " removed!", Toast.LENGTH_SHORT).show();

            }
        });






        return view;

    }




}