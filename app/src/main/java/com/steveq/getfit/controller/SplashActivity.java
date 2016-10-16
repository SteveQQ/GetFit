package com.steveq.getfit.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.steveq.getfit.model.UserManager;

public class SplashActivity extends Activity {

    private UserManager mUserManager;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public static final String PREFS_FILE = "com.steveq.getfit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = getSharedPreferences(PREFS_FILE, Activity.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mUserManager = UserManager.getInstance(this, mSharedPreferences, mEditor);
        mUserManager.initializeUsersList();


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
