package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;

class CustomDialogClass extends Dialog {

    private Activity activity;
    private Book book;

    CustomDialogClass(Activity a, Book book) {
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
        rating.setStepSize(1.0f);
        if (!value.equals(BookData.unknown))
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
}