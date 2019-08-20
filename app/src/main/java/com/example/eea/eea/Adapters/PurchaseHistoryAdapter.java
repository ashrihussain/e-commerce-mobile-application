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

import com.example.eea.eea.Model.Order;
import com.example.eea.eea.Model.OrderItem;
import com.example.eea.eea.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class PurchaseHistoryAdapter extends ArrayAdapter<OrderItem> {

    String id;

    List<String> dates = new ArrayList<>();

    public PurchaseHistoryAdapter(@NonNull Context context,  @NonNull List<OrderItem> objects, List<String> dates) {
        super(context, R.layout.purchase_histrory_custom , objects);
        this.dates = dates;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(getContext());;
        View view = inflater.inflate(R.layout.purchase_histrory_custom, parent, false);

        TextView purchaseProduct = view.findViewById(R.id.purchaseProduct);
        TextView purchasePrice = view.findViewById(R.id.purchasePrice);
        TextView purchaseQuantity = view.findViewById(R.id.purchaseQuantity);
        ImageView purchaseImage = view.findViewById(R.id.purchaseImage);
        TextView purchaseTime = view.findViewById(R.id.purchaseTime);





        final OrderItem pi=getItem(position);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("uid", "");




            purchaseProduct.setText(pi.getProduct().getProductName());
            purchasePrice.setText("$" + String.valueOf(pi.getProduct().getPrice()));
            purchaseQuantity.setText("Quantity : " + String.valueOf(pi.getQuantity()));
            try {
                purchaseTime.setText("Purchase Date : " +dates.get(position));

            }catch(Exception e) {
                e.printStackTrace();
            }






        return view;

    }
}
