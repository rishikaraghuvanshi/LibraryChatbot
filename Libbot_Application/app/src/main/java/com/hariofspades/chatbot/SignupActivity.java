package com.hariofspades.chatbot;



import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hariofspades.chatbot.Pojo.ResponseBean;
import com.hariofspades.chatbot.Pojo.UserBean;

import retrofit.APIClient;
import retrofit.RetrofitClientInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupActivity extends AppCompatActivity {
    private TextView login;
    private EditText input_name, enrollment, r_name, password, mailid;
    private Button btn_signup;
    private TextInputLayout layoutenrollment, layoutroomname;
    private RadioButton rb_admin, rb_student;
    private UserBean user;
    private RetrofitClientInterface retrofitClientInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        login = (TextView) findViewById(R.id.tv_login);
        input_name = (EditText) findViewById(R.id.input_name);
        mailid = (EditText) findViewById(R.id.mailid);
        enrollment = (EditText) findViewById(R.id.enrollment);
        password = (EditText) findViewById(R.id.password);
        layoutenrollment = (TextInputLayout) findViewById(R.id.layoutenrollment);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        rb_student = (RadioButton) findViewById(R.id.rb_student);
        rb_admin = (RadioButton) findViewById(R.id.rb_admin);

        retrofitClientInterface =  APIClient.getClient().create(RetrofitClientInterface.class);

        rb_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layoutenrollment.setHint("Username");


            }
        });
        rb_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enrollment.getText().toString().equals("") || enrollment.getText().toString().equals("Username")) {

                    layoutenrollment.setHint("Enrollment");
                }

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user=new UserBean();

                user.setEmail(mailid.getText().toString());
                user.setName(input_name.getText().toString());
                user.setPassword(password.getText().toString());
                if(rb_admin.isChecked())
                    user.setType("Admin");
                else
                    user.setType("Student");
                user.setUsername(enrollment.getText().toString());

                Call<ResponseBean> retrofitUserCall =   retrofitClientInterface.addUser(user);

                retrofitUserCall.enqueue(new Callback<ResponseBean>() {

                    @Override
                    public void onResponse(Response<ResponseBean> response) {
                        ResponseBean res=response.body();

                        if(res.getStatus()==200)
                        {
                            Toast.makeText(SignupActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                            finish();
                        }
                        else
                            Toast.makeText(SignupActivity.this, "Server Error", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(SignupActivity.this, "Server Error", Toast.LENGTH_LONG).show();

                    }
                });


                }
        });



    }
}