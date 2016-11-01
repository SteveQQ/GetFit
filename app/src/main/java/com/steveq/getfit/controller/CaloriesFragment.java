package com.steveq.getfit.controller;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.steveq.getfit.R;
import com.steveq.getfit.model.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaloriesFragment extends Fragment {

    @BindView(R.id.ageEditText) EditText ageEditText;
    @BindView(R.id.radioGroup) RadioGroup radioGroup;
    @BindView(R.id.heightEditText) EditText heightEditText;
    @BindView(R.id.weightEditText) EditText weightEditText;
    @BindView(R.id.caloriesEditText) EditText caloriesEditText;
    @BindView(R.id.suggestCaloriesButton) Button suggestCaloriesButton;
    @BindView(R.id.confirmCaloriesButton) Button confirmCaloriesButton;
    private UserManager um = UserManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.prefs_layout, container, false);
        view.setOnClickListener(new DismissKeybord(getActivity()));
        ButterKnife.bind(this, view);


        if(um.getCurrentUser().getAge() != 0){
            ageEditText.setText(String.valueOf(um.getCurrentUser().getAge()));

            RadioButton rb = (RadioButton) radioGroup.findViewWithTag(um.getCurrentUser().getSex());
            rb.setChecked(true);

            heightEditText.setText(String.valueOf(um.getCurrentUser().getHeight()));
            weightEditText.setText(String.valueOf(um.getCurrentUser().getWeight()));
            caloriesEditText.setText(String.valueOf(um.getCurrentUser().getCalories()));

        }

        suggestCaloriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                if(     !ageEditText.getText().toString().equals("") &&
                        !radioButton.getText().toString().equals("") &&
                        !heightEditText.getText().toString().equals("")&&
                        !weightEditText.getText().toString().equals("")){

                    caloriesEditText.setText(String.valueOf(countCalories(Integer.valueOf(ageEditText.getText().toString()),
                                                            radioButton.getText().toString(),
                                                            Integer.valueOf(heightEditText.getText().toString()),
                                                            Integer.valueOf(weightEditText.getText().toString()))));

                    um.getCurrentUser().setAge(Integer.valueOf(ageEditText.getText().toString()));
                    um.getCurrentUser().setSex(radioButton.getText().toString());
                    um.getCurrentUser().setHeight(Integer.valueOf(heightEditText.getText().toString()));
                    um.getCurrentUser().setWeight(Integer.valueOf(weightEditText.getText().toString()));

                } else {
                    Toast.makeText(getActivity(), "Insert All Parameters", Toast.LENGTH_SHORT).show();
                }


            }
        });

        confirmCaloriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                um.getCurrentUser().setCalories(Integer.valueOf(caloriesEditText.getText().toString()));
                um.saveUser(um.getCurrentUser().getUserName(), um.getCurrentUser());
                Toast.makeText(getActivity(), "SAVED", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    private int countCalories(int age, String sex, int height, int weight) {

        boolean isMale = sex.equals("male");
        int calories;

        if(isMale){
            calories =(int)(66.47 + (13.75 * weight) + (5 * height) - (6.75 * age));
        } else {
            calories = (int)(665.09 + (9.56 * weight) + (1.84 * height) - (4.67 * age));
        }

        return calories;
    }

}
