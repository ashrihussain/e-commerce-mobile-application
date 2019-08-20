package com.example.eea.eea.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eea.eea.Model.User;
import com.example.eea.eea.R;
import com.example.eea.eea.Services.UserService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ManageAccountFragment extends Fragment {
    boolean showingFirst = true;
    String password;
    String username;

    Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater = LayoutInflater.from(getContext());
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("loginDetails", Context.MODE_PRIVATE);

        final String USERNAME=sharedPreferences.getString("username","");

        getActivity().setTitle("Account Settings");

        final View view = inflater.inflate(R.layout.manage_account, container, false);
        final EditText usernameText=view.findViewById(R.id.usernameText);
        final EditText passwordText=view.findViewById(R.id.passwordText);

        final ImageView profilePic=view.findViewById(R.id.profilePic);
        final Button editProfile=view.findViewById(R.id.editProfile);
        final Button saveChanges=view.findViewById(R.id.saveChanges);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final UserService userService = retrofit.create(UserService.class);

        Call<User> call = userService.getUser(USERNAME);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User u = response.body();

                usernameText.setHint(u.getUsername());
                passwordText.setHint("*******");
                saveChanges.setEnabled(false);


                Picasso.get()
                        .load(u.getProfileimg())
                        .placeholder(R.drawable.mensicons)
                        .into(profilePic);





            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {

            }
        });



        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showingFirst == true) {
                    usernameText.setEnabled(true);
                    passwordText.setEnabled(true);

                    saveChanges.setEnabled(true);
                    editProfile.setEnabled(false);
                }
            }
            });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editProfile.setText("EDIT PROFILE");
                username = usernameText.getText().toString();
                password = passwordText.getText().toString();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UserService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final UserService userService = retrofit.create(UserService.class);

                Call<List<User>> userCall = userService.getAllUsers();


                userCall.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                        List<User> users = response.body();
                        int validater= 0;
                        final User newUserDetails = new User();

                        for(User u: users) {
                            if (u.getUsername().equalsIgnoreCase(username)) {
                                validater++;
                            }
                        }

                            if(validater == 0) {

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(UserService.BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                final UserService userService = retrofit.create(UserService.class);

                                Call<User> userCall = userService.getUser(USERNAME);

                                userCall.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        User existingUser = response.body();

                                        existingUser.setUsername(username);
                                        existingUser.setPassword(password);


                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(UserService.BASE_URL)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();

                                        final UserService userService = retrofit.create(UserService.class);

                                        Call<User> userCall = userService.updateUser(USERNAME, existingUser);

                                        userCall.enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {
                                                Toast.makeText(getActivity(), "Account details updated!", Toast.LENGTH_LONG).show();


                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable throwable) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable throwable) {

                                    }
                                });





                            saveChanges.setEnabled(false);
                            usernameText.setEnabled(false);
                            passwordText.setEnabled(false);

                            editProfile.setEnabled(true);
                            Toast.makeText(getActivity(), "Change Successfull!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getActivity(),"Username already in use!",Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable throwable) {

                    }
                });



            }

        });




return view;


    }



}
