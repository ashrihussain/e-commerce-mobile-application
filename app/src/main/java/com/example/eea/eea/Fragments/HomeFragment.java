package com.example.eea.eea.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.eea.eea.Adapters.DetailedScreenAdapter;
import com.example.eea.eea.Model.Product;
import com.example.eea.eea.R;
//import com.example.eea.eea.Services.ProdService;
import com.example.eea.eea.Services.ProductsService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class HomeFragment extends Fragment {

    int uid;
    DetailedScreenAdapter gridAdapter;

    GridView gridView;
    List<Product> products;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.activity_home, container, false);

        gridView = view.findViewById(R.id.gridView);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ProductsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductsService productsService = retrofit.create(ProductsService.class);
        Call<List<Product>> call = productsService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                products = response.body();

                try{
                    gridAdapter = new DetailedScreenAdapter(getActivity(), products);
                    gridView.setAdapter(gridAdapter);} catch(Exception e) {
                    System.out.println(e);
                }

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });





        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailedScreenFragment f = new DetailedScreenFragment();
                Product product= products.get(position);

                uid = product.getPid();
                Bundle bdl = new Bundle(4);
                bdl.putInt("uid", uid);

                f.setArguments(bdl);

                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                ft.replace(R.id.fragment_container, f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return view;

    }

}
