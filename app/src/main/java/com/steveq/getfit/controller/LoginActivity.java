package com.steveq.getfit.controller;

import android.app.Activity;
import android.os.Bundle;

import com.steveq.getfit.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
