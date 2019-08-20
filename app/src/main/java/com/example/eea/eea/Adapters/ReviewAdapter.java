package com.example.eea.eea.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eea.eea.Model.Review;
import com.example.eea.eea.Model.User;
import com.example.eea.eea.R;
import com.squareup.picasso.Picasso;

import java.util.List;



public class ReviewAdapter extends ArrayAdapter<Review> {


    public ReviewAdapter(@NonNull Context context, List<Review> objects) {
        super(context, R.layout.custom_review, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Review i=new Review();
        i=getItem(position);
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View view=layoutInflater.inflate(R.layout.custom_review, parent, false);

        ImageView inquiryImage=view.findViewById(R.id.inquiryImage);
        TextView inquiryText=view.findViewById(R.id.inquiryText);
        TextView inquirySender=view.findViewById(R.id.inquirySender);
        TextView inquiryTime=view.findViewById(R.id.inquiryTime);


         User u=i.getUser();

        inquiryText.setText(i.getReview());
        inquirySender.setText(u.getUsername());
        inquiryTime.setText(i.getDate());



        Picasso.get()
                .load(u.getProfileimg())
                .placeholder(R.drawable.mensicons)
                .into(inquiryImage);

return view;






    }
}
