package com.example.eea.eea.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.eea.eea.Model.OrderItem;
import com.example.eea.eea.Model.Product;
import com.example.eea.eea.Model.User;
import com.example.eea.eea.R;
import com.example.eea.eea.Services.OrderItemService;
import com.example.eea.eea.Services.ProductsService;
import com.example.eea.eea.Services.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class CartFragment extends AppCompatDialogFragment {


    int position;




    NumberPicker quantity;

    Product selectedProduct;
    User getUserObj;
    List<OrderItem> userCart;


    OrderItem mutateOrderitem;


    Context mContext;
    private String username;
    private int newQuantity;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = null;
        builder = new AlertDialog.Builder(getActivity());


        Bundle bundle = getArguments();
        position = bundle.getInt("pid");


        View view = null;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_buy_pop_up, null);

        quantity = view.findViewById(R.id.quantityPicker);

        selectedProduct= retrieveProductStocks(position);





        builder.setView(view)
                .setTitle("Select Quantity")

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addProductToCart(quantity.getValue(), position);
                        Toast.makeText(getActivity(), "Added to Cart!", Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();

    }


    public void addProductToCart(final int quantity, int position) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        selectedProduct = retrieveProductStocks(position);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final UserService userService = retrofit.create(UserService.class);
        Call<User> call = userService.getUser(username);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                getUserObj = response.body();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(OrderItemService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                OrderItemService orderItemService = retrofit.create(OrderItemService.class);
                Call<List<OrderItem>> nestedCall = orderItemService.getCartItems(username);

                nestedCall.enqueue(new Callback<List<OrderItem>>() {
                    @Override
                    public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                        userCart = response.body();
                        if (userCart.size() >= 1)
                        {
                            for (OrderItem oi : userCart)
                            {
                                if (oi.getProduct().getPid()==(selectedProduct.getPid()))
                                {
                                    mutateOrderitem = oi;
                                    newQuantity = mutateOrderitem.getQuantity() + quantity;
                                    updateCart(newQuantity, mutateOrderitem);
                                    break;

                                }
                                else
                                {
                                        mutateOrderitem = new OrderItem();
                                        addToCart(quantity, getUserObj, mutateOrderitem);


                                }
                            }
                        }
                        else
                        {
                            mutateOrderitem = new OrderItem();

                                    addToCart(quantity, getUserObj, mutateOrderitem);

                            }


                    }
                    @Override
                    public void onFailure(Call<List<OrderItem>> call, Throwable throwable) {


                    }
                });

            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {

            }
        });



    }




    public Product retrieveProductStocks(int id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProductsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsService productsService = retrofit.create(ProductsService.class);
        Call<Product> call = productsService.getProduct(id);

        call.enqueue(new Callback<Product>() {

            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {

                selectedProduct = response.body();
                int maxVal= Integer.parseInt(selectedProduct.getQuantity());
                quantity.setMaxValue(maxVal);
                quantity.setMinValue(1);

            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return selectedProduct;
    }



    public void addToCart(int quantity, User user, OrderItem orderItem) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OrderItemService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderItemService orderItemService = retrofit.create(OrderItemService.class);

        Product product = new Product();
        product.setPid(position);

        User tempUser = new User();
        tempUser.setUsername(user.getUsername());
        tempUser.setUid(user.getUid());

        orderItem.setStatus("cart");
        orderItem.setQuantity(quantity);
        orderItem.setProduct(product);
        orderItem.setOi_user(tempUser);
        Call<OrderItem> call = orderItemService.addToCart(orderItem);

        call.enqueue(new Callback<OrderItem>() {
            @Override
            public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                Toast.makeText(mContext,"Added to Cart!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<OrderItem> call, Throwable throwable) {


            }
        });
    }

    public void updateCart(int quantity, OrderItem orderItem) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(OrderItemService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderItemService orderItemService = retrofit.create(OrderItemService.class);

        orderItem.setQuantity(quantity);




        Call<OrderItem> call = orderItemService.updateCartItem(username,quantity,selectedProduct.getPid());

        call.enqueue(new Callback<OrderItem>() {
            @Override
            public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                Toast.makeText(mContext,"Added to Cart!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<OrderItem> call, Throwable throwable) {


            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}



