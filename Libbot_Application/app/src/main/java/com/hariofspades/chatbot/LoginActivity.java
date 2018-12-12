package com.hariofspades.chatbot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.hariofspades.chatbot.Pojo.UserBean;

import java.util.ArrayList;
import java.util.List;

import retrofit.APIClient;
import retrofit.RetrofitClientInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class LoginActivity extends AppCompatActivity  {
    private TextView signup, input_memType, forgetpassword;
    private EditText inputenrollment, password;
    private RadioButton rbadmin, rbstudent;
    private Button btn_login;
    private ImageView image;
    public boolean result;
    private RetrofitClientInterface retrofitClientInterface;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

      //  SharedPreferences prefs = getSharedPreferences("user_login", MODE_PRIVATE);
       // if(prefs.getBoolean("isLogin", false))
       // {
        //    startActivity(new Intent(LoginActivity.this, MainActivity.class));
        //    finish();
       // }
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_login);


        signup= (TextView) findViewById(R.id.signup);
        forgetpassword= (TextView) findViewById(R.id.forgetpassword);
        rbadmin=(RadioButton)findViewById(R.id.rbadmin);
        rbstudent=(RadioButton)findViewById(R.id.rbstudent);
        input_memType=(TextView)findViewById(R.id.input_memType);
        inputenrollment=(EditText )findViewById(R.id.inputenrollment);
        password=(EditText )findViewById(R.id.password);
        btn_login =(Button)findViewById(R.id.btn_login);
        image = (ImageView) findViewById(R.id.image);
        progress =  (ProgressBar) findViewById(R.id.progress);


        retrofitClientInterface =  APIClient.getClient().create(RetrofitClientInterface.class);



        rbadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputenrollment.setHint("Username");

            }
        });
        rbstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputenrollment.setHint("Enrollment");

            }
        });




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);

            }
        });
       // forgetpassword.setOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View view) {
             //   Intent i= new Intent(LoginActivity.this, ForgotPassword.class);
              //  startActivity(i);

           // }
      //  });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1= inputenrollment.getText().toString();
                String s2= password.getText().toString();
                progress.setVisibility(View.VISIBLE);
                if(s1.equals("")||s2.equals(""))
                    Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else
                {
                    Call<UserBean> retrofitUserCall =   retrofitClientInterface.getUserByUsername(s1);

                    retrofitUserCall.enqueue(new Callback<UserBean>() {

                                                 @Override
                                                 public void onResponse(Response<UserBean> response) {
                                                     progress.setVisibility(View.GONE);

                                                     UserBean user=response.body();
                                                     if(user!=null)
                                                     {
                                                         if(password.getText().toString().equals(user.getPassword()))
                                                         {
                                                             Intent i= new Intent(LoginActivity.this, MainActivity.class);
                                                             startActivity(i);
                                                         }
                                                         else{
                                                             Toast.makeText(LoginActivity.this,"Wrong Password",Toast.LENGTH_LONG).show();
                                                         }
                                                     }
                                                     else
                                                         Toast.makeText(LoginActivity.this,"Invalid Credentials",Toast.LENGTH_LONG).show();

                                                 }

                                                 @Override
                                                 public void onFailure(Throwable t) {
                                                     progress.setVisibility(View.GONE);
                                                     Toast.makeText(LoginActivity.this,"Server Error. Please try again after sometime.",Toast.LENGTH_LONG).show();

                                                 }
                                             });


                }

            }
        });
    }



}
