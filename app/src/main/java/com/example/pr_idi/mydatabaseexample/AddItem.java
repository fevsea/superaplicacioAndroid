package com.example.pr_idi.mydatabaseexample;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class AddItem extends AppCompatActivity {

    public static final String TAG = PrincipalActivity.class.getSimpleName();
    TextView titleV;
    TextView authorV;
    TextView cathegoryV;
    TextView yearV;
    RatingBar evalV;
    String title;
    String author;
    String cathegory;
    String year;
    String eval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Log.d(TAG, "Ok");
    }

    public void onClick(View view){
        titleV = (TextView) findViewById(R.id.new_title);
        authorV = (TextView) findViewById(R.id.new_author);
        cathegoryV = (TextView) findViewById(R.id.new_cat);
        yearV = (TextView) findViewById(R.id.new_year);
        evalV = (RatingBar) findViewById(R.id.new_rat);

        title = titleV.getText().toString();

        if (title == null || title.isEmpty()){
            Toast.makeText(getApplicationContext(), "Title is mandatory", Toast.LENGTH_LONG).show();
            return;
        } else {
            author = authorV.getText().toString();
            ;
            if (author.isEmpty()) {
                author = null;
            }
            cathegory = cathegoryV.getText().toString();
            if (cathegory.isEmpty()) {
                cathegory = null;
                ;
            }
            year = yearV.getText().toString();
            if (year.isEmpty()) {
                year = null;
                ;
            }

            eval = String.valueOf(evalV.getRating());

            BookData mBookData = new BookData(getApplicationContext());
            mBookData.open();
            mBookData.createBook(title, author, cathegory, year, eval);
            mBookData.close();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }

    }
}
