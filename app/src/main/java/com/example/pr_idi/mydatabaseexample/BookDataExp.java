package com.example.pr_idi.mydatabaseexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


class BookDataExp extends BookData {
    enum Screens {TITLE, AUTHOR, CATHEGORY}
    private Screens actual;
    private Context context;

    BookDataExp(Context context) {
        super(context);
        this.context = context;
    }

    void checkFirstInit() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(!prefs.getBoolean("firstTime", false)) {
            addInitBooks();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }

    }

    void setScreen(Screens actualScreen) {
        actual = actualScreen;
    }

    List<Book> generateBooks() {
        List<Book> books = getAllBooks();
        if (actual == BookDataExp.Screens.CATHEGORY){
            Collections.sort(books, new BookDataExp.compareByCathegory() );
        } else if (actual == BookDataExp.Screens.AUTHOR){
            Collections.sort(books, new BookDataExp.compareByAuthor() );
        } else if (actual == BookDataExp.Screens.TITLE){
            Collections.sort(books, new BookDataExp.compareByTitle() );
        }
        return books;
    }

    private class compareByTitle implements Comparator<Book> {
        @Override
        public int compare(Book a, Book b) {
            return a.getTitle().toLowerCase().compareTo(b.getTitle().toLowerCase());
        }
    }
    private class compareByAuthor implements Comparator<Book> {
        @Override
        public int compare(Book a, Book b) {
            return a.getAuthor().toLowerCase().compareTo(b.getAuthor().toLowerCase());
        }
    }
    private class compareByCathegory implements Comparator<Book> {
        @Override
        public int compare(Book a, Book b) {
            return a.getCategory().toLowerCase().compareTo(b.getCategory().toLowerCase());
        }
    }
    private void addInitBooks() {
        // Books aded the first time app is excuted
        createBook("Eragon","Christopher Paolini", "Fantasy novel", "2002", "4");
        createBook("Ender's Game","Orson Scott Card", " Science fiction", "1985", "5");
        createBook("Prelude to Foundation","Isaac Asimov", " Science fiction", "1988", "5");
        createBook("The Call of Cthulu"," H. P. Lovecraft", " Horror", "1928", "4");
    }

}
