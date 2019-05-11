package com.hariofspades.chatbot.Adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hariofspades.chatbot.Pojo.BookBean;
import com.hariofspades.chatbot.R;

import java.util.ArrayList;

public class CheckoutAdapter  extends RecyclerView.Adapter<CheckoutAdapter.MyViewHolder> {

    private ArrayList<BookBean> books;
    private AppCompatActivity context;

    public CheckoutAdapter(ArrayList<BookBean> books, AppCompatActivity context) {
        this.books = books;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_checkout_row,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BookBean book = books.get(position);
        holder.book_name.setText(book.getBook_name());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView book_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            book_name = itemView.findViewById(R.id.book_name);
        }
    }
}
