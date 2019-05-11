package com.hariofspades.chatbot.Adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hariofspades.chatbot.Pojo.BookBean;
import com.hariofspades.chatbot.Pojo.ReturnBean;
import com.hariofspades.chatbot.R;

import java.util.ArrayList;

public class ReturnAdapter extends RecyclerView.Adapter<ReturnAdapter.MyViewHolder>  {

    private ArrayList<ReturnBean> books;
    private AppCompatActivity context;

    public ReturnAdapter(ArrayList<ReturnBean> books, AppCompatActivity context) {
        this.books = books;
        this.context = context;
    }

    @Override
    public ReturnAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_return_row,parent,false);
        return new ReturnAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReturnBean mybook = books.get(position);
        holder.book.setText(mybook.getBook_name());
        holder.fine.setText(String.valueOf(mybook.getFine()));
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
