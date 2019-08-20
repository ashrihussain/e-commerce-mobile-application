package com.example.eea.eea.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eea.eea.Model.Product;

import com.example.eea.eea.R;
//import com.example.eea.eea.Services.ProdService;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DetailedScreenAdapter extends ArrayAdapter<Product> implements Filterable {

    List<Product> products;

    public DetailedScreenAdapter(Context aContext, List<Product> objects) {
        super(aContext, R.layout.custom_layout, objects);
        objects = this.products;

    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Product p=getItem(position);
        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());


        view = layoutInflater.inflate(R.layout.custom_layout, parent, false);

        TextView prodText =  view.findViewById(R.id.productText);
        TextView priceText = view.findViewById(R.id.priceText);
        TextView descriptionText = view.findViewById(R.id.descriptionText);
        ImageView gridFav = view.findViewById(R.id.gridFav);



        ImageView imageViewPhoto =  view.findViewById(R.id.productIcon);


    try {
        String price = String.valueOf(p.getPrice());
        prodText.setText(p.getProductName());
        priceText.setText("$" + price);
        descriptionText.setText("In stock :" + p.getQuantity() + " items");
    } catch(Exception e)
    {
        System.out.println(e);
    }





        Picasso.get()
                .load(p.getProdImage())
                .placeholder(R.drawable.mensicons)
                .resize(300,500)
                .into(imageViewPhoto);




        return view;

    }

}
