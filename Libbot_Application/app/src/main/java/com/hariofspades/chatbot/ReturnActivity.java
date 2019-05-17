package com.hariofspades.chatbot;

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
import com.hariofspades.chatbot.Adapter.ReturnAdapter;
import com.hariofspades.chatbot.Pojo.ResponseBean;
import com.hariofspades.chatbot.Pojo.ReturnBean;

import java.util.ArrayList;

import retrofit.APIClient;
import retrofit.RetrofitClientInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnActivity extends AppCompatActivity {

    private RecyclerView list;
    private Button submit;
    private ArrayList<ReturnBean> books;
    private String user;
    private RetrofitClientInterface retrofitClientInterface;
    private ReturnAdapter returnAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        list = findViewById(R.id.list);
        submit= findViewById(R.id.submit);
        books = (ArrayList<ReturnBean>) getIntent().getSerializableExtra("list");
        user = getIntent().getStringExtra("user");
        retrofitClientInterface = APIClient.getClient().create(RetrofitClientInterface.class);

        if(books == null || books.size() ==0 || user == null || user.equals("") )
        {
            Toast.makeText(this, "No Books to return", Toast.LENGTH_SHORT).show();
            finish();
        }

        returnAdapter = new ReturnAdapter(user,books,ReturnActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        list.setLayoutManager(mLayoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.addItemDecoration(new DividerItemDecoration(ReturnActivity.this, LinearLayoutManager.VERTICAL));
        list.setAdapter(returnAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBean> call =  retrofitClientInterface.returnBook(books,user);
                call.enqueue(new Callback<ResponseBean>() {
                    @Override
                    public void onResponse(Response<ResponseBean> response) {
                        ResponseBean res = response.body();
                        if(res.getStatus()==200)
                        {
                            Toast.makeText(ReturnActivity.this, "Books returned", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        Toast.makeText(ReturnActivity.this, "Error Encountered", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(ReturnActivity.this, "Error Encountered", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
