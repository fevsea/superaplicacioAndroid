package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    private Activity activity;
    private Book book;

    public CustomDialogClass(Activity a, Book book) {
        super(a);
        this.activity = a;
        this.book = book;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        String value = book.getPersonal_evaluation();
        Log.i("a", value);
        if (value != BookData.unknown)
            rating.setRating(Float.parseFloat(value));
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                BookData bookData = new BookData(activity.getApplicationContext());
                bookData.open();
                book.setPersonal_evaluation(String.valueOf(v));
                bookData.update(book);
                bookData.close();
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {}
}