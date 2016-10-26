package com.steveq.getfit.model;

public class Food {

    private String mName;
    private String mCalories;
    private String mCarbo;
    private String mProtein;
    private String mFat;

    public Food(String name, String calories, String carbo, String protein, String fat) {
        mName = name;
        mCalories = calories;
        mCarbo = carbo;
        mProtein = protein;
        mFat = fat;
    }

    public String getName() {
        return mName;
    }

    public String getCalories() {
        return mCalories;
    }

    public String getCarbo() {
        return mCarbo;
    }

    public String getProtein() {
        return mProtein;
    }

    public String getFat() {
        return mFat;
    }
}
