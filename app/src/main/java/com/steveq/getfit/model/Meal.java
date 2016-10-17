package com.steveq.getfit.model;

import java.util.ArrayList;
import java.util.List;

public class Meal {

    private String mName;
    private List<Food> mFoodList = new ArrayList<>();

    public Meal(String name) {
        mName = name;
    }
}
