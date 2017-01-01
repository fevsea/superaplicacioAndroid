package com.example.pr_idi.mydatabaseexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class Evaluation extends BookData {
    public Evaluation(Context context) {super(context);}

    public void makeEvaluation(String name, String evaluation) {
        Book book = getBook(name);
        makeEvaluation(book, evaluation);
    }
    public void makeEvaluation(Long id, String evaluation) {
        Book book = getBook(id);
        makeEvaluation(book, evaluation);
    }
    public void makeEvaluation(Book book, String evaluation) {
        book.setPersonal_evaluation(evaluation);
        updateBook(book);
    }

    public Book getBook(String name) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS, allColumns,
                MySQLiteHelper.COLUMN_TITLE + " = " +  name, null, null, null, null);
        return cursorToBook(cursor);
    }
    public Book getBook(Long id) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_BOOKS, allColumns,
                MySQLiteHelper.COLUMN_ID + " = " +  id, null, null, null, null);
        return cursorToBook(cursor);
    }

    public void updateBook(Book book) {
        ContentValues valuesOfBook = bookToContent(book);
        database.update(MySQLiteHelper.TABLE_BOOKS, valuesOfBook,
                MySQLiteHelper.COLUMN_ID + " = " + book.getId(), null);
    }

    private ContentValues bookToContent(Book book) {
        ContentValues valuesOfBook = new ContentValues();
        if (book.getAuthor() != null)
            valuesOfBook.put(MySQLiteHelper.COLUMN_AUTHOR, book.getAuthor());
        if (book.getCategory() != null)
            valuesOfBook.put(MySQLiteHelper.COLUMN_CATEGORY, book.getCategory());
        if (book.getPersonal_evaluation() != null)
            valuesOfBook.put(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION, book.getPersonal_evaluation());
        if (book.getPublisher() != null)
            valuesOfBook.put(MySQLiteHelper.COLUMN_PUBLISHER, book.getPublisher());
        if (book.getTitle() != null)
            valuesOfBook.put(MySQLiteHelper.COLUMN_TITLE, book.getTitle());

        valuesOfBook.put(MySQLiteHelper.COLUMN_YEAR, book.getYear());
        valuesOfBook.put(MySQLiteHelper.COLUMN_ID, book.getId());
        return valuesOfBook;
    }

    private Book cursorToBook(Cursor cursor) { //ToDo: debug not sure
        cursor.moveToFirst();
        Book book = new Book();
        if (cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID) != -1)
            book.setId(cursor.getLong(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID)));
        if (cursor.getColumnIndex(MySQLiteHelper.COLUMN_TITLE) != -1)
            book.setTitle(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TITLE)));
        if (cursor.getColumnIndex(MySQLiteHelper.COLUMN_AUTHOR) != -1)
            book.setAuthor(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_AUTHOR)));
        if (cursor.getColumnIndex(MySQLiteHelper.COLUMN_YEAR) != -1)
            book.setYear(cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_YEAR)));
        if (cursor.getColumnIndex(MySQLiteHelper.COLUMN_PUBLISHER) != -1)
            book.setPublisher(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_PUBLISHER)));
        if (cursor.getColumnIndex(MySQLiteHelper.COLUMN_CATEGORY) != -1)
            book.setCategory(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_CATEGORY)));
        if (cursor.getColumnIndex(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION) != -1)
            book.setPersonal_evaluation(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_PERSONAL_EVALUATION)));
        return book;
    }
}
