package com.steveq.getfit.model;

import java.util.ArrayList;
import java.util.List;

public class Meal {

    private String mName;
    private ArrayList<Food> mFoodList;

    public Meal(String name) {
        mName = name;
        mFoodList = new ArrayList<>();
    }

    public ArrayList<Food> getFoodList() {
        return mFoodList;
    }

    public void setFoodList(ArrayList<Food> foodList) {
        mFoodList = foodList;
    }

    public String toString(){
        return mName;
    }
}
