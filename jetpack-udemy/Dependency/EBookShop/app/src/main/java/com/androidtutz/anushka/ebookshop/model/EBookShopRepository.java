package com.androidtutz.anushka.ebookshop.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class EBookShopRepository {
   private CategoryDAO categoryDAO;
   private BookDAO bookDAO;
   private LiveData<List<Category>> categories;
   private LiveData<List<Book>> books;

    public EBookShopRepository(Application application) {

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

    public void insertCategory(Category category){

        new InsertCategoryAsyncTask(categoryDAO).execute(category);
    }

    private static class InsertCategoryAsyncTask extends AsyncTask<Category,Void,Void>{
        private CategoryDAO categoryDAO;

        public InsertCategoryAsyncTask(CategoryDAO categoryDAO) {
            this.categoryDAO = categoryDAO;
        }

        @Override
        protected Void doInBackground(Category... categories) {

            categoryDAO.insert(categories[0]);
            return null;
        }
    }

    public void insertBook(Book book){

        new InsertBookAsyncTask(bookDAO).execute(book);
    }

    private static class InsertBookAsyncTask extends AsyncTask<Book,Void,Void>{
        private BookDAO bookDAO;

        public InsertBookAsyncTask(BookDAO bookDAO) {
            this.bookDAO = bookDAO;
        }

        @Override
        protected Void doInBackground(Book... books) {

            bookDAO.insert(books[0]);
            return null;
        }
    }

    public void deleteCategory(Category category){

        new DeleteCategoryAsyncTask(categoryDAO).execute(category);
    }

    private static class DeleteCategoryAsyncTask extends AsyncTask<Category,Void,Void>{
        private CategoryDAO categoryDAO;

        public DeleteCategoryAsyncTask(CategoryDAO categoryDAO) {
            this.categoryDAO = categoryDAO;
        }

        @Override
        protected Void doInBackground(Category... categories) {

            categoryDAO.delete(categories[0]);
            return null;
        }
    }

    public void deleteBook(Book book){

        new DeleteBookAsyncTask(bookDAO).execute(book);
    }

    private static class DeleteBookAsyncTask extends AsyncTask<Book,Void,Void>{
        private BookDAO bookDAO;

        public DeleteBookAsyncTask(BookDAO bookDAO) {
            this.bookDAO = bookDAO;
        }

        @Override
        protected Void doInBackground(Book... books) {

            bookDAO.delete(books[0]);
            return null;
        }
    }

    public void updateCategory(Category category){

        new UpdateCategoryAsyncTask(categoryDAO).execute(category);
    }

    private static class UpdateCategoryAsyncTask extends AsyncTask<Category,Void,Void>{
        private CategoryDAO categoryDAO;

        public UpdateCategoryAsyncTask(CategoryDAO categoryDAO) {
            this.categoryDAO = categoryDAO;
        }

        @Override
        protected Void doInBackground(Category... categories) {

            categoryDAO.update(categories[0]);
            return null;
        }
    }

    public void updateBook(Book book){

        new UpdateBookAsyncTask(bookDAO).execute(book);
    }

    private static class UpdateBookAsyncTask extends AsyncTask<Book,Void,Void>{
        private BookDAO bookDAO;

        public UpdateBookAsyncTask(BookDAO bookDAO) {
            this.bookDAO = bookDAO;
        }

        @Override
        protected Void doInBackground(Book... books) {

            bookDAO.update(books[0]);
            return null;
        }
    }

}
