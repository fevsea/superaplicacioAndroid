package com.example.pr_idi.mydatabaseexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

public class ViewItem extends AppCompatActivity {

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
    String[] values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        values = getIntent().getStringArrayExtra("identifier");
        setTitle("View book");

        titleV = (TextView) findViewById(R.id.new_title);
        authorV = (TextView) findViewById(R.id.new_author);
        cathegoryV = (TextView) findViewById(R.id.new_cat);
        yearV = (TextView) findViewById(R.id.new_year);
        evalV = (RatingBar) findViewById(R.id.new_rat);

        titleV.setText(values[0]);
        authorV.setText(values[1]);
        cathegoryV.setText(values[2]);
        yearV.setText(values[3].equals("-1") ? "Unknown" : values[3]);
        evalV.setRating(Float.valueOf(values[4]));

        titleV.setEnabled(false);
        authorV.setEnabled(false);
        cathegoryV.setEnabled(false);
        yearV.setEnabled(false);
        evalV.setEnabled(false);

    }

}
