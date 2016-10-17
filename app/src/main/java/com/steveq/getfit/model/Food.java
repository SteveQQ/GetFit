package com.steveq.getfit.model;

public class Food {

    private String mName;
    private int mCalories;
    private int mCarbo;
    private int mProtein;
    private int mFat;

    public Food(String name, int calories, int carbo, int protein, int fat) {
        mName = name;
        mCalories = calories;
        mCarbo = carbo;
        mProtein = protein;
        mFat = fat;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getCalories() {
        return mCalories;
    }

    public void setCalories(int calories) {
        mCalories = calories;
    }

    public int getCarbo() {
        return mCarbo;
    }

    public void setCarbo(int carbo) {
        mCarbo = carbo;
    }

    public int getProtein() {
        return mProtein;
    }

    public void setProtein(int protein) {
        mProtein = protein;
    }

    public int getFat() {
        return mFat;
    }

    public void setFat(int fat) {
        mFat = fat;
    }
}
