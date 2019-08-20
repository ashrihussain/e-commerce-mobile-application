package com.example.eea.eea.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

//import com.example.eea.eea.Adapters.DetailedScreenAdapter;
import com.example.eea.eea.Adapters.ReviewAdapter;
import com.example.eea.eea.Model.Review;
import com.example.eea.eea.Model.Product;
import com.example.eea.eea.Model.User;
import com.example.eea.eea.R;
import com.example.eea.eea.Services.ProductsService;
import com.example.eea.eea.Services.ReviewService;
import com.example.eea.eea.Services.UserService;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetailedScreenFragment extends Fragment {

    int position;




    TextView priceText;
    TextView productText;
    ImageView productImage;


    Button buyButton;

    TextView productQuantity;
    TextView descriptionText;
    TextView sendText;

    ListView reviewItemList;

    Button sendButton;



    Product selectedProduct;


    CartFragment cartFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final Bundle bdl = getArguments();
        position = bdl.getInt("uid");


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        final String USERNAME = sharedPreferences.getString("username", "");

        inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.gridview_item, container, false);
        getActivity().setTitle("Product Details");


        priceText = view.findViewById(R.id.productPrice);
        productText = view.findViewById(R.id.productTexts);
        productImage = view.findViewById(R.id.productImage);
        //Button cartButton = view.findViewById(R.id.cartButton);
        buyButton = view.findViewById(R.id.buyButton);

        productQuantity = view.findViewById(R.id.quantity);
        descriptionText = view.findViewById(R.id.DescriptionText);
        sendText = view.findViewById(R.id.edittext_chatbox);
        sendButton = view.findViewById(R.id.button_chatbox_send);
        reviewItemList = view.findViewById(R.id.inquiryList);



        getProduct();


        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartFragment = new CartFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("pid", position);
                cartFragment.setArguments(bundle);
                cartFragment.show(getFragmentManager(), "buyFragment");
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ReviewService reviewService = retrofit.create(ReviewService.class);

        Call<List<Review>> call2 = reviewService.getReview(position);

        call2.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {

                List<Review> reviews = response.body();
                ReviewAdapter populateReviews = new ReviewAdapter(getContext(), reviews);
                reviewItemList.setAdapter(populateReviews);


            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable throwable) {

            }
        });





        reviewItemList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
//

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Your review has been placed, Thank you! for the feedback!", Toast.LENGTH_LONG).show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UserService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final UserService userService = retrofit.create(UserService.class);

                Call<User> call = userService.getUser(USERNAME);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();

                            Product product = new Product();
                            product.setPid(position);

                            Date currentDate = Calendar.getInstance().getTime();
                            Review newReview = new Review();
                            newReview.setReview(sendText.getText().toString());
                            newReview.setUser(user);
                            newReview.setDate(currentDate.toString());
                            newReview.setProd_id(product);


                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(UserService.BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final ReviewService reviewService = retrofit.create(ReviewService.class);

                            Call<Review> secondcall = reviewService.addReview(newReview);
                            secondcall.enqueue(new Callback<Review>() {
                                @Override
                                public void onResponse(Call<Review> call, Response<Review> response) {


                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(UserService.BASE_URL)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    final ReviewService reviewService = retrofit.create(ReviewService.class);

                                    Call<List<Review>> call2 = reviewService.getAllInquiries();
                                    call2.enqueue(new Callback<List<Review>>() {
                                        @Override
                                        public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                                            List<Review> reviews = response.body();
                                            ReviewAdapter setReviews = new ReviewAdapter(getContext(), reviews);
                                            reviewItemList.setAdapter(setReviews);

                                        }

                                        @Override
                                        public void onFailure(Call<List<Review>> call, Throwable throwable) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<Review> call, Throwable throwable) {

                                }
                            });


                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable throwable) {

                        }
                    });

            }
        });




        return view;


    }

    public Product getProduct() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProductsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsService productsService = retrofit.create(ProductsService.class);
        Call<Product> call = productsService.getProduct(position);

        call.enqueue(new Callback<Product>() {

            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                selectedProduct = response.body();

                priceText.setText("Price: $" + selectedProduct.getPrice());
                productText.setText(selectedProduct.getProductName());
//                descriptionText.setText("Description");
                productQuantity.setText("In Stock :" + selectedProduct.getQuantity());

                Picasso.get()
                        .load(selectedProduct.getProdImage())
                        .placeholder(R.drawable.mensicons)
                        .into(productImage);

            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        return selectedProduct;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Product Details");

    }

}
