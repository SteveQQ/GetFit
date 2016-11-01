package com.steveq.getfit.controller;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public class DismissKeybord implements View.OnClickListener {

    Activity mActivity;

    public DismissKeybord(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onClick(View v) {
        if ( v instanceof ViewGroup) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
