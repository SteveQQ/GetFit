package com.steveq.getfit.controller;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.steveq.getfit.R;
import com.steveq.getfit.model.Meal;

public class TodayPlanFragment extends ListFragment {


    public TodayPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
