package com.androidtutz.anushka.ebookshop.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EBookShopRepository {
   private CategoryDAO categoryDAO;
   private BookDAO bookDAO;
   private LiveData<List<Category>> categories;
   private LiveData<List<Book>> books;
    private Executor executor;

    public EBookShopRepository(Application application) {

        executor= Executors.newFixedThreadPool(5);
        BooksDatabase booksDatabase=BooksDatabase.getInstance(application);
        categoryDAO=booksDatabase.categoryDAO();
        bookDAO=booksDatabase.bookDAO();
    }

    public LiveData<List<Category>> getCategories() {
        return categoryDAO.getAllCategories();
    }

    public LiveData<List<Book>> getBooks(int categoryId) {
        return bookDAO.getBooks(categoryId);
    }

    public void insertCategory(final Category category){

        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDAO.insert(category);
            }
        });
    }


    public void insertBook(final Book book){

        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                bookDAO.insert(book);
            }
        });

    }

    public void deleteCategory(final Category category){
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDAO.delete(category);
            }
        });
    }

    public void deleteBook(final Book book){
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                bookDAO.delete(book);
            }
        });
    }

    public void updateCategory(final Category category){

        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDAO.update(category);
            }
        });
    }

    public void updateBook(final Book book){
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                bookDAO.update(book);
            }
        });
    }


}
