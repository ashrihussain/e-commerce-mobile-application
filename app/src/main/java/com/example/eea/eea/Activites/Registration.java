package com.example.eea.eea.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eea.eea.Model.User;
import com.example.eea.eea.R;
import com.example.eea.eea.Services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registration extends AppCompatActivity {
    EditText usernameSU;

    EditText passwordSU, emailAddress, fname, raddress, profimg;


    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        usernameSU=(EditText)findViewById(R.id.editText2);
        passwordSU=(EditText)findViewById(R.id.editText4);
        emailAddress=(EditText)findViewById(R.id.editText5);
        fname=(EditText)findViewById(R.id.editText6);
        raddress=(EditText)findViewById(R.id.editText7);
        profimg=(EditText)findViewById(R.id.editText8);

        signup=(Button)findViewById(R.id.button2);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User u = new User(usernameSU.getText().toString(), passwordSU.getText().toString(),emailAddress.getText().toString(),fname.getText().toString(),raddress.getText().toString(),profimg.getText().toString());

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(UserService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final UserService userService = retrofit.create(UserService.class);

                Call<User> call2 = userService.addUser(u);
                call2.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable throwable) {
                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

    }
    public void sendMessage(String username ,String password, String emailAddress) {




    }
    }





