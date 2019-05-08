package com.hariofspades.chatbot;



import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText layoutname, layoutenrollment, r_name, layoutpassword, emailid;
    private Button btn_signup;
    private RadioButton rb_admin, rb_student, rb_teacher;
    private UserBean user;
    private RetrofitClientInterface retrofitClientInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        login = (TextView) findViewById(R.id.tv_login);
        layoutname = (EditText) findViewById(R.id.layoutname);
        emailid = (EditText) findViewById(R.id.emailid);
        layoutenrollment = (EditText) findViewById(R.id.layoutenrollment);
        layoutpassword = (EditText) findViewById(R.id.password);
        btn_signup = (Button) findViewById(R.id.btn_request);
        rb_student = (RadioButton) findViewById(R.id.rb_student);
        rb_admin = (RadioButton) findViewById(R.id.rb_admin);
        rb_teacher= (RadioButton) findViewById(R.id.rb_teacher);

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
                if (layoutenrollment.getText().toString().equals("") || layoutenrollment.getText().toString().equals("Username")) {

                    layoutenrollment.setHint("Enrollment");
                }

            }
        });
                rb_teacher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                            layoutenrollment.setHint("Employee Id");


                    }
                }
        );


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

                user.setEmail(emailid.getText().toString());
                user.setName(layoutname.getText().toString());
                user.setPassword(layoutpassword.getText().toString());
                if(rb_admin.isChecked())
                    user.setType("Admin");
                else if(rb_teacher.isChecked())
                    user.setType("Teacher");
                else
                    user.setType("Student");
                user.setUsername(layoutenrollment.getText().toString());

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