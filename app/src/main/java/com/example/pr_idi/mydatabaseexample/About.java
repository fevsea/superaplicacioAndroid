package com.example.pr_idi.mydatabaseexample;

import android.os.Bundle;

public class About extends BackToolBar {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        super.onCreate("About");
    }

}
