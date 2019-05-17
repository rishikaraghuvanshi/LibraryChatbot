package com.hariofspades.chatbot.Adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hariofspades.chatbot.Pojo.BookBean;
import com.hariofspades.chatbot.Pojo.ResponseBean;
import com.hariofspades.chatbot.Pojo.ReturnBean;
import com.hariofspades.chatbot.R;
import com.hariofspades.chatbot.ReturnActivity;

import java.util.ArrayList;

import retrofit.APIClient;
import retrofit.RetrofitClientInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnAdapter extends RecyclerView.Adapter<ReturnAdapter.MyViewHolder>  {

    private ArrayList<ReturnBean> books;
    private AppCompatActivity context;
    private String user;

    public ReturnAdapter(String user,ArrayList<ReturnBean> books, AppCompatActivity context) {
        this.books = books;
        this.context = context;
        this.user = user;
    }

    @Override
    public ReturnAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_return_row,parent,false);
        return new ReturnAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ReturnBean mybook = books.get(position);
        holder.book.setText(mybook.getBook_name());
        holder.fine.setText(String.valueOf(mybook.getFine()));
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to return this book?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RetrofitClientInterface retrofitClientInterface = APIClient.getClient().create(RetrofitClientInterface.class);
                        ArrayList<ReturnBean> book = new ArrayList<>();
                        book.add(books.get(position));
                        Call<ResponseBean> call =  retrofitClientInterface.returnBook(book,user);
                        call.enqueue(new Callback<ResponseBean>() {
                            @Override
                            public void onResponse(Response<ResponseBean> response) {
                                ResponseBean res = response.body();
                                if(res.getStatus()==200)
                                {
                                    Toast.makeText(context, "Book returned", Toast.LENGTH_SHORT).show();
                                    books.remove(books.get(position));
                                    notifyDataSetChanged();
                                    return;
                                }
                                Toast.makeText(context, "Error Encountered", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Toast.makeText(context, "Error Encountered", Toast.LENGTH_SHORT).show();
                            }
                    });
                    }
                });
                builder.show();
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return books.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView book,fine;
        public RelativeLayout parent;

        public MyViewHolder(View itemView) {
            super(itemView);
            book = itemView.findViewById(R.id.book);
            fine = itemView.findViewById(R.id.fine);
            parent = itemView.findViewById(R.id.layout);
        }
    }
}
