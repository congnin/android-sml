package com.androidtutz.anushka.ebookshop;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidtutz.anushka.ebookshop.databinding.BookListItemBinding;
import com.androidtutz.anushka.ebookshop.model.Book;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder>{
    private OnItemClickListener listener;
    private ArrayList<Book> books=new ArrayList<>();

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        BookListItemBinding bookListItemBinding= DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.book_list_item,viewGroup,false);
        return new BookViewHolder(bookListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {
         Book book=books.get(i);
         bookViewHolder.bookListItemBinding.setBook(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(ArrayList<Book> newBooks) {
       // this.books = books;
       // notifyDataSetChanged();
       final DiffUtil.DiffResult result=DiffUtil.calculateDiff(new BooksDiffCallback(books,newBooks),false);
       books=newBooks;
       result.dispatchUpdatesTo(BooksAdapter.this);

    }

    class BookViewHolder extends RecyclerView.ViewHolder{
    private BookListItemBinding bookListItemBinding;

    public BookViewHolder(@NonNull BookListItemBinding bookListItemBinding) {
        super(bookListItemBinding.getRoot());
        this.bookListItemBinding=bookListItemBinding;
        bookListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int clickedPosition=getAdapterPosition();

              if(listener!=null && clickedPosition!=RecyclerView.NO_POSITION) {
                  listener.onItemClick(books.get(clickedPosition));
              }
            }
        });

    }

}

public interface OnItemClickListener{
    void onItemClick(Book book);
}

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
