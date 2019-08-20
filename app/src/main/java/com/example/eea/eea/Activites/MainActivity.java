package com.example.eea.eea.Activites;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.View.OnClickListener;
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

public class MainActivity extends AppCompatActivity {
EditText uname;
EditText userPass;
Button button;
Button signup;
String uid;
User selectedUser;
boolean x=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
           setTitle("Login");
    button=(Button)findViewById(R.id.button);
        uname=(EditText)findViewById(R.id.editText);
        userPass=(EditText)findViewById(R.id.editText3);
    signup=(Button)findViewById(R.id.button3);

    button.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
    login(uname.getText().toString(), userPass.getText().toString());
        }
    });

    signup.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
            signup();
        }
    });

    }

    public void login(String username , final String password) {



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService userService = retrofit.create(UserService.class);

        Call<User> call = userService.getUser(username);



        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                selectedUser = response.body();

                if(selectedUser.getPassword().equals(password))
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("loginDetails", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", selectedUser.getUsername());
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                    startActivity(intent);
                }


            }


            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Invalid combination of username and password", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signup() {

        Intent intent=new Intent(MainActivity.this, Registration.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

    }
}
