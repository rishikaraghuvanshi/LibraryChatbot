package com.hariofspades.chatbot;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hariofspades.chatbot.Adapter.CheckoutAdapter;
import com.hariofspades.chatbot.Pojo.BookBean;
import com.hariofspades.chatbot.Pojo.ResponseBean;

import java.util.ArrayList;

import retrofit.APIClient;
import retrofit.RetrofitClientInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView list;
    private CheckoutAdapter checkoutAdapter;
    private String user;
    private ArrayList<BookBean> books;
    private Button submit;
    private RetrofitClientInterface retrofitClientInterface;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        list = findViewById(R.id.list);
        submit = findViewById(R.id.submit);
        retrofitClientInterface =  APIClient.getClient().create(RetrofitClientInterface.class);
        progressDialog = new ProgressDialog(CheckoutActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait...");

        books = (ArrayList<BookBean>) getIntent().getSerializableExtra("books");
        user = getIntent().getStringExtra("user");
        if(books == null || books.size() ==0 || user == null || user.equals("") )
        {
            Toast.makeText(this, "No Books to Checkout", Toast.LENGTH_SHORT).show();
            finish();
        }

        checkoutAdapter = new CheckoutAdapter(books,CheckoutActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        list.setLayoutManager(mLayoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.addItemDecoration(new DividerItemDecoration(CheckoutActivity.this, LinearLayoutManager.VERTICAL));
        list.setAdapter(checkoutAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Call<ResponseBean> call = retrofitClientInterface.checkout(books,user);

                call.enqueue(new Callback<ResponseBean>() {
                    @Override
                    public void onResponse(Response<ResponseBean> response) {
                        progressDialog.dismiss();
                        ResponseBean res  = response.body();
                        if(res == null)
                        {
                            Toast.makeText(CheckoutActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(CheckoutActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(CheckoutActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
