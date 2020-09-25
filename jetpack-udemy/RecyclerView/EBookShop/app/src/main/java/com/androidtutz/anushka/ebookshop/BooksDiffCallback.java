package com.androidtutz.anushka.ebookshop;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.androidtutz.anushka.ebookshop.model.Book;

import java.util.ArrayList;

public class BooksDiffCallback extends DiffUtil.Callback {
    ArrayList<Book> oldBooksList;
    ArrayList<Book> newBooksList;

    public BooksDiffCallback(ArrayList<Book> oldBooksList, ArrayList<Book> newBooksList) {
        this.oldBooksList = oldBooksList;
        this.newBooksList = newBooksList;
    }

    @Override
    public int getOldListSize() {
        return oldBooksList == null ? 0 : oldBooksList.size();
    }

    @Override
    public int getNewListSize() {
        return newBooksList == null ? 0 :newBooksList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldBookPosition, int newBookPosition) {
        return oldBooksList.get(oldBookPosition).getBookId()==newBooksList.get(newBookPosition).getBookId();
    }





    @Override
    public boolean areContentsTheSame(int oldBookPosition, int newBookPosition) {
        return oldBooksList.get(oldBookPosition).equals(newBooksList.get(newBookPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldBookPosition, int newBookPosition) {
        return super.getChangePayload(oldBookPosition, newBookPosition);
    }
}
